#1) BUILD STAGE
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Dependency cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Clean build
COPY src ./src
RUN mvn clean package -DskipTests -B

# 2) RUNTIME STAGE
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create system user (without shell, more secure)
RUN addgroup -S spring && adduser -S spring -G spring

# ARG with the path relative to the builder's WORKDIR
ARG JAR_FILE=target/*.jar

# Crear carpeta de logs con permisos correctos 
RUN mkdir -p /app/logs && chown -R spring:spring /app/logs
# COPY WITH PERMISSIONS (Crucial for it to work with USER spring)
# You no longer need the asterisk, you're guaranteed to get it right
COPY --from=builder --chown=spring:spring /app/target/app.jar app.jar

# Switch to the non-root user after copying
USER spring:spring

# Standard variable that the JVM reads automatically
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]