package it.infn.voms.admin.search.parser.node;

import it.infn.voms.admin.search.parser.AttributeExpressionTerm;

public class AttributeExpressionMatch extends QNode {

  final AttributeExpressionTerm attributeTerm;
  final String value;

  public AttributeExpressionMatch(AttributeExpressionTerm t, String v) {

    attributeTerm = t;
    value = v.substring(1, v.length() - 1);

  }

  public AttributeExpressionTerm getAttributeTerm() {

    return attributeTerm;
  }

  public String getValue() {

    return value;
  }

  @Override
  public String toString() {

    return String.format("%s '%s'", attributeTerm.getRepr(), value);
  }

  @Override
  public <V> V accept(QNodeVisitor<V> visitor) {

    return visitor.visit(this);
  }
}
