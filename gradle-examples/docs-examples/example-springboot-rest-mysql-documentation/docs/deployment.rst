Deployment Guide
================

Prerequisites
-------------

* **Java 21** (Gradle toolchain resolves it automatically)
* **MySQL 8** running locally or accessible remotely
* **Gradle wrapper** — no separate Gradle installation needed
* *(Optional)* `Doxygen <https://www.doxygen.nl>`_ for API reference
* *(Optional)* `Sphinx <https://www.sphinx-doc.org>`_ for this manual

Database setup
--------------

Create the schema and grant the default ``spq`` user:

.. code-block:: bash

   mysql -uroot -p < server/sql/create-messages.sql

The script creates:

* database ``messages``
* table ``user (login PK, password)``
* table ``message (id, user_id FK, text, timestamp)``

Default connection properties (``server/src/main/resources/application.properties``):

.. list-table::
   :widths: 30 20 50
   :header-rows: 1

   * - Environment variable
     - Default
     - Description
   * - ``DB_HOST``
     - ``localhost``
     - MySQL server hostname
   * - ``DB_PORT``
     - ``3306``
     - MySQL server port
   * - ``DB_USER``
     - ``spq``
     - Database user
   * - ``DB_PASS``
     - ``spq``
     - Database password

Override any of them at launch time:

.. code-block:: bash

   DB_HOST=192.168.1.10 DB_USER=myuser DB_PASS=mypass ./gradlew :server:bootRun

Build
-----

Compile and test all modules from the project root:

.. code-block:: bash

   ./gradlew build

Running the server
------------------

.. code-block:: bash

   ./gradlew :server:bootRun

The server listens on **http://localhost:8080** by default.

Running the web client
----------------------

The server must already be running before starting the web client.

.. code-block:: bash

   ./gradlew :web-client:bootRun

The web client listens on **http://localhost:8081**.
The back-end URL can be overridden:

.. code-block:: bash

   ./gradlew :web-client:bootRun --args='--server.api.base-url=http://other-host:8080'

Running the CLI client
----------------------

.. code-block:: bash

   ./gradlew :client:run

Alternatively, use the generated start scripts:

.. code-block:: bash

   client/build/scripts/client

Running all services together
------------------------------

.. code-block:: bash

   # start server in background
   ./gradlew :server:bootRun &

   # start web client in background
   ./gradlew :web-client:bootRun &

   # (optional) run CLI client
   ./gradlew :client:run

Stopping
--------

Press ``Ctrl-C`` in each terminal, or send ``SIGTERM`` to the background
processes.
