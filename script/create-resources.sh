#!/usr/bin/env bash
AWS_PROFILE=$1
PROJECT_NAME=$2
VERSION=`cat "version.properties" | grep "version" | cut -d'=' -f2`

aws cloudformation create-stack --stack-name "$PROJECT_NAME" --parameters ParameterKey=SourceCodeBucket,ParameterValue="$PROJECT_NAME" ParameterKey=Version,ParameterValue="$VERSION" --template-body file://cloudformation.yml  --capabilities CAPABILITY_IAM --region eu-west-1 --profile "$AWS_PROFILE"
