FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY api-gateway/pom.xml api-gateway/pom.xml
COPY book-service/pom.xml book-service/pom.xml
COPY adventure-service/pom.xml adventure-service/pom.xml

COPY adventure-service/src adventure-service/src

RUN mvn -pl adventure-service -am clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/adventure-service/target/adventure-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/app.jar"]