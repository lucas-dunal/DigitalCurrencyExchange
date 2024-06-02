#
# Build stage
#
FROM eclipse-temurin:21-jdk AS build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN ./mvnw -f $HOME/pom.xml clean package
## in CICD consider mounting maven cache to speed up pulling the dependencies as this takes most of the build time
## RUN --mount=type=cache,target=/root/.m2 ./mvnw -f $HOME/pom.xml clean package


#
# Package stage
#
FROM eclipse-temurin:21-jre
ARG JAR_FILE=/usr/app/target/*.jar
COPY --from=build $JAR_FILE /app/runner.jar
EXPOSE 8080
ENTRYPOINT java -jar /app/runner.jar