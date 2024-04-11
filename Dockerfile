FROM openjdk:21 as kafadap-consumer
ARG JAR_FILE=./kafka-consumer/target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8083

ENTRYPOINT ["java","-jar","/app.jar"]

FROM openjdk:21 as kafadap-producer
ARG JAR_FILE=./kafka-producer/target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8082

ENTRYPOINT ["java","-jar","/app.jar"]

FROM openjdk:21 as kafadap-rest
ARG JAR_FILE=./rest-api/target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]

FROM openjdk:21 as kafadap-web
ARG JAR_FILE=./web/target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8081

ENTRYPOINT ["java","-jar","/app.jar"]