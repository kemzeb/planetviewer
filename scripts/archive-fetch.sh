# Fetches the PS table in JSON format from the NASA Exoplanet Archive using
# their TAP service. Should be run in the project root directory.

curl "https://exoplanetarchive.ipac.caltech.edu/TAP/sync?query=select+default_flag,pl_name,hostname,sy_snum,\
sy_pnum,sy_mnum,discoverymethod,disc_year,disc_facility,pl_orbper,pl_rade,pl_bmasse,pl_eqt,st_spectype\
,st_teff,st_rad,st_mass,st_age,sy_dist,glat,glon+from+ps&format=json"\
 > src/main/resources/ps.json