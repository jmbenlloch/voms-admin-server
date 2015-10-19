package it.infn.voms.admin.search.parser;


public class Parser {

  
  private Parser() {}

  
  public static QueryParser newParser(){
    return QueryParserImpl.buildParser();
  }
}
