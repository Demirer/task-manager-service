FROM maven:3.9.3-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .

RUN mvn dependency:go-offline
COPY src ./src

# To follow best practices, we do not skip tests.
# The CI/CD pipeline should fail if the implementation causes test failures.
RUN mvn clean package

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=build /app/target/task-manager-service-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
