SpringBoot REST MySQL Multimodule — Manual
==========================================

.. toctree::
   :maxdepth: 2
   :caption: Contents

   deployment
   usage
   api

Overview
--------

This project demonstrates a multimodule Spring Boot application backed by
MySQL.  It exposes a JSON REST API (the **server** module) and provides two
client interfaces:

* **client** — command-line Java client (``RestTemplate``)
* **web-client** — browser-based Thymeleaf UI running on port 8081

The **commons** module holds the shared JPA entities and serializable DTOs
used by every other module.

.. code-block:: text

   ┌──────────────────────────────────────────────────────┐
   │                        server (:8080)                │
   │   UserController   MessageController                 │
   │        │                  │                          │
   │        └──── MySQL (messages DB) ────┘               │
   └──────────────────────────────────────────────────────┘
            ▲                          ▲
            │  REST/JSON               │  REST/JSON
   ┌────────┴────────┐       ┌─────────┴────────┐
   │ client (CLI)    │       │ web-client (:8081)│
   └─────────────────┘       └──────────────────┘
