<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<voms:panel id="personalInformation" 
        title="Personal informations"
        panelClass="panel"
        headerClass="panelHeader"
        buttonClass="panelButton"
        titleClass="panelTitle"
        contentClass="panelContent">
      <s:form>
        <s:textfield name="name" disabled="true" label="Name" size="40" labelposition="top" cssClass="text"/>
        <s:textfield name="surname" disabled="true" label="Surname" size="40" cssClass="text"/>
        <s:textfield name="institution" disabled="true" label="Institution" size="40" cssClass="text"/>
        <s:textarea name="address" disabled="true" label="Address" rows="4" cols="30"   cssClass="text"/>
        <s:textfield name="phoneNumber" disabled="true" label="Phone" size="40"   cssClass="text"/>
        <s:textfield name="emailAddress" disabled="true" label="Email" size="40"   cssClass="text"/>
        <s:submit value="%{'Change personal information'}" disabled="true"/>
      </s:form>
</voms:panel>

<voms:panel id="userCerts" 
        title="Certificates"
        panelClass="panel"
        headerClass="panelHeader"
        buttonClass="panelButton"
        titleClass="panelTitle"
        contentClass="panelContent">
        
      <voms:authorized permission="CONTAINER_READ|CONTAINER_WRITE" context="vo">
        <s:url action="add-certificate" namespace="/user" var="addCertificateURL">
          <s:param name="userId" value="id"/>
        </s:url>
    
       <s:a href="%{#addCertificateURL}">Add certificate</s:a>
      </voms:authorized>
  

      <s:if test="certificates.empty">
        No certificates defined for this user.
      </s:if>
      <s:else>
        <voms:hasPermissions var="canSuspend" 
                    context="/${voName}" 
                    permission="CONTAINER_READ|MEMBERSHIP_READ|SUSPEND"/>
                    
        <table cellpadding="0" cellspacing="0">
          <tr>
            <th>Subject</th>
            <th>Issuer</th>
            <th>Added on</th>
            <th>Status</th>
          </tr>
          <s:iterator var="cert" value="certificates">
            <tr>
              <td class="dn">
                <voms:formatDN dn="${cert.subjectString}" fields="CN"/>
              </td>
              <td class="dn ca">
                  <voms:formatDN dn="${cert.ca.subjectString}" fields="CN"/>
              </td>
              <td>
                <s:property value="creationTime"/>
              </td>
              <td>
              
                    
                <s:if test="suspended">
                  Suspended! Reason:
                  <div class="suspensionReason">
                    <s:property value="suspensionReason"/>
                  </div>
                    
                  <s:if test="#attr.canSuspend">
                    <s:url action="restore-certificate" namespace="/user" var="restoreCertURL">
                      <s:param name="userId" value="%{model.id}"/>
                      <s:param name="certificateId" value="%{#cert.id}"/>
                    </s:url>
              
                    <s:a href="%{restoreCertURL}" cssClass="actionLink">
                      restore
                    </s:a>
                  </s:if>
                </s:if>
                <s:else>
                  active
                  <s:if test="#attr.canSuspend">
                    <s:url action="suspend-certificate" method="input" namespace="/user" var="suspendCertURL">
                      <s:param name="userId" value="%{model.id}"/>
                      <s:param name="certificateId" value="%{#cert.id}"/>
                    </s:url>
              
                    <s:a href="%{suspendCertURL}" cssClass="actionLink">
                      suspend
                    </s:a>
                  </s:if>
                </s:else>
              </td>
              
              
              <td>
                <s:if test="model.certificates.size > 1">
                  <s:form action="delete-certificate" namespace="/user">
                    <s:token/>
                    <s:hidden name="userId" value="%{model.id}"/>
                    <s:hidden name="certificateId" value="%{#cert.id}"/>
                    <s:submit value="%{'Delete'}"/>
                  </s:form>
                </s:if>
              </td>
            </tr>
          </s:iterator>
        </table>
      </s:else>
</voms:panel>

<voms:panel id="userMembership" 
        title="Membership information"
        panelClass="panel"
        headerClass="panelHeader"
        buttonClass="panelButton"
        titleClass="panelTitle"
        contentClass="panelContent">

      <s:set var="userId" scope="page" value="id"/>
      <voms:unsubscribedGroups var="unsubscribedGroups" userId="${userId}"/>
      <voms:unassignedRoleMap var="unassignedRoleMap" userId="${userId}"/>

      <s:if test="not #attr.unsubscribedGroups.empty">
        <div class="subscribeGroups">
          <s:form action="add-to-group" namespace="/user">
            <s:token/>
            <s:hidden name="userId" value="%{model.id}"/>
            <s:select list="#attr.unsubscribedGroups" listKey="id" listValue="name" name="groupId"/>
            <s:submit value="%{'Add to group'}"/>
          </s:form>
        </div>
      </s:if>
      
      <div class="membershipTab">
        <table cellpadding="0" cellspacing="0">
          <tr>
            <th>Group name</th>
            <th>Roles</th>
            <th></th>
          </tr>
          <s:iterator var="mapping" value="model.mappingsMap">
            <tr>
              <td>
                <s:property value="key"/>   
              </td>
              <td>
                <s:iterator var="role" value="value">
                  <div class="userRoleName">
                    <s:property value="name"/>
                  </div>
                  <div class="dismissRoleBox">
                    
                    <s:form action="dismiss-role" namespace="/user">
                      <s:token/>
                      <s:hidden name="userId" value="%{model.id}"/>
                      <s:hidden name="groupId" value="%{#mapping.key.id}"/>
                      <s:hidden name="roleId" value="%{#role.id}"/>
                      <s:submit value="%{'Dismiss role'}"/>
                    </s:form>
                    
                  </div>
                </s:iterator>
                
                <s:if test="%{not #attr.unassignedRoleMap[#mapping.key.id].isEmpty}">
                  
                  <div class="roleAssign">
                    <s:form action="assign-role" namespace="/user">
                      <s:token/>
                      <s:hidden name="userId" value="%{model.id}"/>
                      <s:hidden name="groupId" value="%{#mapping.key.id}"/>
                      <s:select list="#attr.unassignedRoleMap[#mapping.key.id]" listKey="id" listValue="name" name="roleId"/>
                      <s:submit value="%{'Assign role'}"/> 
                    </s:form>
                  </div>
                </s:if>
              </td>
              <td>
                <s:if test="not key.rootGroup">
                  <s:form action="remove-from-group" namespace="/user">
                    <s:hidden name="userId" value="%{model.id}"/>
                    <s:hidden name="groupId" value="%{#mapping.key.id}"/>
                    <s:submit value="%{'Remove from group'}"/>
                  </s:form>
                </s:if>
              </td>
            </tr>
          </s:iterator>
        </table>
      </div>
      </voms:panel>

<voms:panel id="userAttrs" 
        title="Generic attributes"
        panelClass="panel"
        headerClass="panelHeader"
        buttonClass="panelButton"
        titleClass="panelTitle"
        contentClass="panelContent">



  <voms:authorized permission="ATTRIBUTES_WRITE" context="vo">
  
    <s:if test="not #request.attributeClasses.empty">
      <div class="attributeCreationTab">
        <s:form action="set-attribute" namespace="/user">
          <s:token/>
          <s:hidden name="userId" value="%{model.id}"/>
          
          <table cellpadding="" cellspacing="">
            <tr>
              <td>
                <s:select name="attributeName" 
                  list="#request.attributeClasses" 
                  listKey="name" 
                  listValue="name" 
                  label="Attribute name"/>
              </td>
              </tr>
              
              <tr>
                <td>
                  <s:textarea label="Attribute value" name="attributeValue" rows="4" cols="30" value=""/>
                </td>
              </tr>
              <tr>
                <td>
                  <s:submit value="%{'Set attribute'}"/>
                </td>
              </tr>
          </table>       
        </s:form>
      </div>
    </s:if>
    <s:else>
      No attribute classes defined for this vo.
    </s:else>
  </voms:authorized>
    
  
  <voms:authorized permission="ATTRIBUTES_READ" context="vo">
    <s:if test="attributes.isEmpty">
      No attributes defined for this user
    </s:if>
    <s:else>
      <h4>Attribute list:</h4>
      <table class="table" cellpadding="0" cellspacing="0">
            
            <tr class="tableHeaderRow">
              <td>Attribute name</td>
              <td>Attribute value</td>
              <td colspan="2"/>
            </tr>
            
            <s:iterator var="attribute" value="attributes">
              
              <tr>  
                <td><s:property value="name"/></td>
                <td><s:property value="value"/></td>
                <td>
                  <voms:authorized permission="ATTRIBUTES_WRITE" context="vo">
                    <s:form action="delete-attribute" namespace="/user">
                      <s:token/>
                      <s:hidden name="userId" value="%{model.id}"/>
                      <s:hidden name="attributeName" value="%{#attribute.name}"/>
                      <s:submit value="%{'delete'}"/>
                    </s:form>
                  </voms:authorized>
                </td>
              </tr>
            </s:iterator>
      </table>
    </s:else>
  </voms:authorized>
</voms:panel>

