package com.kemzeb.planetviewer.exoplanet.service;

import com.kemzeb.planetviewer.exception.ExoplanetNotFoundException;
import com.kemzeb.planetviewer.exception.StarNotFoundException;
import com.kemzeb.planetviewer.exoplanet.dto.ExoplanetDto;
import com.kemzeb.planetviewer.exoplanet.entity.Exoplanet;
import com.kemzeb.planetviewer.exoplanet.mapper.ExoplanetMapper;
import com.kemzeb.planetviewer.exoplanet.repo.ExoplanetRepository;
import com.kemzeb.planetviewer.star.entity.Star;
import com.kemzeb.planetviewer.star.repo.StarRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExoplanetService {

  private final ExoplanetMapper exoplanetMapper;
  private final ExoplanetRepository exoplanetRepository;
  private final StarRepository starRepository;

  public Page<ExoplanetDto> getExoplanetList(Pageable pageable) {
    Page<Exoplanet> exoplanets = exoplanetRepository.findAll(pageable);
    return exoplanets.map(exoplanetMapper::toExoplanetDto);
  }

  public ExoplanetDto findExoplanet(String name) {
    Exoplanet exoplanet =
        exoplanetRepository.findById(name).orElseThrow(() -> new ExoplanetNotFoundException(name));
    return exoplanetMapper.toExoplanetDto(exoplanet);
  }

  public List<ExoplanetDto> findExoplanetsThatOrbitStar(String name) {
    Star star = starRepository.findById(name).orElseThrow(() -> new StarNotFoundException(name));
    List<Exoplanet> exoplanets = exoplanetRepository.findAllByStellarHost(star);
    return exoplanets.stream().map(exoplanetMapper::toExoplanetDto).toList();
  }
}
