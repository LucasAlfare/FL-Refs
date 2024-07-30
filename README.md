# Refs 🖼

This repository contains a complete backend to handle the upload of images to some kind of `CDN`. It also handles the
ability to get the stored data as well.

At this point, we are using GutHub as the main `CDN` target, but this should be updated to a new dedicated `CDN` service
soon.

### Tech stack 📚

This server is being built using:

- `Kotlin` as main language/ecosystem;
- `Gradle` as build system and dependencies manager;
- `Ktor` as webserver;
- `Exposed` as SQL framework ORM;
- `Thumbnailator` to generate thumbnails from original images;
- `Docker` to containerize builds.

We are also using `GitHub` itself as a huge basic `CDN` service. Depending on the usage of this, `GitHub` works fine
to store that kind of images. In the future, we should to include more implementations of external `CDN` services, such
as that provided by `Amazon` or even by other files repositories, such as `MediaFire`.

### Design 📘

This project works basically as a middleware for organizing the data. Being the main aim store images binaries and
relate then to some meta-data information (names, descriptions, URLs of uncompressed and thumbnails, etc.), we choose to
use a database and a `CDN` external service, being database used to store meta-data info about the images and `CDN` used
to store the images binaries themselves.

The actual flow of usage is:
- Send a `POST` request with `JSON` data, respecting the definitions of [UploadRequestDTO.kt](backend%2Fsrc%2Fmain%2Fkotlin%2Fcom%2Flucasalfare%2Fflrefs%2Fmain%2Fdomain%2Fmodel%2Fdto%2FUploadRequestDTO.kt);
- Try to insert the meta-data info in the database;
- If info insertion succeeds, we perform an attempt of upload of the received image bytes to the current defined `CDN`;
- If image was possible to be uploaded to the `CDN`, we generate a thumbnail version of it and try to upload it as well;
- if everything is ok, we respond status `201 Created`, otherwise, error status are responded.

Due to the `Kotlin Exposed` "`newSuspendedTransaction()`" API, we consider that if something is thrown during the above
process we just rollback. But is also important to know that, at this point, we didn't implemented rolling back the
uploads to the `CDN` if something after that fail. This should be implemented soon.

Other point is either `upload` and either `clear` are under `authenticate` blocks. This means that only requests with
valid JWT tokens in `Authorization` header will be handled.

By the end, this project was built onto `clean architecture` concepts. Due to this, we can swap between different
external `CDN` implementations with no pain.

### Running ✅

This project can be executed with `Docker`. To do this, clone this project locally and set up a `.env` file
containing the necessary environment fields for the application. The final file should look like this:

```properties
DB_JDBC_URL=som url for JDBC driver connect in a database server 
DB_JDBC_DRIVER=the targeted JDBC driver used
DB_USERNAME=the username to access the database
DB_PASSWORD=the password to access the database
ADMIN_EMAIL=the default global admin email to authenticate and receive JWTs
ADMIN_PLAIN_PASSWORD=the default global admin password to authenticate and receive JWTs
GITHUB_API_TOKEN=a API token from a github user, if github is used as the default CDN repository
GITHUB_CDN_USERNAME=the name of the user that owns the targeted github-as-CDN repository, if github is used as CDN
GITHUB_CDN_REPOSITORY=the name of the github repository that will hold the files uploaded by this project, if github is used as CDN 
JWT_AUTH_REALM=a basic JWT realm to be used in JWT validation
JWT_ALGORITHM_SECRET=a secret used in the JWT generation/validation
```
_Note: we will clean up those variables names or even use multiple `.env` files to hold distinct environment groups._

Place the `.env` file in the project root directory, `docker-compose` will look for this file automatically. Navigate to
the folder and run

```sh
docker-compose up --build
```

This will build an image of the project and run it in a container by
docker, as described in [Dockerfile](Dockerfile) and [docker-compose.yaml](docker-compose.yaml).

After container is up, the server can be accessed through the port `80`.