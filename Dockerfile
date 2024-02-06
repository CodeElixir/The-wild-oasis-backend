FROM maven:3.9.6-amazoncorretto-17  AS build
COPY . .
RUN mvn clean install -DskipTests

FROM amazoncorretto:17.0.10
EXPOSE 8080
COPY --from=build /target/The-wild-oasis-0.0.1-SNAPSHOT.jar The-wild-oasis-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/The-wild-oasis-0.0.1-SNAPSHOT.jar"]