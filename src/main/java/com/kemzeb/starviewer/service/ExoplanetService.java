package com.kemzeb.starviewer.service;

import com.kemzeb.starviewer.dto.ExoplanetDto;
import com.kemzeb.starviewer.entity.Exoplanet;
import com.kemzeb.starviewer.entity.Star;
import com.kemzeb.starviewer.exception.ExoplanetNotFoundException;
import com.kemzeb.starviewer.exception.StarNotFoundException;
import com.kemzeb.starviewer.mapper.ExoplanetMapper;
import com.kemzeb.starviewer.repository.ExoplanetRepository;
import com.kemzeb.starviewer.repository.StarRepository;
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
