# Spring Boot Sample Amqp

(Extracted from [Spring Boot repository](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples))

This demo project is used to illustrate a pattern we are using in an
internal application that uses Event Sourcing and CQRS.

## Getting started

You'll need to install [Docker Toolbox](https://www.docker.com/products/docker-toolbox).

Perform the following steps in the project directory:

1. `docker-machine start default`
1. `docker-compose up`
1. Copy the machine's IP address obtained from this command: `docker-machine ip default`
1. Change the IP address for RabbitMQ host in `src/main/resources/application.yaml` to the one you just copied
1. `mvn install`
1. Finally, to start the application: `mvn exec:java`
