# ------------------------------------------------------------------------------
# SECURITY GROUPS
# ------------------------------------------------------------------------------
resource "aws_security_group" "alb_sg" {
  name        = "${var.environment}-alb-sg"
  description = "Allow port 80 traffic to ALB"
  vpc_id      = aws_vpc.main.id

  ingress {
    protocol    = "tcp"
    from_port   = 80
    to_port     = 80
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    protocol    = "tcp"
    from_port   = 443
    to_port     = 443
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    protocol         = "-1"
    from_port        = 0
    to_port          = 0
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

  tags = {
    Name        = "${var.environment}-alb-sg"
    Environment = var.environment
  }
}

resource "aws_security_group" "ecs_tasks_sg" {
  name        = "${var.environment}-ecs-tasks-sg"
  description = "Allow inbound access from the ALB only"
  vpc_id      = aws_vpc.main.id

  ingress {
    protocol        = "tcp"
    from_port       = var.app_port
    to_port         = var.app_port
    security_groups = [aws_security_group.alb_sg.id]
  }

  egress {
    protocol         = "-1"
    from_port        = 0
    to_port          = 0
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

  tags = {
    Name        = "${var.environment}-ecs-tasks-sg"
    Environment = var.environment
  }
}

# ------------------------------------------------------------------------------
# APPLICATION LOAD BALANCER (ALB)
# ------------------------------------------------------------------------------
resource "aws_lb" "main" {
  name               = "${var.environment}-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb_sg.id]
  subnets            = aws_subnet.public[*].id

  tags = {
    Name        = "${var.environment}-alb"
    Environment = var.environment
  }
}

resource "aws_lb_target_group" "app" {
  name        = "${var.environment}-tg"
  port        = 80
  protocol    = "HTTP"
  vpc_id      = aws_vpc.main.id
  target_type = "ip"

  health_check {
    healthy_threshold   = "3"
    interval            = "30"
    protocol            = "HTTP"
    matcher             = "200,302"
    timeout             = "3"
    path                = "/"
    unhealthy_threshold = "2"
  }

  tags = {
    Name        = "${var.environment}-tg"
    Environment = var.environment
  }
}

resource "aws_lb_listener" "http" {
  load_balancer_arn = aws_lb.main.arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type = "redirect"

    redirect {
      port        = "443"
      protocol    = "HTTPS"
      status_code = "HTTP_301"
    }
  }
}

resource "aws_lb_listener" "https" {
  load_balancer_arn = aws_lb.main.arn
  port              = "443"
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-2016-08"
  certificate_arn   = var.certificate_arn

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.app.arn
  }
}

# ------------------------------------------------------------------------------
# ECS CLUSTER AND IAM
# ------------------------------------------------------------------------------
resource "aws_ecs_cluster" "main" {
  name = "${var.environment}-cluster"

  tags = {
    Name        = "${var.environment}-cluster"
    Environment = var.environment
  }
}

data "aws_iam_policy_document" "ecs_task_execution_role" {
  statement {
    actions = ["sts:AssumeRole"]

    principals {
      type        = "Service"
      identifiers = ["ecs-tasks.amazonaws.com"]
    }
  }
}

resource "aws_iam_role" "ecs_task_execution_role" {
  name               = "${var.environment}-ecs-execution-role"
  assume_role_policy = data.aws_iam_policy_document.ecs_task_execution_role.json
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution_role" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

# Grant the ECS task execution role permission to read secrets from Systems Manager (SSM) Parameter Store
data "aws_iam_policy_document" "ecs_task_ssm_policy" {
  statement {
    actions   = ["ssm:GetParameters"]
    resources = ["arn:aws:ssm:${var.aws_region}:*:parameter/${var.environment}/*"]
  }
}

resource "aws_iam_role_policy" "ecs_task_ssm_policy" {
  name   = "${var.environment}-ecs-ssm-policy"
  role   = aws_iam_role.ecs_task_execution_role.name
  policy = data.aws_iam_policy_document.ecs_task_ssm_policy.json
}

resource "aws_cloudwatch_log_group" "ecs_log_group" {
  name              = "/ecs/${var.environment}-app"
  retention_in_days = 30
}

# ------------------------------------------------------------------------------
# SSM PARAMETERS FOR ECS
# ------------------------------------------------------------------------------
resource "aws_ssm_parameter" "db_url" {
  name  = "/${var.environment}/DB_URL"
  type  = "String"
  value = "jdbc:postgresql://${aws_db_instance.postgres.endpoint}/${var.db_name}"
}

resource "aws_ssm_parameter" "db_username" {
  name  = "/${var.environment}/DB_USERNAME"
  type  = "String"
  value = var.db_username
}

resource "aws_ssm_parameter" "db_password" {
  name  = "/${var.environment}/DB_PASSWORD"
  type  = "SecureString"
  value = var.db_password
}

# ------------------------------------------------------------------------------
# ECS TASK DEFINITION AND SERVICE (Initial Setup via Terraform)
# Note: GitHub Actions will takeover deploying new images to this service
# ------------------------------------------------------------------------------
resource "aws_ecs_task_definition" "app" {
  family                   = "${var.environment}-app-task"
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = var.fargate_cpu
  memory                   = var.fargate_memory

  container_definitions = jsonencode([
    {
      name      = "homemade-app"
      image     = "public.ecr.aws/amazonlinux/amazonlinux:latest" # GitHub action overwrites this
      cpu       = var.fargate_cpu
      memory    = var.fargate_memory
      essential = true
      
      # Non-sensitive configuration injected into Spring Boot
      environment = [
        { name = "SPRING_PROFILES_ACTIVE", value = "prod" }
      ]
      
      # Sensitive secrets automatically fetched from AWS Systems Manager (SSM) by AWS Fargate
      secrets = [
        { name = "DB_URL", valueFrom = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter/${var.environment}/DB_URL" },
        { name = "DB_USERNAME", valueFrom = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter/${var.environment}/DB_USERNAME" },
        { name = "DB_PASSWORD", valueFrom = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter/${var.environment}/DB_PASSWORD" },
        { name = "REDIS_URL", valueFrom = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter/${var.environment}/REDIS_URL" },
        { name = "KAFKA_BOOTSTRAP_SERVERS", valueFrom = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter/${var.environment}/KAFKA_BOOTSTRAP_SERVERS" },
        { name = "KAFKA_KEY_PASSWORD", valueFrom = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter/${var.environment}/KAFKA_KEY_PASSWORD" },
        { name = "KAFKA_KEYSTORE_BASE64", valueFrom = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter/${var.environment}/KAFKA_KEYSTORE_BASE64" },
        { name = "KAFKA_TRUSTSTORE_BASE64", valueFrom = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter/${var.environment}/KAFKA_TRUSTSTORE_BASE64" },
        { name = "STRIPE_SECRET_KEY", valueFrom = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter/${var.environment}/STRIPE_SECRET_KEY" },
        { name = "STRIPE_PUBLISHABLE_KEY", valueFrom = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter/${var.environment}/STRIPE_PUBLISHABLE_KEY" },
        { name = "STRIPE_WEBHOOK_SECRET", valueFrom = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter/${var.environment}/STRIPE_WEBHOOK_SECRET" },
        { name = "FRONTEND_SUCCESS_URL", valueFrom = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter/${var.environment}/FRONTEND_SUCCESS_URL" },
        { name = "FRONTEND_CANCEL_URL", valueFrom = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter/${var.environment}/FRONTEND_CANCEL_URL" },
        { name = "CLOUDINARY_CLOUD_NAME", valueFrom = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter/${var.environment}/CLOUDINARY_CLOUD_NAME" },
        { name = "CLOUDINARY_API_KEY", valueFrom = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter/${var.environment}/CLOUDINARY_API_KEY" },
        { name = "CLOUDINARY_API_SECRET", valueFrom = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter/${var.environment}/CLOUDINARY_API_SECRET" }
      ]

      portMappings = [
        {
          containerPort = var.app_port
          hostPort      = var.app_port
        }
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.ecs_log_group.name
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "main" {
  name            = "${var.environment}-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.app.arn
  desired_count   = var.app_count
  launch_type     = "FARGATE"

  network_configuration {
    security_groups  = [aws_security_group.ecs_tasks_sg.id]
    subnets          = aws_subnet.public[*].id # Deploy tasks tightly into public subnets
    assign_public_ip = true                    # Public IP is required to pull ECR images from public subnets
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.app.arn
    container_name   = "homemade-app"
    container_port   = var.app_port
  }

  depends_on = [aws_lb_listener.front_end]

  lifecycle {
    ignore_changes = [task_definition, desired_count] # Tells terraform to stop modifying app logic. Github Action manages image deployments!
  }
}

# ------------------------------------------------------------------------------
# AUTO SCALING
# ------------------------------------------------------------------------------
resource "aws_appautoscaling_target" "ecs_target" {
  max_capacity       = 2
  min_capacity       = 1
  resource_id        = "service/${aws_ecs_cluster.main.name}/${aws_ecs_service.main.name}"
  scalable_dimension = "ecs:service:DesiredCount"
  service_namespace  = "ecs"
}

resource "aws_appautoscaling_policy" "ecs_policy_cpu" {
  name               = "cpu-scaling"
  policy_type        = "TargetTrackingScaling"
  resource_id        = aws_appautoscaling_target.ecs_target.resource_id
  scalable_dimension = aws_appautoscaling_target.ecs_target.scalable_dimension
  service_namespace  = aws_appautoscaling_target.ecs_target.service_namespace

  target_tracking_scaling_policy_configuration {
    predefined_metric_specification {
      predefined_metric_type = "ECSServiceAverageCPUUtilization"
    }

    target_value       = 70.0
    scale_in_cooldown  = 300
    scale_out_cooldown = 60
  }
}

resource "aws_appautoscaling_policy" "ecs_policy_memory" {
  name               = "memory-scaling"
  policy_type        = "TargetTrackingScaling"
  resource_id        = aws_appautoscaling_target.ecs_target.resource_id
  scalable_dimension = aws_appautoscaling_target.ecs_target.scalable_dimension
  service_namespace  = aws_appautoscaling_target.ecs_target.service_namespace

  target_tracking_scaling_policy_configuration {
    predefined_metric_specification {
      predefined_metric_type = "ECSServiceAverageMemoryUtilization"
    }

    target_value       = 70.0
    scale_in_cooldown  = 300
    scale_out_cooldown = 60
  }
}

output "alb_hostname" {
  value = aws_lb.main.dns_name
}
