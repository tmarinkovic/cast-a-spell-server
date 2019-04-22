# Cast a spell backend
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
Backend for handling socket connection between host and client components.

## Usecase
Using STOMP protocol and HTTP endpoint, it accepts new connections, creating room for maximum of 2 players and holds
socket connection between them.

For example this components are connecting through this micro service:
 - [cast-a-spell-host][4]
 - cast-a-spell-client[TBD]

It is build using gradle and has support for deploying to AWS.
## Local usage
 ```groovy
 ./gradlew runBoot
 ```
 
 ## AWS usage
 ### Prerequisite
  - [AWS account][2]    
  - [AWS CLI][1]
  - [AWS credentials][3]
 
 Then simply run this 2 commands:
```groovy
./gradlew createResources
```
  
```groovy
./gradlew deploy
```

First command will take some time to finish. You can follow progress in aws console.
It is creating aws role, ecr docker repository and ec2 instance running amazon linux 2.

Deploy command builds docker image, upload it to docker repository and updates stack.
  
  
  
  
  
  
  [1]: https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html
  [2]: https://aws.amazon.com
  [3]: https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html
  [4]: https://github.com/tmarinkovic/cast-a-spell-host