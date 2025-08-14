FROM openjdk:17-jre-slim

WORKDIR /usr/app
COPY bookstore/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
