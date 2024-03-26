
FROM maven:3.8.7-openjdk-18-slim AS build

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests=true

FROM ubuntu:22.04

RUN apt-get update && \
    apt-get install -y openjdk-18-jdk sudo && \
    apt-get clean;

COPY --from=build /app/target/elephant.jar .

COPY db.config .

EXPOSE 7000

CMD ["java", "-jar", "elephant.jar", "db.config"]





