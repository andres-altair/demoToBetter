# --- STAGE 1: BUILDER ---
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# --- STAGE 2: DEVELOPMENT (Nueva) ---
FROM builder AS development
CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.profiles=dev"]

# --- STAGE 3: RUNTIME (Prod) ---
FROM eclipse-temurin:21-jre-alpine AS production
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
COPY --from=builder --chown=spring:spring /app/target/*.jar app.jar
COPY --chown=spring:spring entrypoint.sh /app/entrypoint.sh 
RUN chmod +x /app/entrypoint.sh
USER spring:spring
ENTRYPOINT ["sh" , "/app/entrypoint.sh"]


