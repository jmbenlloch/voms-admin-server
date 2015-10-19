package it.infn.voms.admin.search.parser.node;

import it.infn.voms.admin.search.parser.FieldName;
import it.infn.voms.admin.search.parser.MatchOperator;

public class FieldMatch extends QNode {

  final FieldName fieldName;
  final MatchOperator matchOperator;
  final String value;

  protected FieldMatch(FieldName f, MatchOperator o, String value) {

    this.fieldName = f;
    this.matchOperator = o;
    this.value = value.substring(1, value.length() - 1);
  }

  public FieldName getFieldName() {

    return fieldName;
  }

  public MatchOperator getMatchOperator() {

    return matchOperator;
  }
  
  public String getValue() {
  
    return value;
  }

  @Override
  public String toString() {

    return String.format("%s %s '%s'", fieldName.getRepr(),
      matchOperator.getRepr(), value);
  }
  
  @Override
  public <V> V accept(QNodeVisitor<V> visitor) {
    return visitor.visit(this);
  }

}
