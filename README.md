# Simple Spring Boot Microservice Demo
Example is an adapted version of https://github.com/Mitschi/lecture-planner. Many thanks for that.

A simple frontend (using thymeleaf) is provided that accesses (via REST) two services to add teachers and lectures. The frontend and each service runs in its own container. 

For installing and running the demo in docker:
1. clone the project
2. ```mvn package``` to build each service
3. ```docker compose build``` to create the images
4. ```docker compose up --force-recreate``` to build, (re)create, and start the containers for a service.
5. ```docker compose down -rmi all -v``` to remove containers, all dependent images and volumes

For running the services within your IDE (e.g., IntelliJ):
1. clone the project
2. open the project within your IDE
3. run an instance of mariadb providing an db_example database, e.g. run the db in a container using the following command:<br/>
```docker run --name mariadb -d --rm -e MYSQL_ROOT_PASSWORD=mypass -e MYSQL_DATABASE=db_example -p 3306:3306 mariadb```
4. run the App.class of each component 
