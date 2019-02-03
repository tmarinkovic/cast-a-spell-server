#!/usr/bin/env bash

PROJECT_NAME="cast-a-spell-server"
AWS_PROFILE="tmarinkovic"

echo "Creating bucket..."
aws s3api create-bucket --bucket "$PROJECT_NAME" --region eu-west-1 --create-bucket-configuration LocationConstraint=eu-west-1 --profile "$AWS_PROFILE"

echo "Building application..."
./gradlew clean test
./gradlew clean integrationTest
./gradlew clean build -x test

echo "Uploading to S3..."
aws s3 cp build/libs/"$PROJECT_NAME"-0.1.0.jar s3://"$PROJECT_NAME"/"$PROJECT_NAME"-0.1.0.jar --profile "$AWS_PROFILE"
aws s3 cp cloudformation.template s3://"$PROJECT_NAME"/cloudformation.template --region eu-west-1 --profile "$AWS_PROFILE"
aws s3 cp script/stop-service.sh s3://"$PROJECT_NAME"/stop-service.sh --region eu-west-1 --profile "$AWS_PROFILE"
echo "Creating stack on aws..."
aws cloudformation create-stack --stack-name "$PROJECT_NAME" --parameters ParameterKey=SourceCodeBucket,ParameterValue="$PROJECT_NAME" --template-url https://s3.amazonaws.com/"$PROJECT_NAME"/cloudformation.template --capabilities CAPABILITY_IAM --region eu-west-1 --profile "$AWS_PROFILE"
echo "Done!"

PUBLIC_IP=$(aws ec2 describe-instances --filters "Name=instance-type,Values=t2.nano" --profile "$AWS_PROFILE" --region eu-west-1 | grep "PublicIpAddress" | head -1)
IFS=': ' read -r -a array <<< "$PUBLIC_IP"
PUBLIC_IP=${array[1]}
PUBLIC_IP=$(sed 's/.\{1\}$//' <<< "$PUBLIC_IP")
PUBLIC_IP="${PUBLIC_IP//\"}"
echo "Public IP: $PUBLIC_IP"












