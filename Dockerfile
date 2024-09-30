#
FROM openjdk:8
ADD target/backend-0.0.1-SNAPSHOT.jar backend.jar
EXPOSE 9001
ENTRYPOINT ["java", "-jar","backend.jar"]
# Build stage
#FROM eclipse-temurin:17-jdk-alpine AS builder
#
#WORKDIR /app
#COPY . .
#RUN #./mvnw package
#
## Run stage
#FROM eclipse-temurin:17-jdk-alpine AS runner
#
#WORKDIR /app
#COPY --from=builder target/backend-0.0.1-SNAPSHOT.jar backend.jar
#
#CMD ["java", "-jar", "backend.jar"]