package com.kemzeb.starviewer.db.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.mapstruct.Mapping;

@Retention(RetentionPolicy.CLASS)
@Mapping(source = "syDist", target = "distanceParsecs")
@Mapping(source = "sySnum", target = "numStars")
@Mapping(source = "syMnum", target = "numMoons")
@Mapping(source = "syPnum", target = "numPlanets")
@Mapping(source = "glat", target = "galacticLatitudeDegrees")
@Mapping(source = "glon", target = "galacticLongitudeDegrees")
@Mapping(target = "id", ignore = true)
public @interface ToPlanetarySystem {}
