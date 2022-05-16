POM FOR A RESTful EXAMPLE
-------------------------

First, the whole code can be compile using:

    mvn compile

Then, in three separate cmd windows, run the following commands:

    mvn jetty:run 
    mvn exec:java -Pmanager
    mvn exec:java -Pclient