#!/usr/bin/env bash

PROJECT_NAME="cast-a-spell-server"
AWS_PROFILE="tmarinkovic"

echo "Deleting AWS resources..."
aws s3 rb s3://"$PROJECT_NAME" --region eu-west-1 --profile "$AWS_PROFILE" --force
aws cloudformation delete-stack --stack-name "$PROJECT_NAME" --region eu-west-1 --profile "$AWS_PROFILE"
echo "Done!"
