package it.infn.voms.admin.search.parser;

public enum BooleanOperator {

  AND("and"),
  OR("or"),
  NOT("not");

  String repr;

  private BooleanOperator(String r) {

    repr = r;
  }

  public static BooleanOperator fromString(String s) {

    if (s != null) {
      if (s.trim().equals("and")) {
        return AND;
      } else if (s.trim().equals("or")) {
        return OR;
      }else if (s.trim().equals("not")) {
        return NOT;
      }
    }
    return null;
  }
  
  
  public String getRepr() {

    return repr;
  }
}
