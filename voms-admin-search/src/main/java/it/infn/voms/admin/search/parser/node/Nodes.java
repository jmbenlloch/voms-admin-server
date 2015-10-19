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
package it.infn.voms.admin.search.parser.node;

import it.infn.voms.admin.search.parser.AttributeExpressionTerm;
import it.infn.voms.admin.search.parser.BooleanExpressionTerm;
import it.infn.voms.admin.search.parser.FieldName;
import it.infn.voms.admin.search.parser.MatchOperator;

public class Nodes {

  private Nodes() {

  }

  public static QNode fieldMatch(String fieldName, String operator, String value) {

    return new FieldMatch(FieldName.fromString(fieldName),
      MatchOperator.fromString(operator), value);
  }

  public static QNode negate(QNode child) {

    return new NegateMatch(child);
  }

  public static QNode conjunction(QNode left, QNode right) {

    return new ConjunctionMatch(left, right);
  }

  public static QNode disjunction(QNode left, QNode right) {

    return new DisjunctionMatch(left, right);
  }

  public static QNode nullMatch(String field) {

    return new NullMatch(FieldName.fromString(field));
  }

  public static QNode notNullMatch(String field) {

    return new NotNullMatch(FieldName.fromString(field));
  }

  public static QNode statusMatch(String statusFlag, Boolean value) {

    return new StatusMatch(BooleanExpressionTerm.fromString(statusFlag), value);
  }

  public static QNode attributeExpressionMatch(String ae, String value) {

    return new AttributeExpressionMatch(AttributeExpressionTerm.fromString(ae),
      value);
  }
}
