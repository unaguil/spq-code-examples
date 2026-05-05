Maven site & Doxygen basic example
==================================

This project contains a basic example of Maven site information generation and doxygen basic usage

Maven site generation can be launched with

    mvn site

After process is finished go to **target/site** folder and open the main **index.hmtl** file to see the reports.

To generate the Doxygen document, first you need to download and correctly configure the tool to be available in your PATH.

The output directory must be created previously, so run the following command to create the **target/doxygen** directory.

    mvn compile

Change to the **src/main/resources** directory and run the doxygen command

    cd src/main/resources
    doxygen Doxyfile