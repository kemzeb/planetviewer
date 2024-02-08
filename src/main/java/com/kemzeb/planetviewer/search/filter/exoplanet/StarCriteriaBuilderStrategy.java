package com.kemzeb.planetviewer.search.filter.exoplanet;

import com.kemzeb.planetviewer.search.filter.parser.ParsedFilter;
import jakarta.validation.ValidationException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.util.Pair;

public class StarCriteriaBuilderStrategy extends CriteriaBuilderStrategy {

  /**
   * Mapping of fields (i.e. the name we have clients use) to the ElasticSearch fields defined in
   * {@link com.kemzeb.planetviewer.exoplanet.entity.ExoplanetDocument}.
   */
  private static final Map<String, Pair<String, Class<?>>> FIELDS_MAP;

  static {
    Map<String, Pair<String, Class<?>>> tempMap = new HashMap<>();

    tempMap.put("spectralType", Pair.of("spectralType", String.class));
    tempMap.put("numStars", Pair.of("sysNumStars", Integer.class));
    tempMap.put("numPlanets", Pair.of("sysNumPlanets", Integer.class));
    tempMap.put("numMoons", Pair.of("sysNumMoons", Integer.class));
    tempMap.put("solarMass", Pair.of("solarMass", Double.class));

    FIELDS_MAP = Collections.unmodifiableMap(tempMap);
  }

  // TODO: Should we deduplicate this or is it alright to have more control on the criteria impl?
  // ...since this is just a copy of the exoplanet criteria builder strategy code.

  @Override
  public Criteria buildCriteria(String keyword, List<ParsedFilter> filters) {
    Set<String> fieldSet = new HashSet<>();
    Criteria criteria = Criteria.where("name").matches(keyword);

    for (ParsedFilter filter : filters) {
      criteria.and(addCriteria(filter, fieldSet));
    }

    return criteria;
  }

  private Criteria addCriteria(ParsedFilter filter, Set<String> fieldSet) {
    String field = filter.field();
    if (fieldSet.contains(field)) {
      throw new ValidationException(String.format("Given duplicate field \"%s\"", field));
    }

    fieldSet.add(field);

    if (!FIELDS_MAP.containsKey(field)) {
      throw new ValidationException(String.format("Unknown filter field \"%s\".", field));
    }

    Pair<String, Class<?>> esFieldAndDataTypePair = FIELDS_MAP.get(field);
    String esField = esFieldAndDataTypePair.getFirst();
    Class<?> clazz = esFieldAndDataTypePair.getSecond();

    Criteria criteria = Criteria.where(esField);

    if (clazz == String.class) {
      return criteria.matches(filter.value());
    }
    if (clazz == Integer.class) {
      Integer operand = tryToParseInt(filter.value());
      return addOperation(criteria, filter.operator(), operand);
    }
    if (clazz == Double.class) {
      Double operand = tryToParseDouble(filter.value());
      return addOperation(criteria, filter.operator(), operand);
    }

    throw new RuntimeException(
        String.format("Field \"%s\" is mapped to unsupported class type.", esField));
  }

  private Integer tryToParseInt(String value) {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      throw new ValidationException("Expected a given filter value to be a valid integer.");
    }
  }

  private Double tryToParseDouble(String value) {
    try {
      return Double.parseDouble(value);
    } catch (NumberFormatException e) {
      throw new ValidationException("Expected a given filter value to be a valid double.");
    }
  }
}
