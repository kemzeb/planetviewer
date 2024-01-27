package com.kemzeb.starviewer.mapper;

import com.kemzeb.starviewer.dto.PlanetarySystemDto;
import com.kemzeb.starviewer.entity.PlanetarySystem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface PlanetarySystemMapper {

  PlanetarySystemDto toPlanetarySystemDto(PlanetarySystem planetarySystem);
}
