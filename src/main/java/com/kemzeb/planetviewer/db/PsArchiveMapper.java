package com.kemzeb.planetviewer.db;

import com.kemzeb.planetviewer.db.annotation.ToExoplanet;
import com.kemzeb.planetviewer.db.annotation.ToPlanetarySystem;
import com.kemzeb.planetviewer.db.annotation.ToStar;
import com.kemzeb.planetviewer.entity.Exoplanet;
import com.kemzeb.planetviewer.entity.PlanetarySystem;
import com.kemzeb.planetviewer.entity.Star;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PsArchiveMapper {

  @ToPlanetarySystem
  PlanetarySystem toPlanetarySystem(PsArchive psArchive);

  @ToStar
  Star toStar(PsArchive psArchive);

  @ToExoplanet
  Exoplanet toExoplanet(PsArchive psArchive);

  @ToPlanetarySystem
  void updatePlanetarySystem(PsArchive psArchive, @MappingTarget PlanetarySystem planetarySystem);

  @ToStar
  void updateStar(PsArchive psArchive, @MappingTarget Star star);

  @ToExoplanet
  void updateExoplanet(PsArchive psArchive, @MappingTarget Exoplanet exoplanet);

  default void updateNullFieldsInPlanetarySystem(
      PsArchive psArchive, PlanetarySystem planetarySystem) {
    if ((psArchive.syDist != null) && (planetarySystem.getDistanceParsecs() == null)) {
      planetarySystem.setDistanceParsecs(psArchive.syDist);
    }

    if ((psArchive.sySnum != null) && (planetarySystem.getNumStars() == null)) {
      planetarySystem.setNumStars(psArchive.sySnum);
    }

    if ((psArchive.syPnum) != null && (planetarySystem.getNumPlanets() == null)) {
      planetarySystem.setNumPlanets(psArchive.syPnum);
    }

    if ((psArchive.syMnum != null) && (planetarySystem.getNumMoons() == null)) {
      planetarySystem.setNumMoons(psArchive.syMnum);
    }
  }

  default void updateNullFieldsInStar(PsArchive psArchive, Star star) {
    // name and starSystem should already be set (and shouldn't change anyways).

    if ((psArchive.stSpectype != null) && (star.getSpectralType() == null)) {
      star.setSpectralType(psArchive.stSpectype);
    }

    if ((psArchive.stTeff != null) && (star.getEffectiveTemperatureKelvin() == null)) {
      star.setEffectiveTemperatureKelvin(psArchive.stTeff);
    }

    if ((psArchive.stRad != null) && (star.getSolarRadius() == null)) {
      star.setSolarRadius(psArchive.stRad);
    }

    if ((psArchive.stMass != null) && (star.getSolarMass() == null)) {
      star.setSolarMass(psArchive.stMass);
    }

    if ((psArchive.stAge != null) && (star.getAgeGyr() == null)) {
      star.setAgeGyr(psArchive.stAge);
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
