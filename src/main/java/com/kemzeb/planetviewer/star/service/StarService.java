package com.kemzeb.planetviewer.star.service;

import com.kemzeb.planetviewer.exception.StarNotFoundException;
import com.kemzeb.planetviewer.star.dto.StarDto;
import com.kemzeb.planetviewer.star.entity.Star;
import com.kemzeb.planetviewer.star.mapper.StarMapper;
import com.kemzeb.planetviewer.star.repo.StarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StarService {

  private final StarMapper starMapper;
  private final StarRepository starRepository;

  public Page<StarDto> getStarList(Pageable pageable) {
    Page<Star> stars = starRepository.findAll(pageable);
    return stars.map(starMapper::toStarDto);
  }

  public StarDto findStar(String name) {
    Star star = starRepository.findById(name).orElseThrow(() -> new StarNotFoundException(name));
    return starMapper.toStarDto(star);
  }
}
