# Commands to test Docker

## Testing Docker
0. Create a Dockerfile
```yaml
# Specifies the base image
FROM maven:3.8.1-openjdk-11
# Copy the source code to the image
COPY . /spq-simple-service
# Execute the maven build
RUN cd spq-simple-service && mvn compile datanucleus:enhance
# change to this directory
WORKDIR spq-simple-service
# Specify which command will be launched when starting the container
CMD ["mvn", "exec:java"]
```

1. Build an image: 
````
	docker build . -t simple-jersey
````

2. Run a container based on the above generated image: 
````
	docker run -p 9080:8080 simple-jersey
````

3. Access the server by going to a browser: http://localhost:9080/myapp/myresource

4. You can launch the container in detached mode: 
````
	docker run -d -p 9080:8080 --name jersey-server simple-jersey
````

5. You can get a shell to that server by typing: 
````
	docker exec -it jersey-server /bin/bash
````

6. Print logs: 
````
	docker logs jersey-server
````

## Testing Docker-Compose

7. Create a MySQL container: 
````
	docker run --rm --name database -e MYSQL_ROOT_PASSWORD=root -d mysql:8.0.25
````

8. Create script to insert data in the DB: 
````
	docker exec -i database mysql -uroot -proot < src/main/resources/database.sql
````

9. Stop them since we need to connect these containers through a common network:
````
	docker stop jersey-server
	docker stop database
````

10. Create a new network: 
````
	docker network create my-network
````

11. Modify datanucles.properties to point to the name of the container, which acts as the host/node name:
```java
	javax.jdo.option.ConnectionURL=jdbc:mysql://database/jersey?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
```

12. Modify the Main class of the Grizzly server to accept connections from anywhere:
```java
	public static final String BASE_URI = "http://0.0.0.0:8080/myapp/";
```

13. Configure a user to be able to access from remote servers to the database:
```sql
	CREATE DATABASE jersey;
	CREATE USER IF NOT EXISTS 'spq'@'localhost' IDENTIFIED BY 'spq';
	GRANT ALL ON jersey.* TO 'spq'@'localhost';
	CREATE USER IF NOT EXISTS 'spq'@'%' IDENTIFIED BY 'spq';
	GRANT ALL ON jersey.* TO 'spq'@'%';
```

14. Run the two containers connected to the same network:
````
docker run --rm --name database --network my-network -e MYSQL_ROOT_PASSWORD=root -d mysql:8.0.25
docker exec -i database mysql -uroot -proot < src/main/resources/database.sql

docker run --rm -d -p 9080:8080 --network my-network --name jersey-server simple-jersey
docker exec jersey-server mvn datanucleus:schema-create exec:java -Pdatos
````

15. Run all together thanks to Docker-compose
```yaml
version: '3.3'

volumes:
    db_data:

services:
    database:
        image: mysql:8.0.25
        volumes:
        - db_data:/var/lib/mysql
        restart: always
        environment:
            MYSQL_ROOT_PASSWORD: root
            MYSQL_DATABASE: jersey
            MYSQL_USER: spq
            MYSQL_PASSWORD: spq
    server:
        depends_on:
        - database
        build: .
        ports:
        - "9080:8080"
        restart: always
```

16. Run the docker-compose
```bash
	docker container stop database
	docker container stop jersey-server
	docker container ls
	docker compose build
	docker compose up -d
	docker compose exec server mvn datanucleus:schema-create exec:java -Pdatos
	docker exec -it server /bin/bash
```

17. Test the service deployed with Docker-compose which accesses to a database in MySQL: http://localhost:9080/myapp/users

