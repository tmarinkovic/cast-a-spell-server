#!/usr/bin/env bash
aws s3 cp s3://cast-a-spell-server/cast-a-spell-server-0.1.0.jar /home/ec2-user/cast-a-spell-server-0.1.0.jar
aws s3 cp s3://cast-a-spell-server/stop-service.sh /home/ec2-user/stop-service.sh
sudo yum -y install java-1.8.0
sudo yum -y remove java-1.7.0-openjdk
cd /home/ec2-user/
sudo nohup java -jar cast-a-spell-server-0.1.0.jar > cast-a-spell-server.log