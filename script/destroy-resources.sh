#!/usr/bin/env bash
AWS_PROFILE=$1
PROJECT_NAME=$2

echo "version=0" > version.properties

aws ecr delete-repository --force --repository-name "$PROJECT_NAME"-docker-repo --profile "$AWS_PROFILE"
aws cloudformation delete-stack --stack-name "$PROJECT_NAME" --region eu-west-1 --profile "$AWS_PROFILE"