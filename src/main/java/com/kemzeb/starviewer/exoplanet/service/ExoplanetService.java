package com.kemzeb.starviewer.exoplanet.service;

import com.kemzeb.starviewer.exception.ExoplanetNotFoundException;
import com.kemzeb.starviewer.exoplanet.dto.ExoplanetDto;
import com.kemzeb.starviewer.exoplanet.dto.ExoplanetMapper;
import com.kemzeb.starviewer.exoplanet.entity.Exoplanet;
import com.kemzeb.starviewer.exoplanet.entity.ExoplanetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExoplanetService {

  private final ExoplanetMapper exoplanetMapper;
  private final ExoplanetRepository exoplanetRepository;

  public ExoplanetDto findExoplanet(String name) {
    Exoplanet exoplanet =
        exoplanetRepository.findById(name).orElseThrow(() -> new ExoplanetNotFoundException(name));
    return exoplanetMapper.toExoplanetDto(exoplanet);
  }
}
