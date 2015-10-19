package it.infn.voms.admin.search.parser;

public enum AttributeExpressionTerm {

  IN_GROUP("in_group"),
  WITH_ROLE("with_role"),
  WITH_ATTRIBUTE("with_attribute");

  final String repr;

  private AttributeExpressionTerm(String r) {

    repr = r;

  }

  public String getRepr() {

    return repr;
  }

  public static AttributeExpressionTerm fromString(String s){
    if (s == null){
      return null;
    }
    
    for (AttributeExpressionTerm a: AttributeExpressionTerm.values()){
      if (s.trim().equals(a.repr)){
        return a;
      }
    }
    
    return null;
  }
}
