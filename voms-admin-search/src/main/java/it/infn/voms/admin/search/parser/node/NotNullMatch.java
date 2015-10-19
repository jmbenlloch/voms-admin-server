package it.infn.voms.admin.search.parser.node;

import it.infn.voms.admin.search.parser.FieldName;

public class NotNullMatch extends QNode {

  final FieldName fieldName;

  public NotNullMatch(FieldName f) {

    fieldName = f;
  }

  @Override
  public String toString() {

    return fieldName.getRepr() + " is not null";
  }

  public FieldName getFieldName() {

    return fieldName;
  }

  @Override
  public <V> V accept(QNodeVisitor<V> visitor) {

    return visitor.visit(this);
  }

}
