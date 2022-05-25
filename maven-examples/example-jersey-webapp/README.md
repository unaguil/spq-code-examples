Simple Jersey Application With Maven
====================================

The project contains the server and client example codes.
First, the server and client can be compiled using

    mvn compile

To launch the server run the command

    mvn jetty:run

Now, the client sample code can be executed in a new command window with

    mvn exec:java -Pclient