terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

# Used to get the AWS Account ID dynamically for SSM resource ARNs
data "aws_caller_identity" "current" {}

# ------------------------------------------------------------------------------
# ECR REPOSITORY
# ------------------------------------------------------------------------------
resource "aws_ecr_repository" "app_repo" {
  name                 = var.ecr_repository_name
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }
}

# ------------------------------------------------------------------------------
# VPC NETWORKING (Budget-friendly Public Subnet Architecture)
# ------------------------------------------------------------------------------
resource "aws_vpc" "main" {
  cidr_block                       = var.vpc_cidr
  assign_generated_ipv6_cidr_block = true
  enable_dns_hostnames             = true
  enable_dns_support               = true

  tags = {
    Name        = "${var.environment}-vpc"
    Environment = var.environment
  }
}

# We are hardcoding the availability zones here because the 'cli_user' 
# does not have the 'ec2:DescribeAvailabilityZones' IAM permission.
# These zones must exist in the us-east-1 region (which is our default).
locals {
  azs = ["us-east-1a", "us-east-1b"]
}

resource "aws_internet_gateway" "gw" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name        = "${var.environment}-igw"
    Environment = var.environment
  }
}

# Create 2 Public Subnets (One in each AZ for high availability)
resource "aws_subnet" "public" {
  count                           = 2
  vpc_id                          = aws_vpc.main.id
  cidr_block                      = cidrsubnet(aws_vpc.main.cidr_block, 8, count.index)
  ipv6_cidr_block                 = cidrsubnet(aws_vpc.main.ipv6_cidr_block, 8, count.index)
  availability_zone               = local.azs[count.index]
  map_public_ip_on_launch         = true
  assign_ipv6_address_on_creation = true

  tags = {
    Name        = "${var.environment}-public-subnet-${count.index + 1}"
    Environment = var.environment
  }
}

# Route Table for Public Subnets
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.gw.id
  }

  route {
    ipv6_cidr_block = "::/0"
    gateway_id      = aws_internet_gateway.gw.id
  }

  tags = {
    Name        = "${var.environment}-public-rt"
    Environment = var.environment
  }
}

resource "aws_route_table_association" "public" {
  count          = 2
  subnet_id      = aws_subnet.public[count.index].id
  route_table_id = aws_route_table.public.id
}

# ------------------------------------------------------------------------------
# RDS POSTGRESQL INSTANCE
# ------------------------------------------------------------------------------
resource "aws_db_subnet_group" "db_subnet_group" {
  name       = "${var.environment}-db-subnet-group"
  subnet_ids = aws_subnet.public[*].id

  tags = {
    Name        = "${var.environment}-db-subnet-group"
    Environment = var.environment
  }
}

resource "aws_security_group" "db_security_group" {
  name        = "${var.environment}-db-sg"
  description = "RDS Security Group"
  vpc_id      = aws_vpc.main.id

  # Allow postgres port from ECS tasks
  ingress {
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    security_groups = [aws_security_group.ecs_tasks_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name        = "${var.environment}-db-sg"
    Environment = var.environment
  }
}

resource "aws_db_instance" "postgres" {
  identifier           = "${var.environment}-db"
  allocated_storage    = 20
  storage_type         = "gp2"
  engine               = "postgres"
  engine_version       = "15.16"
  instance_class       = var.db_instance_class
  db_name              = var.db_name
  username             = var.db_username
  password             = var.db_password
  db_subnet_group_name = aws_db_subnet_group.db_subnet_group.name
  vpc_security_group_ids = [aws_security_group.db_security_group.id]
  skip_final_snapshot  = true
  publicly_accessible  = true # Set to true for easier management since we are in public subnets

  tags = {
    Name        = "${var.environment}-db"
    Environment = var.environment
  }
}
