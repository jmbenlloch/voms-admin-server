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
package org.glite.security.voms.admin.apiv2;

import java.util.List;

import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

public class UserSearchTO {

  String query;

  int firstResult;
  int maxResults;

  List<VOMSUserTO> results;

  public UserSearchTO() {

  }

  @RequiredStringValidator(type = ValidatorType.FIELD,
    message = "Please provide a query to search the VOMS user database")
  public String getQuery() {

    return query;
  }

  public void setQuery(String query) {

    this.query = query;
  }
  
  @IntRangeFieldValidator(type = ValidatorType.FIELD,
    min="0", message="Please set a positive first result index")
  public int getFirstResult() {

    return firstResult;
  }

  public void setFirstResult(int firstResult) {

    this.firstResult = firstResult;
  }
  
  public int getMaxResults() {

    return maxResults;
  }

  public void setMaxResults(int maxResults) {

    this.maxResults = maxResults;
  }

  public List<VOMSUserTO> getResults() {

    return results;
  }

  public void setResults(List<VOMSUserTO> results) {

    this.results = results;
  }

}
