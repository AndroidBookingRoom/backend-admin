# Build stage
FROM eclipse-temurin:17-jdk-alpine AS builder
ADD /target/*.jar backend.jar
EXPOSE 9001
ENTRYPOINT ["java", "-jar","backend.jar"]

#COPY . .
##RUN #mvn clean package -DskipTests
#
## Run stage
#FROM eclipse-temurin:17-jdk-alpine AS runner
#COPY --from=builder /target/*.jar backend.jar
#
#CMD ["java", "-jar", "backend.jar"]