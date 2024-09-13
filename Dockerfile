# Sử dụng image Maven chính thức để build ứng dụng
FROM maven:3.9.4-eclipse-temurin-21 as build

# Set thư mục làm việc
WORKDIR /app

# Copy toàn bộ mã nguồn vào container
COPY . .

# Build ứng dụng Spring Boot
RUN mvn clean package -DskipTests

# Sử dụng JRE để chạy ứng dụng sau khi build
FROM eclipse-temurin:21-jre-alpine

# Set thư mục làm việc
WORKDIR /app

# Copy file JAR từ step trước
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]