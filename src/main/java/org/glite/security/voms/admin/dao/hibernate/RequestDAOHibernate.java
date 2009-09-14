package org.glite.security.voms.admin.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bouncycastle.ocsp.Req;
import org.glite.security.voms.admin.common.VOMSException;
import org.glite.security.voms.admin.dao.generic.RequestDAO;
import org.glite.security.voms.admin.database.AlreadyMemberException;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.model.Certificate;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.request.CertificateRequest;
import org.glite.security.voms.admin.model.request.GroupMembershipRequest;
import org.glite.security.voms.admin.model.request.MembershipRemovalRequest;
import org.glite.security.voms.admin.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.model.request.Request;
import org.glite.security.voms.admin.model.request.RequesterInfo;
import org.glite.security.voms.admin.model.request.RoleMembershipRequest;
import org.glite.security.voms.admin.model.request.Request.StatusFlag;
import org.glite.security.voms.admin.model.task.Task.TaskStatus;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class RequestDAOHibernate extends GenericHibernateDAO<Request, Long>
		implements RequestDAO {

	public CertificateRequest createCertificateRequest() {

		return null;
	}

	public CertificateRequest createCertificateRequest(VOMSUser u,
			String certificateSubject, String certificateIssuer,
			Date expirationDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public GroupMembershipRequest createGroupMembershipRequest(VOMSUser usr,
			VOMSGroup group, Date expirationDate) {
		
		if (usr.isMember(group))
			throw new VOMSException("User '"+usr+"' is already member of group '"+group+"'!");
		
		if (userHasPendingGroupMembershipRequest(usr, group))
			throw new AlreadyMemberException("User '"+usr+"' has a pending group membership request for group '"+group+"'!");
		
		GroupMembershipRequest req = new GroupMembershipRequest();
		req.setStatus(StatusFlag.SUBMITTED);
		req.setRequesterInfo(RequesterInfo.fromVOUser(usr));
		req.setCreationDate(new Date());
		req.setExpirationDate(expirationDate);
		
		req.setGroupName(group.getName());
		
		makePersistent(req);
		
		return req;
	}

	public MembershipRemovalRequest createMembershipRemovalRequest(
			VOMSUser usr, String reason, Date expirationDate) {
		
		MembershipRemovalRequest req = new MembershipRemovalRequest();
		req.setStatus(StatusFlag.SUBMITTED);
		req.setRequesterInfo(RequesterInfo.fromVOUser(usr));
		req.setCreationDate(new Date());
		req.setExpirationDate(expirationDate);
		req.setReason(reason);
		
		makePersistent(req);
		
		return req;
	}

	public RoleMembershipRequest createRoleMembershipRequest(VOMSUser usr,
			VOMSGroup group, VOMSRole r, Date expirationDate) {
		
		if (usr.hasRole(group, r))
			throw new AlreadyMemberException("User '"+usr+"' already has role '"+r.getName()+"' in group '"+group+"'!");
		
		RoleMembershipRequest req = new RoleMembershipRequest();
		req.setStatus(StatusFlag.SUBMITTED);
		req.setRequesterInfo(RequesterInfo.fromVOUser(usr));
		req.setCreationDate(new Date());
		req.setExpirationDate(expirationDate);
		
		req.setGroupName(group.getName());
		req.setRoleName(r.getName());
		
		makePersistent(req);
		
		return req;
	}
	
	public NewVOMembershipRequest createVOMembershipRequest(
			RequesterInfo requester, Date expirationDate) {

		NewVOMembershipRequest req = new NewVOMembershipRequest();

		req.setStatus(StatusFlag.SUBMITTED);
		req.setRequesterInfo(requester);
		req.setCreationDate(new Date());
		req.setExpirationDate(expirationDate);

		req.setConfirmId(UUID.randomUUID().toString());
		makePersistent(req);

		return req;
	}

	public void deleteRequestFromUser(VOMSUser u) {
		List<Request> userReqs = findRequestsFromUser(u);
		
		for (Request r: userReqs)	
			makeTransient(r);
		
		
	}
	
	public NewVOMembershipRequest findActiveVOMembershipRequest(
			RequesterInfo requester) {
		Criteria crit = getSession().createCriteria(
				NewVOMembershipRequest.class);

		crit.add(Restrictions.ne("status", StatusFlag.APPROVED)).add(
				Restrictions.ne("status", StatusFlag.REJECTED)).createCriteria(
				"requesterInfo").add(
				Restrictions.eq("certificateSubject", requester
						.getCertificateSubject())).add(
				Restrictions.eq("certificateIssuer", requester
						.getCertificateIssuer())).add(
				Restrictions.eq("emailAddress", requester.getEmailAddress()));

		return (NewVOMembershipRequest) crit.uniqueResult();

	}
	
	
	public List<NewVOMembershipRequest> findConfirmedVOMembershipRequests() {

		Criteria crit = getSession().createCriteria(
				NewVOMembershipRequest.class);

		crit.add(Restrictions.eq("status", StatusFlag.CONFIRMED));

		return crit.list();

	}

	public List<NewVOMembershipRequest> findExpiredVOMembershipRequests() {
		Criteria crit = getSession().createCriteria(NewVOMembershipRequest.class);
		
		 Date now = new Date();
		 crit.add(Restrictions.lt("expirationDate", now));
		 
		return crit.list();
	}
	
	public List<CertificateRequest> findPendingCertificateRequests(){
		Criteria crit = getSession().createCriteria(CertificateRequest.class);
		crit.add(Restrictions.eq("status", StatusFlag.SUBMITTED));
		
		return crit.list();
		
	}

	public List<GroupMembershipRequest> findPendingGroupMembershipRequests() {
		
		Criteria crit = getSession().createCriteria(GroupMembershipRequest.class);
		
		crit.add(Restrictions.eq("status", StatusFlag.SUBMITTED));
		
		return crit.list();
	}

	
	public List<MembershipRemovalRequest> findPendingMembershipRemovalRequests() {
		Criteria crit = getSession().createCriteria(MembershipRemovalRequest.class);
		
		crit.add(Restrictions.eq("status", StatusFlag.SUBMITTED));
		
		return crit.list();
	}
	
	public List<Request> findPendingRequests() {
		List<Request> result = new ArrayList<Request>();
		
		result.addAll(findConfirmedVOMembershipRequests());
		result.addAll(findPendingGroupMembershipRequests());
		result.addAll(findPendingRoleMembershipRequests());
		result.addAll(findPendingCertificateRequests());
		result.addAll(findPendingMembershipRemovalRequests());
		
		return result;
	}

	public List<RoleMembershipRequest> findPendingRoleMembershipRequests() {
		Criteria crit = getSession().createCriteria(RoleMembershipRequest.class);
		
		crit.add(Restrictions.eq("status", StatusFlag.SUBMITTED));
		return crit.list();
	}
	

	public List<CertificateRequest> findPendingUserCertificateRequests(VOMSUser u){
		
		Criteria crit = getSession().createCriteria(CertificateRequest.class);
		
		crit.add(Restrictions.disjunction().
				add(Restrictions.eq("status", StatusFlag.SUBMITTED)).
				add(Restrictions.eq("status", StatusFlag.PENDING))).createCriteria("requesterInfo")
				.add(getDnEqualityCheckConstraints(u));
		
		return crit.list();
	}
	
	public List<GroupMembershipRequest> findPendingUserGroupMembershipRequests(VOMSUser u){
		
		Criteria crit = getSession().createCriteria(
				GroupMembershipRequest.class);
		
		
		crit.add(Restrictions.disjunction().
				add(Restrictions.eq("status", StatusFlag.SUBMITTED)).
				add(Restrictions.eq("status", StatusFlag.PENDING))).createCriteria("requesterInfo")
				.add(getDnEqualityCheckConstraints(u));
				
		
		return crit.list();
	}

	public List<MembershipRemovalRequest> findPendingUserMembershipRemovalRequests(
			VOMSUser u) {
		
		Criteria crit = getSession().createCriteria(MembershipRemovalRequest.class);
		
		crit.add(Restrictions.disjunction().
				add(Restrictions.eq("status", StatusFlag.SUBMITTED)).
				add(Restrictions.eq("status", StatusFlag.PENDING))).createCriteria("requesterInfo")
				.add(getDnEqualityCheckConstraints(u));
		
		return crit.list();
	}

	public List<RoleMembershipRequest> findPendingUserRoleMembershipRequests(VOMSUser u){
		Criteria crit = getSession().createCriteria(RoleMembershipRequest.class);
		crit.add(Restrictions.disjunction().
				add(Restrictions.eq("status", StatusFlag.SUBMITTED)).
				add(Restrictions.eq("status", StatusFlag.PENDING))).createCriteria("requesterInfo")
				.add(getDnEqualityCheckConstraints(u));
		
		return crit.list();
	}

	public List<NewVOMembershipRequest> findRejectedVOMembershipRequests() {
		Criteria crit = getSession().createCriteria(NewVOMembershipRequest.class);
		crit.add(Restrictions.eq("status", StatusFlag.REJECTED));
		 
		return crit.list();
	}

	public List<Request> findRequestsFromUser(VOMSUser u) {
		
		Criteria crit = getSession().createCriteria(Request.class);
		crit.addOrder(Order.desc("creationDate"));
		crit.createCriteria("requesterInfo").add(getDnEqualityCheckConstraints(u));
		
		return crit.list();
	}

	protected Disjunction getDnEqualityCheckConstraints(VOMSUser u){
		
		Disjunction dnEqualityChecks = Restrictions.disjunction();
		
		for (Certificate c: u.getCertificates())
			dnEqualityChecks.add(Restrictions.eq("certificateSubject", c.getSubjectString()));
		
		return dnEqualityChecks;
		
	}

	public boolean userHasPendingCertificateRequest(VOMSUser u,
			String certificateSubject, String certificateIssuer) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean userHasPendingGroupMembershipRequest(VOMSUser u, VOMSGroup g){
		
		
		Criteria crit = getSession().createCriteria(
				GroupMembershipRequest.class);
		
		
		crit.add(Restrictions.eq("groupName", g.getName()));
		
		crit.add(Restrictions.disjunction().
				add(Restrictions.eq("status", StatusFlag.SUBMITTED)).
				add(Restrictions.eq("status", StatusFlag.PENDING))).createCriteria("requesterInfo").add(getDnEqualityCheckConstraints(u));
				
		List<GroupMembershipRequest> reqs= crit.list();
		
		if (reqs ==  null || reqs.isEmpty())
			return false;
		
		
		return true;
	}

	public boolean userHasPendingMembershipRemovalRequest(VOMSUser u) {
		Criteria crit = getSession().createCriteria(MembershipRemovalRequest.class);
		
		crit.add(Restrictions.disjunction().
				add(Restrictions.eq("status", StatusFlag.SUBMITTED)).
				add(Restrictions.eq("status", StatusFlag.PENDING))).createCriteria("requesterInfo").add(getDnEqualityCheckConstraints(u));
		
		List<MembershipRemovalRequest> reqs = crit.list();
		
		if (reqs ==  null || reqs.isEmpty())
			return false;
		
		return true;
	}

	public boolean userHasPendingRoleMembershipRequest(VOMSUser u, VOMSGroup g,
			VOMSRole r) {
		
		Criteria crit = getSession().createCriteria(
				RoleMembershipRequest.class);
		
		crit.add(Restrictions.eq("groupName", g.getName()));
		crit.add(Restrictions.eq("roleName", r.getName()));
		
		crit.add(Restrictions.disjunction().
				add(Restrictions.eq("status", StatusFlag.SUBMITTED)).
				add(Restrictions.eq("status", StatusFlag.PENDING))).createCriteria("requesterInfo").add(getDnEqualityCheckConstraints(u));
		
		List<RoleMembershipRequest> reqs = crit.list();
		
		if (reqs ==  null || reqs.isEmpty())
			return false;
		
		
		return true;
	}
	

	

}
