FROM openjdk:latest
#VOLUME /tmp
#ARG JAR_FILE
#WORKDIR /app/
#COPY target/${JAR_FILE} /app/spring-boot.jar
#ENV EMPLOYEE_HOST=lecture-planner-employee

COPY target/lecture-planner-frontend-1.0-SNAPSHOT.jar /app/spring-boot.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar","/app/spring-boot.jar"]
