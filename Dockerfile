
# Build the application using Maven and Java 21
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY src src

RUN chmod +x mvnw && ./mvnw -DskipTests package

# Run the packaged application with a Java 21 runtime
FROM eclipse-temurin:21-jre

WORKDIR /app

RUN useradd --system --no-create-home appuser
COPY --from=build /app/target/*.jar app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
