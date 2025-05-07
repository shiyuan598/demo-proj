FROM eclipse-temurin:23-jdk-jammy

WORKDIR /app

COPY . /app

RUN ./mvnw clean package -DskipTests

EXPOSE 9002

CMD ["java", "-jar", "target/demo-proj-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]
