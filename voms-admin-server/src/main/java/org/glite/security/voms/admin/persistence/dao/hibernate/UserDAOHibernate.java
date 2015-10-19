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

import it.infn.voms.admin.search.parser.node.QNode;

import java.util.List;

import org.glite.security.voms.admin.persistence.dao.generic.UserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class UserDAOHibernate extends NamedEntityHibernateDAO<VOMSUser, Long>
  implements UserDAO {

  private Criteria buildHibernateQuery(QNode query) {

    Criteria crit = getSession()
      .createCriteria(VOMSUser.class)
      .createAlias("certificates", "cert")
      .createAlias("cert.ca", "iss")
      .createAlias("mappings", "m")
      .createAlias("m.group", "g")
      .createAlias("m.role", "r")
      .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
      .setComment("user-search-query");
    
    crit.add(query.accept(new CriterionBuildVisitor()));
    
    return crit;
  }

  @Override
  public List<VOMSUser> searchUsers(QNode query, int firstResult, int maxResults) {

    return buildHibernateQuery(query).list();
  }

}
