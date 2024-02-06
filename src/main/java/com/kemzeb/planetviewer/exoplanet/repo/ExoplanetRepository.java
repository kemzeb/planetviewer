package com.kemzeb.planetviewer.exoplanet.repo;

import com.kemzeb.planetviewer.exoplanet.entity.Exoplanet;
import com.kemzeb.planetviewer.star.entity.Star;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExoplanetRepository extends JpaRepository<Exoplanet, String> {

  List<Exoplanet> findAllByStellarHost(Star stellarHost);
}
