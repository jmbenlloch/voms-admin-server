package it.infn.voms.admin.search.parser;

public enum MatchOperator {

  EQUAL("="),
  NOT_EQUAL("!="),
  LIKE("like"),
  LT("<"),
  LE("<="),
  GT(">"),
  GE(">="),
  IS("is"),
  IS_NULL("is null"),
  IS_NOT_NULL("is not null");

  final String repr;

  private MatchOperator(String r) {

    repr = r;
  }

  public String getRepr() {

    return repr;
  }

  public static MatchOperator fromString(String op) {

    if (op != null) {
      for (MatchOperator m : MatchOperator.values()) {
        if (op.trim().equals(m.repr)) {
          return m;
        }
      }
    }

    return null;
  }
}
