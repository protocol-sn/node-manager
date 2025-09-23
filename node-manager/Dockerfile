FROM eclipse-temurin:21-jre
WORKDIR /home/app
ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,address=*:5006,server=y,suspend=n"
COPY --link build/libs/node-manager*-all.jar /home/app/application.jar
EXPOSE 8080
EXPOSE 5006
ENTRYPOINT ["java", "-Dmicronaut.environments=docker", "-jar", "/home/app/application.jar"]
