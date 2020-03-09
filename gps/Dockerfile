FROM adoptopenjdk:13-jre-openj9
COPY build/libs/gps.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
