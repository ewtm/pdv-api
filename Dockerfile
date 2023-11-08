FROM openjdk
FROM maven
WORKDIR /app
COPY . .

COPY ./target/pdv-api-0.0.1-SNAPSHOT.jar pdv-api-0.0.1-SNAPSHOT.jar

EXPOSE 8080


ENTRYPOINT [ "java", "-jar", "pdv-api-0.0.1-SNAPSHOT.jar" ]