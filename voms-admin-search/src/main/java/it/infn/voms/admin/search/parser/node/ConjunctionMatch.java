package it.infn.voms.admin.search.parser.node;

import it.infn.voms.admin.search.parser.BooleanOperator;

public class ConjunctionMatch extends LogicalCombinationNode {

  public ConjunctionMatch(QNode left, QNode right) {

    super(BooleanOperator.AND, left, right);
  }

  @Override
  public <V> V accept(QNodeVisitor<V> visitor) {
    return visitor.visit(this);
  }
}
