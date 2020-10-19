FROM openjdk:14.0.1 AS builder

ADD . /source

WORKDIR /source

RUN ./gradlew build

FROM openjdk:14.0.1-slim-buster

RUN adduser --disabled-password --gecos '' spring

USER spring

ARG JAR_FILE=build/libs/*all.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]