FROM gradle:8.5-alpine as my_build
LABEL authors="Francisco Lucas"

EXPOSE 9999

COPY . /app
WORKDIR /app
# gradle assemble is used to disable tests
RUN cd /app; gradle clean; gradle assemble --no-daemon

FROM openjdk:17-alpine

EXPOSE 9999

RUN mkdir /app
COPY --from=my_build /app /app
ENTRYPOINT ["java", "-jar", "/app/build/libs/FL-Refs-1.0.jar"]