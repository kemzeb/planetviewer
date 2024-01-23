package com.kemzeb.starviewer.db.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.mapstruct.Mapping;

@Retention(RetentionPolicy.CLASS)
@Mapping(source = "hostname", target = "name")
@Mapping(source = "stSpectype", target = "spectralType")
@Mapping(source = "stTeff", target = "effectiveTemperatureKelvin")
@Mapping(source = "stRad", target = "solarRadius")
@Mapping(source = "stMass", target = "solarMass")
@Mapping(source = "stAge", target = "ageGyr")
@Mapping(target = "planetarySystem", ignore = true)
public @interface ToStar {}
