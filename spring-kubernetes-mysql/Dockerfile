#FROM adoptopenjdk/openjdk11:alpine-jre
#ADD target/RestPatient-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

FROM openjdk:11
EXPOSE 8080
ADD target/RestPatient-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]