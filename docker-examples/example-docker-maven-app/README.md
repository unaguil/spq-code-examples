Maven app containerization using Docker
=======================================

This example shows how to containerize the following maven application.

Maven application documentation
-------------------------------

Run the following command to compile all classes and launch the unit tests:

      mvn test

Make sure that the database was correctly configured. Use the contents of the file *create-message.sql* to create the database and grant privileges. For example,

      mysql –uroot -p < sql/create-messages.sql

In this example, the class enhancement required by DataNucleus must be manually executed after the unit testing is performed.
This is required to avoid cluttering the JaCoCo report with all the methods generated automatically by DataNucleus.

Therefore, execute the following command to enhance the database classes

      mvn datanucleus:enhance

Run the following command to create database schema for this sample.

      mvn datanucleus:schema-create

Integration tests can be launched using the following command. An embedded Grizzly HTTP server will be launched to perform real calls
to the REST API and to the MySQL database.

      mvn verify -Pintegration-tests

Performance tests can be launched using the following command. In this example, these tests are the same integration tests but executed
multiple times to calculate some statistics

      mvn verify -Pperformance-tests

To launch the server run the command

      mvn jetty:run

You can check that the server is correctly by opening the following URL http://localhost:8080/rest/resource/hello

Now, the client sample code can be executed in a new command window with
      
      mvn exec:java -Pclient


Containerization of the application using Docker
-----------------------------------------------------

Create a file name Dockerfile in your project root folder

```yaml
# Specifies the base image
FROM maven:3.9.6-eclipse-temurin-21
# Copies the current directory contents to the new Docker image
# inside the spq-simple-service folder
COPY . /spq-simple-service
# Executes the maven build
RUN cd spq-simple-service && mvn compile datanucleus:enhance
# change to this directory
WORKDIR spq-simple-service
# Specify which command will be launched when starting the container
CMD ["mvn", "jetty:run"]
```

Use docker to build the new image which will be tagged with the `simple-jersey` identifier.
```
docker build . -t simple-jersey
```

List the new image by running
```
docker image ls
```

Run a container from the previoulsy generated image and map the application internal port 8080 to your host 9080 port
and in detached mode
```
docker run -d -p 9080:8080 simple-jersey
```

Check that the server is currenyl running by opening the following URL in your browser: http://localhost:9080/rest/resource/hello

However, the server is failing because it cannot connect to the required MySQL database.

You can see the logs written by the application running the following command where CONTAINER-NAME is the id of your container
```
docker logs CONTAINER-NAME
```

Get the name of your container using the following command and obtaine the logs
```
docker ps -a
```

Creating the Database container
-------------------------------

Run a MySQL container by instantiating an existing image from docker hub and assing the name `database` to the container
```
docker run --rm --name database -e MYSQL_ROOT_PASSWORD=root -d mysql:8.4.0
```

Execute the following command into the previously running container script to execute the SQL script and create the database schema 
```
docker exec -i database mysql -uroot -proot < sql/create-messages.sql
```

Connect to the MySQL container to check issue MySQL commands and check database was correctly created ()
```
docker exec -it database /bin/bash
```
then run the following command inside the container to connect to the database with the MySQL client
```
bash-5.1# mysql -uroot -proot
``` 
and show the databases
```
mysql> show databases;
```

Connecting the containers
-------------------------
Make sure that the application container and database containers are not running, because we need to connect them with a network to allow communication. If the containers are running stop them with the following commands
```
docker stop CONTAINER-NAME
docker stop database
```

Create a new network to communicate the two containers
```
docker network create my-network
```

Check your datanucleus.properties to be sure that the JDO connection is pointing to the name of the `database` container, which acts as the host/node name
```java
	javax.jdo.option.ConnectionURL=jdbc:mysql://database/messagesDB
```

Run the two containers connected to the same network and provide a name to the application container to ease future reference.

First, run again an instance of the `database` container
```
docker run --rm --name database --network my-network -e MYSQL_ROOT_PASSWORD=root -d mysql:8.4.0
```

and don't forget to execute again the SQL script to create the database schema, as this is a new instance and data is not persisted among instances (by default)

```
docker exec -i database mysql -uroot -proot < sql/create-messages.sql
```

Then, run the application container again
```
docker run --rm -d -p 9080:8080 --network my-network --name jersey-server simple-jersey
```

and don't forget to execute the Datanucleus schema creation from the application container
```
docker exec jersey-server mvn datanucleus:schema-create
```

Now, the connection between the two containers is established and the application should connect correctly to the database container (http://localhost:9080/rest/resource/hello)

Stop the containers and remove then before continuing
```
docker stop jersey-server
docker rm jersey-server
docker stop database
docker rm database
```

Automating with Docker Compose
------------------------------

Docker Compose is a tool that eases the orchestration of multiple containers by automating all the previous steps.

The following file defines and application composed of two containers: a Jersey application and a database. In addition, it configures the usage of persistent volumes to store the database data between executions of the containers.

```yaml
volumes:
    db_data:

services:
    database:
        image: mysql:8.4.0
        volumes:
        - db_data:/var/lib/mysql
        restart: always
        environment:
            MYSQL_ROOT_PASSWORD: root
            MYSQL_DATABASE: messagesDB
            MYSQL_USER: spq
            MYSQL_PASSWORD: spq
    server:
        image: simple-jersey
        depends_on:
        - database
        ports:
        - "9080:8080"
        restart: always
```

Run the Docker compose orchestration and check that the server is correctly working by opening the URL http://localhost:9080/rest/resource/hello.

```
docker compose up -d
docker compose exec server mvn datanucleus:schema-create
```

Applications runned by Docker Compose can be stoped with
```
docker compose down
```