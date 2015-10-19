package it.infn.voms.admin.search.parser.node;

public abstract class QNode implements BinTreeNode<QNode>, Visitable {

  private final QNode left, right;
  private QNode parent;

  protected QNode(QNode left, QNode right) {

    if (left != null) {
      left.setParent(this);
    }
    if (right != null) {
      right.setParent(null);
    }

    this.left = left;
    this.right = right;

  }

  protected QNode() {

    this(null, null);
  }

  protected QNode(QNode left) {

    this(left, null);
  }

  @Override
  public QNode left() {

    return left;
  }

  @Override
  public QNode right() {

    return right;
  }

  @Override
  public QNode getParent() {

    return parent;
  }

  protected void setParent(QNode parent) {

    this.parent = parent;
  }

  @Override
  public <V> V accept(QNodeVisitor<V> visitor) {

    throw new IllegalArgumentException();
  }
}
