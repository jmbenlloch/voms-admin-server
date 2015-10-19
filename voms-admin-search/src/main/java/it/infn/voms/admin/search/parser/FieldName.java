/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
