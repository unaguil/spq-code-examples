API Reference
=============

The REST API is fully documented by Doxygen (see :ref:`generating-doxygen`).
This page gives a concise summary of every endpoint and the DTOs exchanged.

Endpoints
---------

.. list-table::
   :widths: 15 40 25 20
   :header-rows: 1

   * - Method
     - Path
     - Request body
     - Response body
   * - ``POST``
     - ``/users/add``
     - ``UserData``
     - ``"Saved"`` (plain text)
   * - ``POST``
     - ``/users/say``
     - ``DirectMessage``
     - ``MessageData`` or ``400``
   * - ``GET``
     - ``/users/all``
     - —
     - ``UserData[]``
   * - ``GET``
     - ``/messages/all?login=<login>``
     - —
     - ``MessageData[]``

Data Transfer Objects
---------------------

UserData
~~~~~~~~

.. code-block:: json

   {
     "login":    "string",
     "password": "string"
   }

MessageData
~~~~~~~~~~~

.. code-block:: json

   {
     "message": "string"
   }

DirectMessage
~~~~~~~~~~~~~

.. code-block:: json

   {
     "userData":    { "login": "string", "password": "string" },
     "messageData": { "message": "string" }
   }

.. _generating-doxygen:

Generating API docs (Doxygen)
-----------------------------

.. code-block:: bash

   ./gradlew doxygen

HTML output: ``build/reports/doxygen/html/index.html``
