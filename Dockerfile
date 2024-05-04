FROM gradle:8.5-alpine as my_build
LABEL authors="Francisco Lucas"
EXPOSE 9999
COPY . /app
WORKDIR /app

FROM openjdk:17-alpine

RUN mkdir /app
COPY --from=my_build /app /app
ENTRYPOINT ["java", "-jar", "/app/build/libs/FL-Refs-1.0.jar"]