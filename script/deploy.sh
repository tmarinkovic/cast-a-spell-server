#!/usr/bin/env bash

PROJECT_NAME="cast-a-spell-server"
AWS_PROFILE="tmarinkovic"

PUBLIC_DNS=$(aws ec2 describe-instances --filters "Name=instance-type,Values=t2.nano" --profile "$AWS_PROFILE" --region eu-west-1 | grep "PublicDnsName" | head -1)
IFS=': ' read -r -a array <<< "$PUBLIC_DNS"
PUBLIC_DNS=${array[1]}
PUBLIC_DNS=$(sed 's/.\{1\}$//' <<< "$PUBLIC_DNS")
PUBLIC_DNS="${PUBLIC_DNS//\"}"
echo "Public DNS: $PUBLIC_DNS"

echo "Building artifacts..."
./gradlew clean test
./gradlew clean integrationTest
./gradlew clean build -x test

echo "Syncing bucket..."
aws s3 sync build/libs s3://"$PROJECT_NAME" --profile "$AWS_PROFILE"

echo "Deploying..."
ssh -o "StrictHostKeyChecking no" -i /Users/tmarinkovic/.ssh/id_rsa ec2-user@"$PUBLIC_DNS" "sudo chmod u+x stop-service.sh"
ssh -o "StrictHostKeyChecking no" -i /Users/tmarinkovic/.ssh/id_rsa ec2-user@"$PUBLIC_DNS" "sudo ./stop-service.sh"
ssh -o "StrictHostKeyChecking no" -i /Users/tmarinkovic/.ssh/id_rsa ec2-user@"$PUBLIC_DNS" "sudo rm cast-a-spell-server-0.1.0.jar"
ssh -o "StrictHostKeyChecking no" -i /Users/tmarinkovic/.ssh/id_rsa ec2-user@"$PUBLIC_DNS" "sudo rm cast-a-spell-server.log"
ssh -o "StrictHostKeyChecking no" -i /Users/tmarinkovic/.ssh/id_rsa ec2-user@"$PUBLIC_DNS" "sudo aws s3 cp s3://cast-a-spell-server/cast-a-spell-server-0.1.0.jar /home/ec2-user/cast-a-spell-server-0.1.0.jar"
ssh -o "StrictHostKeyChecking no" -i /Users/tmarinkovic/.ssh/id_rsa ec2-user@"$PUBLIC_DNS" "sudo nohup java -jar cast-a-spell-server-0.1.0.jar > cast-a-spell-server.log &"
echo "Done!"
