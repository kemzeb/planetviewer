package com.kemzeb.starviewer.db.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.mapstruct.Mapping;

@Retention(RetentionPolicy.CLASS)
@Mapping(source = "discFacility", target = "discoveryFacility")
@Mapping(source = "discoverymethod", target = "discoveryMethod")
@Mapping(source = "discYear", target = "discoveryYear")
@Mapping(source = "plBmasse", target = "earthMass")
@Mapping(source = "plRade", target = "earthRadius")
@Mapping(source = "plEqt", target = "equilibriumTemperatureKelvin")
@Mapping(source = "plName", target = "name")
@Mapping(source = "plOrbper", target = "orbitalPeriodDays")
@Mapping(target = "stellarHost", ignore = true)
public @interface ToExoplanet {}
