Usage Guide
===========

Web client
----------

Open **http://localhost:8081** in a browser after both server and web client
are running (see :doc:`deployment`).

The home page contains four links:

.. list-table::
   :widths: 40 60
   :header-rows: 1

   * - Link
     - What it does
   * - *Register a user*
     - Calls ``POST /users/add`` — creates or updates a user's password
   * - *View all users*
     - Calls ``GET /users/all`` — lists every registered login
   * - *Post a message*
     - Calls ``POST /users/say`` — stores a message for an authenticated user
   * - *View messages by user*
     - Calls ``GET /messages/all?login=<login>`` — shows all messages by a user

Registering a user
~~~~~~~~~~~~~~~~~~

#. Click **Register a user**.
#. Enter a ``login`` (e.g. ``alice``) and a ``password``.
#. Click **Register**.
#. The page confirms with *Server response: Saved*.

Posting a message
~~~~~~~~~~~~~~~~~

#. Click **Post a message**.
#. Enter the same ``login`` and ``password`` used during registration.
#. Type the message text and click **Send**.
#. The server echoes the message text on success.
   A ``400 Bad Request`` is returned when credentials are wrong.

Viewing messages
~~~~~~~~~~~~~~~~

#. Click **View messages by user**.
#. Enter a ``login`` and click **Search**.
#. All messages stored for that user are listed.

REST API (direct)
-----------------

All endpoints are under **http://localhost:8080**.
For interactive API documentation, open
**http://localhost:8080/swagger-ui/index.html**.

POST /users/add
~~~~~~~~~~~~~~~

Register a new user or update the password of an existing one.

.. code-block:: bash

   curl -s -X POST http://localhost:8080/users/add \
        -H 'Content-Type: application/json' \
        -d '{"login":"alice","password":"s3cr3t"}'

Response: ``Saved``

POST /users/say
~~~~~~~~~~~~~~~

Post a message on behalf of an authenticated user.

.. code-block:: bash

   curl -s -X POST http://localhost:8080/users/say \
        -H 'Content-Type: application/json' \
        -d '{
              "userData":    {"login":"alice","password":"s3cr3t"},
              "messageData": {"message":"Hello from curl"}
            }'

Successful response body:

.. code-block:: json

   {"message":"Hello from curl"}

Returns ``400 Bad Request`` when the login/password pair does not match.

GET /users/all
~~~~~~~~~~~~~~

List all registered users.

.. code-block:: bash

   curl -s http://localhost:8080/users/all

Example response:

.. code-block:: json

   [{"login":"alice","password":"s3cr3t"},{"login":"bob","password":"pass"}]

GET /messages/all
~~~~~~~~~~~~~~~~~

Get all messages for a specific user.

.. code-block:: bash

   curl -s "http://localhost:8080/messages/all?login=alice"

Example response:

.. code-block:: json

   [{"message":"Hello from curl"},{"message":"Second message"}]

CLI client
----------

The bundled ``ExampleClient`` registers the hard-coded user ``dipina``,
posts a test message, and retrieves the user's messages.  To point it at a
non-default server, edit
``client/src/main/java/es/deusto/spq/client/ExampleClient.java`` and change
the ``hostname`` / ``port`` constants before rebuilding.

.. code-block:: bash

   ./gradlew :client:run
