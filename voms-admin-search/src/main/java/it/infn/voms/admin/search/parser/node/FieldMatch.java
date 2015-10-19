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

import it.infn.voms.admin.search.parser.FieldName;
import it.infn.voms.admin.search.parser.MatchOperator;

public class FieldMatch extends QNode {

  final FieldName fieldName;
  final MatchOperator matchOperator;
  final String value;

  protected FieldMatch(FieldName f, MatchOperator o, String value) {

    this.fieldName = f;
    this.matchOperator = o;
    this.value = value.substring(1, value.length() - 1);
  }

  public FieldName getFieldName() {

    return fieldName;
  }

  public MatchOperator getMatchOperator() {

    return matchOperator;
  }
  
  public String getValue() {
  
    return value;
  }

  @Override
  public String toString() {

    return String.format("%s %s '%s'", fieldName.getRepr(),
      matchOperator.getRepr(), value);
  }
  
  @Override
  public <V> V accept(QNodeVisitor<V> visitor) {
    return visitor.visit(this);
  }

}
