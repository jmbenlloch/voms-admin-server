package it.infn.voms.admin.search.parser.node;

import it.infn.voms.admin.search.parser.AttributeExpressionTerm;
import it.infn.voms.admin.search.parser.BooleanExpressionTerm;
import it.infn.voms.admin.search.parser.FieldName;
import it.infn.voms.admin.search.parser.MatchOperator;

public class Nodes {

  private Nodes() {

  }

  public static QNode fieldMatch(String fieldName, String operator, String value) {

    return new FieldMatch(FieldName.fromString(fieldName),
      MatchOperator.fromString(operator), value);
  }

  public static QNode negate(QNode child) {

    return new NegateMatch(child);
  }

  public static QNode conjunction(QNode left, QNode right) {

    return new ConjunctionMatch(left, right);
  }

  public static QNode disjunction(QNode left, QNode right) {

    return new DisjunctionMatch(left, right);
  }

  public static QNode nullMatch(String field) {

    return new NullMatch(FieldName.fromString(field));
  }

  public static QNode notNullMatch(String field) {

    return new NotNullMatch(FieldName.fromString(field));
  }

  public static QNode statusMatch(String statusFlag, Boolean value) {

    return new StatusMatch(BooleanExpressionTerm.fromString(statusFlag), value);
  }

  public static QNode attributeExpressionMatch(String ae, String value) {

    return new AttributeExpressionMatch(AttributeExpressionTerm.fromString(ae),
      value);
  }
}
