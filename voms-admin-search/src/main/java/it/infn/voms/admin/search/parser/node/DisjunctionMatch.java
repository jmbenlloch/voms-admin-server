package it.infn.voms.admin.search.parser.node;

import it.infn.voms.admin.search.parser.BooleanOperator;

public class DisjunctionMatch extends LogicalCombinationNode {

  public DisjunctionMatch(QNode left, QNode right) {

    super(BooleanOperator.OR, left, right);
  }

  @Override
  public <V> V accept(QNodeVisitor<V> visitor) {
    return visitor.visit(this);
  }
}
