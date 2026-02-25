FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

FROM eclipse-temurin:17-jre
WORKDIR /app
ARG JAR_FILE=turnstile-1.0.0.jar
COPY --from=build /app/target/${JAR_FILE} app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
