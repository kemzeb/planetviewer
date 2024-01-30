package com.kemzeb.planetviewer.repository;

import com.kemzeb.planetviewer.entity.Exoplanet;
import com.kemzeb.planetviewer.entity.Star;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExoplanetRepository extends JpaRepository<Exoplanet, String> {

  List<Exoplanet> findAllByStellarHost(Star stellarHost);
}
