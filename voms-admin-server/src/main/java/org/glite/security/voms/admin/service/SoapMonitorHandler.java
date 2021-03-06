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
package org.glite.security.voms.admin.service;

import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoapMonitorHandler extends BasicHandler {

  /**
     * 
     */
  private static final long serialVersionUID = 1L;
  private static final Logger log = LoggerFactory
    .getLogger(SoapMonitorHandler.class);

  public void invoke(MessageContext msgContext) throws AxisFault {

    Message msg = msgContext.getCurrentMessage();
    log.info("Current message: " + msg.getSOAPPartAsString());
  }

}
