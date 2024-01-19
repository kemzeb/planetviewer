package com.kemzeb.starviewer.star;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface StarMapper {

  StarDto toStarDto(Star star);
}
