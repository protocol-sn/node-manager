FROM eclipse-temurin:21-jre
WORKDIR /home/app
COPY --link build/libs/node-manager*-all.jar /home/app/application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dmicronaut.environments=docker", "-jar", "/home/app/application.jar"]
