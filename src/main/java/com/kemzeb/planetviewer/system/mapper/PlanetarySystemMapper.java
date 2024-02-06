package com.kemzeb.planetviewer.system.mapper;

import com.kemzeb.planetviewer.system.dto.PlanetarySystemDto;
import com.kemzeb.planetviewer.system.entity.PlanetarySystem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface PlanetarySystemMapper {

  PlanetarySystemDto toPlanetarySystemDto(PlanetarySystem planetarySystem);
}
