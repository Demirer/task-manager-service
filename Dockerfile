# Use Eclipse Temurin 21 JDK
FROM eclipse-temurin:21-jdk

# Install Maven (if not using Maven wrapper)
RUN apt-get update && apt-get install -y maven

WORKDIR /app

# Copy pom.xml first (for dependency caching)
COPY pom.xml ./

# Copy source code
COPY src ./src

# Build the project, we don't skip tests so if test fails it breaks build pipeline same as production system
RUN mvn clean package

# Run the app
CMD ["java", "-jar", "target/task-manager-service-1.0-SNAPSHOT.jar"]