FROM maven:3.9.4-amazoncorretto-21-debian-bookworm AS MAVEN_BUILD
COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
COPY ./src ./src
COPY ./uploads/photos ./uploads/photos
RUN mvn package

FROM openjdk:21-slim-bookworm
EXPOSE 8080
COPY --from=MAVEN_BUILD /target/mojaryba-*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]