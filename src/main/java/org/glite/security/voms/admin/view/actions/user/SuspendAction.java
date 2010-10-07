/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.UserRestoredEvent;
import org.glite.security.voms.admin.event.user.UserSuspendedEvent;
import org.glite.security.voms.admin.operations.users.RestoreUserOperation;
import org.glite.security.voms.admin.operations.users.SuspendUserOperation;
import org.glite.security.voms.admin.persistence.model.VOMSUser.SuspensionReason;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( {
		@Result(name = BaseAction.SUCCESS, location = "/user/search.action", type = "redirect"),
		@Result(name = BaseAction.INPUT, location = "userSuspend") })
		
@InterceptorRef(value = "authenticatedStack", params = {
		"token.includeMethods", "execute" })
public class SuspendAction extends UserActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String suspensionReason;

	@Override
	public String execute() throws Exception {

		SuspensionReason r = SuspensionReason.OTHER;
		r.setMessage(getSuspensionReason());

		SuspendUserOperation.instance(getModel(), r).execute();

		return SUCCESS;
	}

	@Action("restore")
	public String restore() throws Exception {

		RestoreUserOperation.instance(getModel()).execute();

		EventManager.dispatch(new UserRestoredEvent(getModel()));

		return SUCCESS;
	}

	public String getSuspensionReason() {
		return suspensionReason;
	}

	public void setSuspensionReason(String suspensionReason) {
		this.suspensionReason = suspensionReason;
	}

}
