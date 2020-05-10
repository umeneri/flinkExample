## S3 Bucket for ERM Logs
resource "aws_s3_bucket" "emr-log" {
  bucket = "emr-flink-logs"
  acl = "private"
  force_destroy = true
}

## VPC
resource "aws_vpc" "emr-test" {
  cidr_block = "${var.vpc_cidr}"
  instance_tenancy = "default"
  enable_dns_support = true
  enable_dns_hostnames = true

  tags = {
    Name = "${var.role}-vpc"
  }
}

## Internet GW
resource "aws_internet_gateway" "emr-test" {
  vpc_id = "${aws_vpc.emr-test.id}"

  tags = {
    Name = "${var.role}-igw"
  }
}

## Subnet
resource "aws_subnet" "public" {
  count = 2
  vpc_id = "${aws_vpc.emr-test.id}"
  cidr_block = "${cidrsubnet(var.vpc_cidr, 8, count.index)}"
  availability_zone = "${data.aws_availability_zones.available.names[count.index]}"
  map_public_ip_on_launch = true

  tags = {
    Name = "${format("${var.role}-public-subnet%02d", count.index + 1)}"
  }
}

## Route Table
resource "aws_route_table" "public" {
  vpc_id = "${aws_vpc.emr-test.id}"

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = "${aws_internet_gateway.emr-test.id}"
  }

  tags = {
    Name = "${var.role}-public"
  }
}

resource "aws_route_table_association" "public" {
  count = 2
  subnet_id = "${element(aws_subnet.public.*.id, count.index)}"
  route_table_id = "${aws_route_table.public.id}"
}

## IAM role for EMR
data "aws_iam_policy_document" "emr-assume-role-policy" {
  statement {
    effect = "Allow"
    actions = [
      "sts:AssumeRole"]

    principals {
      type = "Service"
      identifiers = [
        "elasticmapreduce.amazonaws.com"]
    }
  }
}

resource "aws_iam_role" "emr-service-role" {
  name = "EMR_DefaultRole"
  assume_role_policy = "${data.aws_iam_policy_document.emr-assume-role-policy.json}"
}

resource "aws_iam_role_policy_attachment" "emr-service-role" {
  role = "${aws_iam_role.emr-service-role.name}"
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonElasticMapReduceRole"
}

## IAM Role for EC2
data "aws_iam_policy_document" "ec2-assume-role-policy" {
  statement {
    effect = "Allow"
    actions = [
      "sts:AssumeRole"]

    principals {
      type = "Service"
      identifiers = [
        "ec2.amazonaws.com"]
    }
  }
}

resource "aws_iam_role" "emr-ec2-role" {
  name = "EMR_EC2_DefaultRole"
  assume_role_policy = "${data.aws_iam_policy_document.ec2-assume-role-policy.json}"
}

resource "aws_iam_role_policy_attachment" "emr-ec2-role" {
  role = "${aws_iam_role.emr-ec2-role.name}"
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonElasticMapReduceforEC2Role"
}

resource "aws_iam_instance_profile" "emr-ec2-profile" {
  name = "${var.role}-emr-ec2-profile"
  role = "${aws_iam_role.emr-ec2-role.name}"
}

## EMR Cluster
resource "aws_emr_cluster" "emr-test-cluster" {
  name = "emr-test-cluster"
  release_label = "emr-5.29.0"
  applications = [
    "Hadoop",
    "Flink"
  ]
  log_uri = "s3://${aws_s3_bucket.emr-log.id}/elasticmapreduce/"
  service_role = "${aws_iam_role.emr-service-role.arn}"
  master_instance_group {
    instance_type = "m1.medium"
  }

  core_instance_group {
    instance_type = "m1.medium"
    instance_count = 1
    bid_price = "0.30"
  }

  ec2_attributes {
    key_name = "${var.ssh_key_name}"
    subnet_id = "${aws_subnet.public.0.id}"
    instance_profile = "${aws_iam_instance_profile.emr-ec2-profile.name}"
  }

  configurations_json = <<EOF
[
    {
      "Classification": "flink-conf",
      "Properties": {
        "taskmanager.numberOfTaskSlots":"2"
      }
    }
]
EOF
}