package it.infn.voms.admin.search.parser.node;


public interface BinTreeNode<T> {
  
  public T getParent();
  
  public T left();
  
  public T right();

}
