package it.infn.voms.admin.search.parser.node;


public interface QNodeVisitor<V> {
  
  public V visit(QNode n);
  
  public V visit(FieldMatch n);
  public V visit(NullMatch n);
  public V visit(NotNullMatch n);
  public V visit(StatusMatch n);
  public V visit(AttributeExpressionMatch n);
  
  public V visit(NegateMatch n);
  public V visit(ConjunctionMatch n);
  public V visit(DisjunctionMatch n);
  
  
}
