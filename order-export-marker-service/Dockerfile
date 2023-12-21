FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/*.jar app.jar
ADD target/classes/application.yml application.yml
ENTRYPOINT ["java","-jar","app.jar"]
