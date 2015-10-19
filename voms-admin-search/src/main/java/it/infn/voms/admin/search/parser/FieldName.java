package it.infn.voms.admin.search.parser;

public enum FieldName {

  ID("id"),
  ORGDB_ID("hr_id", "orgDbId"),
  NAME("name"),
  SURNAME("surname"),
  DN("dn", "cert.certificateSubject"),
  CA("ca", "iss.certificateSubject"),
  ADDRESS("address"),
  EMAIL_ADDRESS("email", "emailAddress"),
  PHONE_NUMBER("phone", "phoneNumber"),
  INSTITUTION("institution"),
  CREATION_TIME("creation_time"),
  EXPIRATION_TIME("expiration_time", "endTime"), ;

  final String repr;
  final String hql;

  private FieldName(String r, String h) {

    repr = r;
    hql = h;
  }

  private FieldName(String r) {

    this(r, r);
  }

  public String getRepr() {

    return repr;
  }

  public String getHql() {

    return hql;
  }

  public static FieldName fromString(String fn) {

    if (fn != null) {
      for (FieldName f : FieldName.values()) {
        if (fn.trim().equals(f.repr)) {
          return f;
        }
      }
    }

    return null;
  }
}
