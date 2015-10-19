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
package org.glite.security.voms.admin.operations.users;

import it.infn.voms.admin.search.parser.Parser;
import it.infn.voms.admin.search.parser.QueryParser;
import it.infn.voms.admin.search.parser.node.QNode;

import java.util.List;

import org.glite.security.voms.admin.apiv2.UserSearchTO;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.UserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class DynaSearchOperation extends BaseVomsOperation<List<VOMSUser>> {

  final UserSearchTO searchData;

  public DynaSearchOperation(UserSearchTO searchData) {

    this.searchData = searchData;
  }

  @Override
  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
      .getContainerReadPermission().setMembershipReadPermission());
  }

  private QNode parseQuery(){
    QueryParser p = Parser.newParser();
    QNode q = p.parse(searchData.getQuery());
    return q;
  }
  @Override
  protected List<VOMSUser> doExecute() {

    UserDAO dao = DAOFactory.instance().getUserDAO();
    return dao.searchUsers(parseQuery(), 
      searchData.getFirstResult(), 
      searchData.getMaxResults());
  }

}
