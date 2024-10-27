FROM openjdk:19
WORKDIR /app
COPY ./target/TestForIntern-0.0.1-SNAPSHOT.jar app/target/TestForIntern-0.0.1-SNAPSHOT.jar
CMD ["mvn", "clean", "build"]
ENTRYPOINT ["java", "-jar", "app/target/TestForIntern-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080