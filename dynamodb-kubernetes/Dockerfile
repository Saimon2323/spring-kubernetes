#FROM adoptopenjdk/openjdk11:alpine-jre
#ADD target/RestPatient-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

FROM openjdk:11

RUN apt-get update && apt-get install -y awscli

ENV AWS_ACCESS_KEY_ID=fakeMyKeyId
ENV AWS_SECRET_ACCESS_KEY=fakeSecretAccessKey
ENV AWS_DEFAULT_REGION=ap-southeast-1

RUN aws dynamodb --endpoint-url http://192.168.49.2:31001 create-table --table-name customer-sync --attribute-definitions AttributeName=customerId,AttributeType=S --key-schema AttributeName=customerId,KeyType=HASH --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5

EXPOSE 9081
ADD target/dynamodb-kubernetes-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]