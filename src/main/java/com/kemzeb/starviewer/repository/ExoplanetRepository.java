package com.kemzeb.starviewer.repository;

import com.kemzeb.starviewer.entity.Exoplanet;
import com.kemzeb.starviewer.entity.Star;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExoplanetRepository extends JpaRepository<Exoplanet, String> {

  List<Exoplanet> findAllByStellarHost(Star stellarHost);
}
