# Heidenheim an der Brenz via Wikipedia
This is a simple API for accessing information about the town of Heidenheim an der Brenz in southern Germany. This API was built as part of a technical assessment of Nate Hall's software engineering skills.

## Running the application
To build the project from the root directory:

```bash
./gradlew bootRun
```

To run as a Docker container:

```bash
./gradlew build
docker build -t natehall/tech-assessment .
docker run -p 8080:8080 -d --name <some_name> natehall/tech-assessment
```

The application will be available locally on port 8080.

To stop:

```bash
docker stop <some_name>
```

## Using the API
The following REST endpoints are available for V1. All endpoints start with `/api/v1/`.

| HTTP Verb    | Path          | Optional parameter   | What does it do?
|--------------|---------------|----------------------|-------------------
| GET          | /articles     | articleContent       | Returns all articles. If the parameter is included, returns all articles containing the supplied text.
| GET          | /articles/{id}| none                 | Returns the specific article by it's id (1 for Heidenheim an der Brenz) or a 404 if the article with that id is not found.
| GET          | /articles/{id}/sections | searchTerm | Returns all sections of the article matching the supplied id. If the parameter is supplied, returns all sections containing the supplied text.
| GET          | /articles/{id}/sections/{sectionId} | none | Returns the specific section of the article requested, of a 404 if that section id is not found. Section ids are in the form `1` for a top level section, `1-2` for top level with its first subsection, etc.