package com.kemzeb.starviewer.system.dto;

import com.kemzeb.starviewer.system.entity.PlanetarySystem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface PlanetarySystemMapper {

  PlanetarySystemDto toPlanetarySystemDto(PlanetarySystem planetarySystem);
}
