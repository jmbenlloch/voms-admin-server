package it.infn.voms.admin.search.parser.node;

import it.infn.voms.admin.search.parser.BooleanExpressionTerm;

public class StatusMatch extends QNode {

  final BooleanExpressionTerm statusFlag;
  final Boolean value;

  public StatusMatch(BooleanExpressionTerm sf, Boolean v) {

    super();
    statusFlag = sf;
    value = v;
  }

  public BooleanExpressionTerm getStatusFlag() {

    return statusFlag;
  }

  public Boolean getValue() {

    return value;
  }
  
  @Override
  public String toString() {
    return String.format("%s is %b", 
      statusFlag.getRepr(),
      value.booleanValue());
  }
  
  @Override
  public <V> V accept(QNodeVisitor<V> visitor) {
    return visitor.visit(this);
  }
}
