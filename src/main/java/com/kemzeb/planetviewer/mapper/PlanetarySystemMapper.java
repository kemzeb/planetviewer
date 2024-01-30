package com.kemzeb.planetviewer.mapper;

import com.kemzeb.planetviewer.dto.PlanetarySystemDto;
import com.kemzeb.planetviewer.entity.PlanetarySystem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface PlanetarySystemMapper {

  PlanetarySystemDto toPlanetarySystemDto(PlanetarySystem planetarySystem);
}
