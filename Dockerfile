## TODO: this is temporary. Switch to multi-stage after development.
#FROM openjdk:17-alpine as jdk_stage
#EXPOSE 80
#RUN mkdir /app
#COPY ./backend/build/libs /app
#ENTRYPOINT ["java", "-jar", "/app/backend-1.0.jar"]

FROM gradle:8.5-alpine as gradle_stage
LABEL authors="Francisco Lucas"
COPY ./backend /app
WORKDIR /app
RUN cd /app
RUN gradle clean
# gradle assemble is used to build without running tests
# also daemons are not needed because gradle will be discarded
RUN gradle assemble --no-daemon

FROM openjdk:17-alpine as jdk_stage
EXPOSE 80
RUN mkdir /app
COPY --from=gradle_stage /app /app
ENTRYPOINT ["java", "-jar", "/app/build/libs/backend-1.0.jar"]