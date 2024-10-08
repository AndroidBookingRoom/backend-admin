# Base image được sử dụng để build image
FROM --platform=amd64 openjdk:17.0.2-oraclelinux8

# Thông tin tác giả
LABEL authors="nhanns"

# Set working directory trong container
#WORKDIR /app

# Copy file JAR được build từ ứng dụng Spring Boot vào working directory trong container
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE}  application.jar

# Expose port của ứng dụng
EXPOSE 8080

# Chỉ định command để chạy ứng dụng khi container khởi chạy
CMD ["java", "-jar", "application.jar"]
#ADD target/backend-0.0.1-SNAPSHOT.jar backend.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar","backend.jar"]
