FROM maven:3.9.6-eclipse-temurin-8 AS build
WORKDIR /workspace
COPY pom.xml .
RUN mvn -B -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -B -DskipTests package

FROM tomcat:8.5-jdk8-temurin
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /workspace/target/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
