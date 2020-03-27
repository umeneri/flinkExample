provider "aws" {
  region     = "ap-northeast-1"
}

terraform {
  backend "s3" {
    bucket = "emr-flink-cluster"
    key    = "emr-flink-cluster.tfstate"
    region     = "ap-northeast-1"
    profile = "terraform"
  }
}

variable aws_profile { default = "terraform" }
//variable "access_key" {}
//variable "secret_key" {}
variable "region" {
  default = "ap-northeast-1"
}

variable "role" {
  default = "emr-flink"
}

variable "ssh_key_name" {
  default = "terraform"
}

data "aws_availability_zones" "available" {
  state = "available"
}
variable "vpc_cidr" { default = "10.0.0.0/16" }

