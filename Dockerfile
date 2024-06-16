FROM maven:3.9.7-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
RUN addgroup -S raiftest && adduser -S raiftest -G raiftest
USER raiftest:raiftest
COPY --from=build /app/target/raif-test-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "raif-test-0.0.1-SNAPSHOT.jar"]
