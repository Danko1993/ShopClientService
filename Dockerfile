FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

COPY ./pom.xml ./
COPY ./src ./src
COPY ./mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests


FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/target/clientService-0.0.1-SNAPSHOT.jar /app/clientService.jar


EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/clientService.jar"]
