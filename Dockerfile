FROM eclipse-temurin:23-jdk

WORKDIR /app

COPY gradlew .
COPY gradle gradle

RUN chmod +x gradlew
COPY . .

RUN ./gradlew clean bootJar --no-daemon -x test

ENTRYPOINT ["java", "-jar", "build/libs/Shelter-1.0.0.jar"]
