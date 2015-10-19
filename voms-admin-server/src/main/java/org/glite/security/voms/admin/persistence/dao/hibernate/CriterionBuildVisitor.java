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
package org.glite.security.voms.admin.persistence.dao.hibernate;

import it.infn.voms.admin.search.parser.node.AttributeExpressionMatch;
import it.infn.voms.admin.search.parser.node.ConjunctionMatch;
import it.infn.voms.admin.search.parser.node.DisjunctionMatch;
import it.infn.voms.admin.search.parser.node.FieldMatch;
import it.infn.voms.admin.search.parser.node.NegateMatch;
import it.infn.voms.admin.search.parser.node.NotNullMatch;
import it.infn.voms.admin.search.parser.node.NullMatch;
import it.infn.voms.admin.search.parser.node.QNode;
import it.infn.voms.admin.search.parser.node.QNodeVisitor;
import it.infn.voms.admin.search.parser.node.StatusMatch;

import org.apache.commons.lang.NotImplementedException;
import org.glite.security.voms.admin.persistence.model.task.Task;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.italiangrid.voms.util.VOMSFQANNamingScheme;

public class CriterionBuildVisitor implements QNodeVisitor<Criterion> {

  public CriterionBuildVisitor() {

  }

  @Override
  public Criterion visit(QNode n) {

    throw new IllegalStateException();
  }

  private String rootScope(String s) {

    return String.format("this.%s", s);
  }

  @Override
  public Criterion visit(FieldMatch n) {

    switch (n.getMatchOperator()) {

    case EQUAL:
      return Restrictions
        .eq(rootScope(n.getFieldName().getHql()), n.getValue());

    case NOT_EQUAL:
      return Restrictions
        .ne(rootScope(n.getFieldName().getHql()), n.getValue());

    case LIKE:
      return Restrictions.ilike(rootScope(n.getFieldName().getHql()),
        n.getValue(), MatchMode.ANYWHERE);

    default:
      throw new NotImplementedException();
    }

  }

  @Override
  public Criterion visit(NegateMatch n) {

    return Restrictions.not(n.left().accept(this));

  }

  @Override
  public Criterion visit(NullMatch n) {

    return Restrictions.isNull(rootScope(n.getFieldName().getHql()));
  }

  @Override
  public Criterion visit(NotNullMatch n) {

    return Restrictions.isNotNull(rootScope(n.getFieldName().getHql()));
  }

  @Override
  public Criterion visit(StatusMatch n) {

    switch (n.getStatusFlag()) {
    case SUSPENDED:
      return Restrictions.eq(rootScope("suspended"), Boolean.TRUE);

    case PENDING_AUP_REQUEST:
      return Restrictions.ne("t.status", Task.TaskStatus.COMPLETED);

    default:
      throw new NotImplementedException();
    }
  }

  @Override
  public Criterion visit(AttributeExpressionMatch n) {

    switch (n.getAttributeTerm()) {
    case IN_GROUP:

      return Restrictions.eq("g.name", n.getValue());

    case WITH_ROLE:
      String groupName = VOMSFQANNamingScheme.getGroupName(n.getValue());
      String roleName = VOMSFQANNamingScheme.getRoleName(n.getValue());

      return Restrictions.and(Restrictions.eq("r.name", roleName),
        Restrictions.eq("g.name", groupName));

    default:
      throw new NotImplementedException();
    }
  }

  @Override
  public Criterion visit(ConjunctionMatch n) {

    return Restrictions.and(n.left().accept(this), n.right().accept(this));

  }

  @Override
  public Criterion visit(DisjunctionMatch n) {

    return Restrictions.or(n.left().accept(this), n.right().accept(this));
  }

}
