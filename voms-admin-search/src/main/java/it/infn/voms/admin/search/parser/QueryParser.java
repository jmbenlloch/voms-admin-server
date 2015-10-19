package it.infn.voms.admin.search.parser;

import it.infn.voms.admin.search.parser.node.QNode;

public interface QueryParser {

  public QNode parse(String input);
}
