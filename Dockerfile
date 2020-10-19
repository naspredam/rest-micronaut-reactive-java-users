FROM openjdk:14.0.1 AS builder

ADD . /source

WORKDIR /source

RUN rm -rf build out .idea .gradle
RUN ./gradlew clean build

FROM openjdk:14.0.1-slim-buster

RUN adduser --disabled-password --gecos '' micronaut

ARG JAR_FILE=/source/build/libs/*all.jar
COPY --chown=micronaut --from=builder ${JAR_FILE} /home/micronaut/app.jar

WORKDIR /home/micronaut
USER micronaut

ENTRYPOINT ["java","-jar","/app.jar"]