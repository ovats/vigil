# Vigil code-challenge

## Notes

Unfortunately the following has not been implemented for lack of time:

- Integration tests.
- Ipload images features.

The project is divided in three parts:

- api: Api Rest, endpoints required
- ep: it was created for processing messages/events using Kafka. It is related to the feature of subscribing to user's posts and sending notifications. 
- common: all common classes, traits, and objects required by `api` and `ep`.

## Requirements

- Java 11 or higher (it may work on Java 8 but I didn't test it)
- Docker

## How to run

In order to run the application, you need to do the following steps in this order:

- Run an instance of Postgres.
- Then run the service.

### Running Postgres

Open a terminal and go to the folder `docker` under the root of the project.
Run the command:

`docker-compose up`

You can run from the command line using `sbt` or using `docker`.
The file `docker-compose.yaml` defines user and password, you will need it if for example you want to use a database client (DBeaver, ...).
If you use a different Postgres (for example already installed in your machine), you will need to update the file `application.conf`.

If you want to start from a fresh instance of Postgres run in the same folder:

`docker-compose down`

and then again:

`docker-compose up`

After the database is running, next step is start the service.
There are two options: using just sbt or using `docker` again.

The file `scripts/sample-data.sh` can be executed to add some data to the database (just a few rows), and also it's easy to add more rows. 
BUT (IMPORTANT): this script must be executed after the database contains the required tables/indexes. 
To create these tables you just need to start the service (Flyway will create tables/indexes), and after that you can run the script. 

### Starting the service using sbt

Open a terminal and run the command:

`sbt api/run`

### Starting the service using with Docker

First we need to start `sbt` from a terminal.
To build a `docker` image of the service run:

`docker:publishLocal`

To check the image has been created run:

`docker images | grep api`

You should see your image just created.

To run the service using this image:

`docker run --rm -p:8080:8080 api`

## Checking the service is running

Open `http://localhost:8080/docs` in a browser. 
You should see all the endpoints of the microservice (and use them from the page!)

## Unit tests

To run unit tests:

`sbt test`

## Environment variables

By default all values defined in `application.conf` would be enough to run the service.
In case of troubleshooting check the environment variables defined in `application.conf`.

## Libraries

- Akka: the server is implemented using Akka.
- Circe: for encoding and decoding
- Slick: for read/write data to Postgres
- Tapir: for decouple endpoints definitions and endpoints implementation.
  Also Tapir builds a nice documentation: everytime you made a change to an endpoint definition, the documentation is automatically updated.
- Flyway: version control for the database. 
- Pure Config: for reading configuration files and mapped them to Scala case classes.
- Scalatest/Mockito: for unit and integration tests.

Others:

- sbt-scalafmt: to keep a nice code style.
- sbt-native-packager: for building `docker images`.
- sbt-buildinfo: to get information about the build from the code. It is used to display version in the UI that displays all endpoints (thanks to Tapir). 

## Enhancements/Improvements

- First of all image capabilities could be added.
- Output of the endpoints (responses) could be improved to bring more useful data. For example when retrieving posts, the response only show the `id` of the user, it doesn't show `name` and/or `email`.
- Only very basic validations are implemented. More validations can be added, for example format of email should be correct.
- For validations the library Cats.validate could be useful. 
- In case of using other library instead of Akka, Tapir will help to make the migration. Endpoints definitions can be reused.
- To improve search capabilities Elastic Search can be used.
- Kafka can be used for implementing asynchronous messages and notifications.