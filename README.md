# planetviewer

A fun, hobby full-stack alternative to NASA Exoplanet Archive's [Overview service](https://exoplanetarchive.ipac.caltech.edu/overview), which provides search and data overview capabilities for exoplanets and stars.

This project exists for me to explore the realm of astronomy, all in the while building a full-stack web application that has me work with technologies I am interested in learning/reviewing.

This project utilizes the [Planetary Systems](https://exoplanetarchive.ipac.caltech.edu/cgi-bin/TblView/nph-tblView?app=ExoTbls&config=PS) table that they maintain in order to gather exoplanet-related and star-related data.
It does have duplicate rows for each exoplanet (as each row represents a publication associated to that exoplanet). So when fetching the data using the Archive's TAP service, all I do is essentially perform a union of all
the duplicate rows, but give precendence to the row that the Archive flagged as the "default" row. From the looks of it, there does seem to be limitation with this table; I don't have a way of uniquely identifying a planetary system, unlike the Overview service. From reading the description of this service, it seem to draw its data from its "own list of cross-identifications for every star and planet in its database" that doesn't seem to be accessible to the public.

## Todo
- [ ] Implement an endpoint for searching for stars
- [ ] Refine and add additional filtering capabilites for the search endpoint
- [ ] Implement a production environment that users can run (instead of using the dev environment) using containers (via Docker Compose)
- [ ] Design and implement an Angular client that consumes the RESTful service such that it can search for and provide an visual overview for any exoplanet or star.

... and more too come

## Usage
There isn't a production environment that you can use yet, but you can run this app using the development environment by hopping on VSCode and taking advantage of the [Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) extension.

Once you fired up the dev environment, execute the following in your shell:
1. Fetch the data from the NASA Exoplanet Archive's TAP service by running the following script in the project's root directory:

`sh scripts/archive-fetch.sh`

2. Perform the necessary code generation for the MapStruct and Lombok code generators by doing the following:

`mvn clean install`

3. Run the Spring Boot app using its "run" Maven goal:

`mvn spring-boot:run`

This should fire up a RESTful HTTP server for you to communicate with (using any HTTP client such as `curl`, `wget`, `Postman`). Use `http://localhost:8080/api/` to see what endpoints are available!

