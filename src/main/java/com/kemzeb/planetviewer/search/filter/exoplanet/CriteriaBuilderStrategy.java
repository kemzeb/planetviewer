package com.kemzeb.planetviewer.search.filter.exoplanet;

import com.kemzeb.planetviewer.search.filter.parser.Operator;
import com.kemzeb.planetviewer.search.filter.parser.ParsedFilter;
import java.util.List;
import org.springframework.data.elasticsearch.core.query.Criteria;

public abstract class CriteriaBuilderStrategy {

  public abstract Criteria buildCriteria(String keyword, List<ParsedFilter> filters);

  protected final <T extends Number> Criteria addOperation(
      Criteria criteria, Operator operator, T operand) {
    switch (operator) {
      case GREATER_THAN:
        return criteria.greaterThan(operand);
      case GREATER_THAN_OR_EQUAL_TO:
        return criteria.greaterThanEqual(operand);
      case LESS_THAN:
        return criteria.lessThan(operand);
      case LESS_THAN_OR_EQUAL_TO:
        return criteria.lessThanEqual(operand);
      default:
        return criteria.matches(operand);
    }
  }
}
