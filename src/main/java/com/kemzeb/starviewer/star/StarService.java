package com.kemzeb.starviewer.star;

import com.kemzeb.starviewer.exception.StarNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StarService {

  private final StarMapper starMapper;
  private final StarRepository starRepository;

  public StarDto findStar(String name) {
    Star star = starRepository.findById(name).orElseThrow(() -> new StarNotFoundException(name));
    return starMapper.toStarDto(star);
  }
}
