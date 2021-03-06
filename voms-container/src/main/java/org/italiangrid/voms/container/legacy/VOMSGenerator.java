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
package org.italiangrid.voms.container.legacy;

import java.io.IOException;

import org.eclipse.jetty.http.HttpGenerator;
import org.eclipse.jetty.io.Buffers;
import org.eclipse.jetty.io.EndPoint;

public class VOMSGenerator extends HttpGenerator {

  private final VOMSConnection _connection;

  public VOMSGenerator(VOMSConnection conn, Buffers buffers, EndPoint io) {

    super(buffers, io);
    _connection = conn;
  }

  protected boolean isLegacyRequest() {

    return (_connection.getRequest().getHeader(
      LegacyHTTPHeader.LEGACY_REQUEST_HEADER.getHeaderName()) != null);
  }

  @Override
  public int flushBuffer() throws IOException {

    if (!isLegacyRequest()) {
      return super.flushBuffer();
    }

    int numWritten = _endp.flush(_buffer);
    _state = STATE_END;
    _endp.shutdownOutput();

    return numWritten;
  }

}
