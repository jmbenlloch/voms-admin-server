package it.infn.voms.admin.search.parser;

public enum BooleanExpressionTerm {

  SUSPENDED("suspended"),
  MEMBERSHIP_EXPIRED("membership_expired"),
  PENDING_AUP_REQUEST("pendingAupRequest");

  final String repr;

  private BooleanExpressionTerm(String r) {

    repr = r;
  }

  public String getRepr() {

    return repr;
  }

  public static BooleanExpressionTerm fromString(String s){
    if (s == null){
      return null;
    }
    
    for (BooleanExpressionTerm f: BooleanExpressionTerm.values()){
      if (s.trim().equals(f.repr)){
        return f;
      }
    }
    
    return null;
  }

}
