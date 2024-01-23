package com.kemzeb.starviewer.exoplanet.entity;

import com.kemzeb.starviewer.star.entity.Star;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExoplanetRepository extends JpaRepository<Exoplanet, String> {

  List<Exoplanet> findAllByStellarHost(Star stellarHost);
}
