package com.kemzeb.starviewer.star;

import com.kemzeb.starviewer.exception.StarNotFoundException;
import com.kemzeb.starviewer.star.dto.StarDto;
import com.kemzeb.starviewer.star.dto.StarMapper;
import com.kemzeb.starviewer.star.entity.Star;
import com.kemzeb.starviewer.star.entity.StarRepository;
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
