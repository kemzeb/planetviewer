package com.kemzeb.starviewer.db;

import com.kemzeb.starviewer.db.annotation.ToExoplanet;
import com.kemzeb.starviewer.db.annotation.ToStar;
import com.kemzeb.starviewer.db.annotation.ToStarSystem;
import com.kemzeb.starviewer.exoplanet.entity.Exoplanet;
import com.kemzeb.starviewer.star.entity.Star;
import com.kemzeb.starviewer.system.entity.StarSystem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SystemArchiveMapper {

  @ToStarSystem
  StarSystem toStarSystem(PsArchive psArchive);

  @ToStar
  Star toStar(PsArchive psArchive);

  @ToExoplanet
  Exoplanet toExoplanet(PsArchive psArchive);

  @ToStarSystem
  void updateStarSystem(PsArchive psArchive, @MappingTarget StarSystem starSystem);

  @ToStar
  void updateStar(PsArchive psArchive, @MappingTarget Star star);

  @ToExoplanet
  void updateExoplanet(PsArchive psArchive, @MappingTarget Exoplanet exoplanet);

  default void updateNullFieldsInStarSystem(PsArchive psArchive, StarSystem starSystem) {
    if ((psArchive.syDist != null) && (starSystem.getDistanceParsecs() == null)) {
      starSystem.setDistanceParsecs(psArchive.syDist);
    }

    if ((psArchive.sySnum != null) && (starSystem.getNumStars() == null)) {
      starSystem.setNumStars(psArchive.sySnum);
    }

    if ((psArchive.syPnum) != null && (starSystem.getNumPlanets() == null)) {
      starSystem.setNumPlanets(psArchive.syPnum);
    }

    if ((psArchive.syMnum != null) && (starSystem.getNumMoons() == null)) {
      starSystem.setNumMoons(psArchive.syMnum);
    }
  }

  default void updateNullFieldsInStar(PsArchive psArchive, Star star) {
    // name and starSystem should already be set (and shouldn't change anyways).

    if ((psArchive.stSpectype != null) && (star.getSpectralType() == null)) {
      star.setSpectralType(psArchive.stSpectype);
    }
  }

  default void updateNullFieldsInExoplanet(PsArchive psArchive, Exoplanet exoplanet) {
    // name, stellarHost, and discovery-related columns should already be set (and shouldn't change
    // anyways).

    if ((psArchive.plOrbper != null) && (exoplanet.getOrbitalPeriodDays() == null)) {
      exoplanet.setOrbitalPeriodDays(psArchive.plOrbper);
    }

    if ((psArchive.plRade != null) && (exoplanet.getEarthRadius() == null)) {
      exoplanet.setEarthRadius(psArchive.plRade);
    }

    if ((psArchive.plBmasse != null) && (exoplanet.getEarthMass() == null)) {
      exoplanet.setEarthMass(psArchive.plBmasse);
    }

    if ((psArchive.plEqt != null) && (exoplanet.getEquilibriumTemperatureKelvin() == null)) {
      exoplanet.setEquilibriumTemperatureKelvin(psArchive.plEqt);
    }
  }
}
