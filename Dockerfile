FROM bellsoft/liberica-openjdk-alpine:latest
ADD target/poseidon-app-0.0.1-SNAPSHOT.jar poseidon-app-0.0.1-SNAPSHOT.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","poseidon-app-0.0.1-SNAPSHOT.jar"]