FROM maven:3.9.4-amazoncorretto-21-debian-bookworm AS maven_build
COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
COPY ./src ./src
COPY ./uploads/photos ./uploads/photos
RUN mvn package

FROM eclipse-temurin:21-jdk-jammy
EXPOSE 8080
COPY --from=maven_build /target/mojaryba-*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]