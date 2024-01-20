package com.kemzeb.starviewer.star;

import com.kemzeb.starviewer.exception.StarNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StarService {

  private final StarAssembler starAssembler;
  private final StarRepository starRepository;

  public Page<StarDto> getStarList(Pageable pageable) {
    Page<Star> stars = starRepository.findAll(pageable);
    return stars.map(starAssembler::toModel);
  }

  public StarDto findStar(String name) {
    Star star = starRepository.findById(name).orElseThrow(() -> new StarNotFoundException(name));
    return starAssembler.toModel(star);
  }
}
