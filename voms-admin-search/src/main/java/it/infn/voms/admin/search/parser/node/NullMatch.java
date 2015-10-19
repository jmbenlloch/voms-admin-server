package it.infn.voms.admin.search.parser.node;

import it.infn.voms.admin.search.parser.FieldName;

public class NullMatch extends QNode {

  final FieldName fieldName;

  public NullMatch(FieldName f) {

    fieldName = f;
  }

  @Override
  public String toString() {

    return fieldName.getRepr() + " is null";
  }

  @Override
  public <V> V accept(QNodeVisitor<V> visitor) {

    return visitor.visit(this);
  }

  public FieldName getFieldName() {

    return fieldName;
  }

}
