package com.kemzeb.starviewer.star.dto;

import com.kemzeb.starviewer.star.Star;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface StarMapper {

  StarDto toStarDto(Star star);
}
