package it.infn.voms.admin.search.parser.node;

public interface Visitable {

  public <V> V accept(QNodeVisitor<V> visitor);
}
