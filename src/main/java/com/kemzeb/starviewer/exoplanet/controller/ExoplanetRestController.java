package com.kemzeb.starviewer.exoplanet.controller;

import com.kemzeb.starviewer.exoplanet.dto.ExoplanetDto;
import com.kemzeb.starviewer.exoplanet.service.ExoplanetService;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/planets")
@RequiredArgsConstructor
public class ExoplanetRestController {

  private final ExoplanetService exoplanetService;

  @GetMapping(path = "/{name}", produces = "application/hal+json")
  public ExoplanetDto findExoplanet(@PathVariable("name") String encodedName) {
    String decodedName = URLDecoder.decode(encodedName, Charset.defaultCharset());
    return exoplanetService.findExoplanet(decodedName);
  }
}
