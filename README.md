# planetviewer

A fun, hobby backend alternative to NASA Exoplanet Archive's [Overview service](https://exoplanetarchive.ipac.caltech.edu/overview), which provides search and data overview capabilities for exoplanets and stars.

This project exists for me to explore the realm of astronomy, all in the while building a full-stack web application that has me work with technologies I am interested in learning/reviewing.

This project utilizes the [Planetary Systems](https://exoplanetarchive.ipac.caltech.edu/cgi-bin/TblView/nph-tblView?app=ExoTbls&config=PS) table that they maintain in order to gather exoplanet-related and star-related data.
It does have duplicate rows for each exoplanet (as each row represents a publication associated to that exoplanet). So when fetching the data using the Archive's TAP service, all I do is essentially perform a union of all
the duplicate rows, but give precendence to the row that the Archive flagged as the "default" row. From the looks of it, there does seem to be limitation with this table; I don't have a way of uniquely identifying a planetary system, unlike the Overview service. From reading the description of this service, it seem to draw its data from its "own list of cross-identifications for every star and planet in its database" that doesn't seem to be accessible to the public.

## Todo
- [x] Implement an endpoint for searching for planets and stars
- [x] Add filtering capabilites for the search endpoint
- [x] Implement a production environment that users can run (instead of using the dev environment) using containers (via Docker Compose)
- [ ] Design and implement an Angular client that consumes the RESTful service such that it can search for and provide an visual overview for any exoplanet or star.

... and more too come

## Usage
***NOTE:*** (The following command requires Docker and a Unix-based system to run!)

To run it, use the following command in the project's root directory:
```console
sh meta/run.sh
```

After some time fetching the PS table from the Exoplanet Archive, building the PostgreSQL database, and building the Elasticsearch indices, it should fire up a RESTful HTTP server for you to communicate with (using your favorite HTTP client such as `curl`, `wget`, `Postman`). Use `http://localhost:8080/api/` to see what endpoints are available!

