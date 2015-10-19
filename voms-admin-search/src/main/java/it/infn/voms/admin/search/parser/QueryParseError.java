package it.infn.voms.admin.search.parser;

public class QueryParseError extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public QueryParseError() {

  }

  public QueryParseError(String message) {

    super(message);
  }

  public QueryParseError(String message, Throwable cause) {

    super(message, cause);

  }

}
