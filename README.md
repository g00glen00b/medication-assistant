# Mediminder

This application is basically developed for fun and contains a Spring Boot backend with an Angular frontend.
The goal of the application is to make it easier to manage the medication you have in your cabinet and at which times you have to take them.

Existing apps usually try to solve one part of the problem (either the scheduling or the inventory management) but not both.
This application does by automatically updating the medication inventory as soon as you complete a medication intake event.

## Setting up

To start the application there are two prerequisites:

- Docker must be installed
- JDK 17.x must be installed

To run the application, do the following:

```shell
docker-compose up # Runs the database
./mvnw package # This installs both the Maven and npm dependencies
java -jar application/target/mediminder-application-*.jar
```

If you want to run the application in development mode, it's recommended to install the following:

- Maven 3.x
- Node.js >16.x (LTS or later)

After that you can run the backend using:

```shell
cd application
mvn spring-boot:run
```

And you can run the frontend using:

```shell
cd frontend
npm run start
```