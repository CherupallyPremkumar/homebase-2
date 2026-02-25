variable "aws_region" {
  description = "The AWS region things are created in"
  default     = "us-east-1"
}

variable "environment" {
  description = "The environment classification"
  default     = "prod"
}

variable "ecr_repository_name" {
  description = "Name for the Amazon Elastic Container Registry (ECR)"
  default     = "homemade-app"
}

variable "vpc_cidr" {
  description = "CIDR block for the VPC"
  default     = "10.0.0.0/16"
}

variable "app_port" {
  description = "Port exposed by the docker image to route traffic to"
  default     = 8080
}

variable "app_count" {
  description = "Number of docker containers to run"
  default     = 1
}

variable "fargate_cpu" {
  description = "Fargate instance CPU units to provision (1 vCPU = 1024)"
  default     = 512
}

variable "fargate_memory" {
  description = "Fargate instance memory to provision (in MiB)"
  default     = 1024
}

variable "db_name" {
  description = "The name of the database to create"
  default     = "postgres"
}

variable "db_username" {
  description = "The database username"
  default     = "postgres"
}

variable "db_password" {
  description = "The database password"
  sensitive   = true
}

variable "db_instance_class" {
  description = "The RDS instance class"
  default     = "db.t3.micro"
}

variable "certificate_arn" {
  description = "The ARN of the ACM certificate for the ALB"
  type        = string
  default     = "" # Should be provided via terraform.tfvars or env var
}
