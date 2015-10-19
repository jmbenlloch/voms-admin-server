package it.infn.voms.admin.search.parser.node;

import it.infn.voms.admin.search.parser.BooleanOperator;

public class LogicalCombinationNode extends QNode {

  final BooleanOperator op;

  public LogicalCombinationNode(BooleanOperator op, QNode left,
    QNode right) {

    super(left, right);
    this.op = op;
  }

  public BooleanOperator getOp() {

    return op;
  }

  @Override
  public String toString() {
    return op.getRepr();
  }
  
}
