FROM eclipse-temurin:17-jdk-alpine
ARG JAR_FILE=build/libs/user-services-0.0.1-SNAPSHOT.jar
ENV JAVA_TOOL_OPTIONS -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5004
COPY ${JAR_FILE} application.jar