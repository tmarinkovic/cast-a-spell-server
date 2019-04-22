#!/usr/bin/env bash
AWS_PROFILE=$1
PROJECT_NAME=$2


VERSION=`cat "version.properties" | grep "version" | cut -d'=' -f2`
NEXT_VERSION=$((VERSION+1))
echo "version=$NEXT_VERSION" > version.properties

IMAGE_NAME="cast-a-spell-server-docker-repo"
REPO="995746385715.dkr.ecr.eu-west-1.amazonaws.com"

aws ecr batch-delete-image --repository-name "$PROJECT_NAME"-docker-repo --image-ids imageTag=latest --profile "$AWS_PROFILE"
aws ecr batch-delete-image --repository-name "$PROJECT_NAME"-docker-repo --image-ids imageTag="$VERSION" --profile "$AWS_PROFILE"

function cleanup {
    docker rmi "$IMAGE_NAME:$NEXT_VERSION"
    docker rmi "$REPO/$IMAGE_NAME:$NEXT_VERSION"
    docker rmi "$REPO/$IMAGE_NAME:latest"
}
trap "cleanup; exit" INT TERM EXIT

`aws ecr get-login --region eu-west-1 --no-include-email --profile "$AWS_PROFILE"`
docker build -t "$IMAGE_NAME:$NEXT_VERSION" .
docker tag "$IMAGE_NAME:$NEXT_VERSION" "$REPO/$IMAGE_NAME:$NEXT_VERSION"
docker tag "$IMAGE_NAME:$NEXT_VERSION" "$REPO/$IMAGE_NAME:latest"
docker push "$REPO/$IMAGE_NAME"


aws cloudformation update-stack --stack-name "$PROJECT_NAME" --parameters ParameterKey=SourceCodeBucket,ParameterValue="$PROJECT_NAME" ParameterKey=Version,ParameterValue="$NEXT_VERSION" --template-body file://cloudformation.yml  --capabilities CAPABILITY_IAM --region eu-west-1 --profile "$AWS_PROFILE"

