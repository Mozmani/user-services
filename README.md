# User Management Service

A simple microservice to handle users, user roles, and permissions. **(A work in progress)**

## Quick Run:
#### run `./gradlew runLocal`
This works if you are using a bash / zsh terminal, and your user is configured to 
use docker commands. This will perform everything needed and you can skip the first run 
instructions.


## First Local Launch Instructions:
1) In order to run this application we must first build the application.
We can do this by running `./gradlew build`
2) The first time you run this application locally, you will need to generate
the local database password, as well as the public and private pem keys used
for authorization checks. This can be done by running `./gradlew initLocalEnv`
This creates 3 files locally that are automatically ignored from version control.
3) Now all you need to do is launch the application with docker compose
by running `docker-compose up --build`


## Setting Up Debug Mode:
Since we are running inside a docker container, we will need to create a 
remote JVM. This can be done very simply by creating a new configuration, to do
so in IntelliJ simply do the following:
1) In the run menu, select `edit configurations`
2) Click the plus, and select `Remote JVM Debug` and give it a name
3) Change the port to `5004` (This is set in the Dockerfile) and apply
4) Once this is set, you can simply click this as the container is running to debug.

