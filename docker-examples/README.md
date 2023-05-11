Ejemplo de Maven + Jersey + JDO
===============================


![java ci workflow](https://github.com/unaguil/spq-simple-service/actions/workflows/java-ci.yml/badge.svg) [![codecov](https://codecov.io/gh/unaguil/spq-simple-service/branch/main/graph/badge.svg?token=VWV6C72V4T)](https://codecov.io/gh/unaguil/spq-simple-service)

Configuración
------------- 

**Construcción y prueba**

Se puede construir el proyecto y lanzar las pruebas unitarias con el comando

    mvn test

**Base de datos**

Crear una base de datos llamada *jersey* y dar permisos al usuario por defecto

    CREATE DATABASE jersey;
    CREATE USER IF NOT EXISTS 'spq'@'localhost' IDENTIFIED BY 'spq';
    GRANT ALL ON jersey.* TO 'spq'@'localhost';

La configuración por defecto para la base de datos y los usuarios puede ser actualizada en el fichero *resources/datanucleus.properties*.

Las clases de datos deben ser procesadas antes de generar las tablas con el comando 

    mvn datanucleus:enhance

Para la creación de las tablas se debe ejecutar el comando de maven

    mvn datanucleus:schema-create

**Datos de prueba**

Se pueden introducir datos de prueba en la aplicación utilizando el comando de maven

    mvn -Pdatos exec:java

**Tests de integración y rendimiento**

Introducidos los datos de prueba se pueden lanzar los tests de integración y rendimiento usando el comando

    mvn verify -Pintegracion

**Inicio del servidor**

El servidor REST de la aplicación se lanza utilizando el comando

    mvn exec:java

Si el servidor ha sido iniciado correctamente se pueden obtener los datos de prueba accediendo con el navegador a la URL http://localhost:8080/myapp/users.


**Inicio de la aplicación cliente**

La aplicación cliente puede iniciarse usando el comando

    mvn -Pcliente exec:java