package com.kemzeb.planetviewer.search.filter.parser;

import lombok.Builder;

@Builder
public record ParsedFilter(String field, Operator operator, String literal) {}
