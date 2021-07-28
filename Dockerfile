FROM openjdk:11
MAINTAINER kdzi
COPY build/libs/github-0.0.1-SNAPSHOT.jar github-0.0.1.jar
ENTRYPOINT ["java","-jar","/github-0.0.1.jar"]
