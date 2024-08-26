FROM openjdk:latest
#VOLUME /tmp
#ARG JAR_FILE
#WORKDIR /app/
#COPY target/${JAR_FILE} /app/spring-boot.jar
COPY target/lecture-planner-lecture-service-1.0-SNAPSHOT.jar /app/spring-boot.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar","/app/spring-boot.jar"]
