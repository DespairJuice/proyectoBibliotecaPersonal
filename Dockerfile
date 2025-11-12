# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml ./
COPY .mvn .mvn
COPY src ./src
RUN mvn -B -e -ntp clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENV PORT=8060
EXPOSE 8060
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
