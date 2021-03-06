/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
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
package org.glite.security.voms.admin.persistence.model.attribute;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.service.attributes.AttributeClass;
import org.glite.security.voms.service.attributes.AttributeValue;

@Entity
@Table(name = "usr_attrs")
public class VOMSUserAttribute implements Serializable, GenericAttributeValue {

  /**
     * 
     */
  private static final long serialVersionUID = 1L;

  @Id
  @ManyToOne
  @JoinColumn(name="a_id")
  VOMSAttributeDescription attributeDescription;

  @Id
  @ManyToOne
  @JoinColumn(name="u_id")
  VOMSUser user;

  @Column(name = "a_value")
  String value;

  public VOMSUser getUser() {

    return user;
  }

  public void setUser(VOMSUser user) {

    this.user = user;
  }

  public VOMSUserAttribute() {

  }

  public VOMSUserAttribute(VOMSAttributeDescription desc, String attrValue) {

    attributeDescription = desc;
    value = attrValue;

  }

  public VOMSUserAttribute(VOMSAttributeDescription desc, String value,
    VOMSUser u) {

    this(desc, value);

    setUser(u);
  }

  public boolean equals(Object other) {

    if (this == other)
      return true;

    if (!(other instanceof VOMSUserAttribute))
      return false;

    if (other == null)
      return false;

    VOMSUserAttribute that = (VOMSUserAttribute) other;

    if (getUser().equals(that.getUser()))
      return getAttributeDescription().equals(that.getAttributeDescription());

    return false;
  }

  public int hashCode() {

    int result = 14;

    result = 29 * result + getAttributeDescription().hashCode();

    if (getUser() != null)
      result = 29 * result + getUser().hashCode();

    return result;

  }

  public static VOMSUserAttribute instance(VOMSAttributeDescription desc,
    String value, VOMSUser u) {

    return new VOMSUserAttribute(desc, value, u);
  }

  public static VOMSUserAttribute instance(VOMSAttributeDescription desc,
    String value) {

    return new VOMSUserAttribute(desc, value);
  }

  public static VOMSUserAttribute instance(String attrName, String attrDesc,
    String attrValue) {

    VOMSAttributeDescription desc = new VOMSAttributeDescription(attrName,
      attrDesc);
    VOMSUserAttribute instance = new VOMSUserAttribute(desc, attrValue);

    return instance;
  }

  public String toString() {

    return "(" + getName() + "," + value + ")";
  }

  public String getContext() {

    return null;
  }

  public String getValue() {

    return value;
  }

  public void setValue(String value) {

    this.value = value;
  }

  public String getName() {

    return attributeDescription.getName();
  }

  @Override
  public VOMSAttributeDescription getAttributeDescription() {

    return attributeDescription;
  }

  @Override
  public void setAttributeDescription(VOMSAttributeDescription desc) {

    attributeDescription = desc;
  }

  @Override
  public void setContext(String context) {

    // NO-OP
  }

  @Override
  public AttributeValue asAttributeValue() {

    AttributeClass aClass = getAttributeDescription().asAttributeClass();

    AttributeValue val = new AttributeValue();
    val.setAttributeClass(aClass);

    val.setContext(getContext());

    val.setValue(getValue());

    return val;
  }

}
