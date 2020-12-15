FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
WORKDIR /opt
ENV PORT 9092
EXPOSE 9092
COPY ${JAR_FILE} api-entry-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","api-entry-0.0.1-SNAPSHOT.jar"]