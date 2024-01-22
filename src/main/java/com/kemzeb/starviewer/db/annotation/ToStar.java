package com.kemzeb.starviewer.db.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.mapstruct.Mapping;

@Retention(RetentionPolicy.CLASS)
@Mapping(source = "hostname", target = "name")
@Mapping(source = "stSpectype", target = "spectralType")
@Mapping(target = "starSystem", ignore = true)
public @interface ToStar {}
