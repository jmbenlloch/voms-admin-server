package it.infn.voms.admin.search.parser.node;

import it.infn.voms.admin.search.parser.BooleanOperator;

public class NegateMatch extends LogicalCombinationNode {

  public NegateMatch(QNode left) {

    super(BooleanOperator.NOT, left, null);
  }
  
  @Override
  public <V> V accept(QNodeVisitor<V> visitor) {
  
   return visitor.visit(this);
  }
}
