# ------------------------------------------------------------------------------
# GITHUB ACTIONS CI/CD USER
# ------------------------------------------------------------------------------

resource "aws_iam_user" "github_actions" {
  name = "${var.environment}-github-actions-user"
  tags = {
    Environment = var.environment
  }
}

resource "aws_iam_access_key" "github_actions" {
  user = aws_iam_user.github_actions.name
}

# ------------------------------------------------------------------------------
# CI/CD PERMISSIONS (ECR + ECS)
# ------------------------------------------------------------------------------
data "aws_iam_policy_document" "github_actions_policy" {
  # 1. Login to ECR
  statement {
    actions   = ["ecr:GetAuthorizationToken"]
    resources = ["*"]
  }

  # 2. Push/Pull from ECR
  statement {
    actions   = [
      "ecr:BatchCheckLayerAvailability",
      "ecr:GetDownloadUrlForLayer",
      "ecr:BatchGetImage",
      "ecr:PutImage",
      "ecr:InitiateLayerUpload",
      "ecr:UploadLayerPart",
      "ecr:CompleteLayerUpload"
    ]
    resources = [aws_ecr_repository.app_repo.arn]
  }

  # 3. ECS Deployment Permissions
  statement {
    actions   = [
      "ecs:DescribeTaskDefinition",
      "ecs:RegisterTaskDefinition",
      "ecs:DescribeServices",
      "ecs:UpdateService"
    ]
    resources = ["*"] # Ideally scoped to specific cluster/service arns
  }

  # 4. PassRole to ECS Execution Role (Required for registering task definition)
  statement {
    actions   = ["iam:PassRole"]
    resources = [aws_iam_role.ecs_task_execution_role.arn]
  }
}

resource "aws_iam_user_policy" "github_actions_policy" {
  name   = "${var.environment}-github-actions-policy"
  user   = aws_iam_user.github_actions.name
  policy = data.aws_iam_policy_document.github_actions_policy.json
}

# ------------------------------------------------------------------------------
# OUTPUTS
# ------------------------------------------------------------------------------
output "github_actions_access_key_id" {
  value     = aws_iam_access_key.github_actions.id
  sensitive = true
}

output "github_actions_secret_access_key" {
  value     = aws_iam_access_key.github_actions.secret
  sensitive = true
}
