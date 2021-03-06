/**
 *
 * Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */

package org.jclouds.rackspace;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;

import org.jclouds.rackspace.functions.ParseAuthenticationResponseFromHeaders;
import org.jclouds.rackspace.reference.RackspaceHeaders;
import org.jclouds.rest.annotations.ResponseParser;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Provides access to Rackspace resources via their REST API.
 * <p/>
 * 
 * @see <a href="http://docs.rackspacecloud.com/servers/api/cs-devguide-latest.pdf" />
 * @author Adrian Cole
 */
@Path("/v" + RackspaceAuthAsyncClient.VERSION)
public interface RackspaceAuthAsyncClient {
   public static final String VERSION = "1.0";

   public interface AuthenticationResponse {
      @CloudFiles
      URI getStorageUrl();

      @CloudFilesCDN
      URI getCDNManagementUrl();

      @CloudServers
      URI getServerManagementUrl();

      @Authentication
      String getAuthToken();
   }

   @GET
   @Path("")
   @ResponseParser(ParseAuthenticationResponseFromHeaders.class)
   ListenableFuture<AuthenticationResponse> authenticate(@HeaderParam(RackspaceHeaders.AUTH_USER) String user,
            @HeaderParam(RackspaceHeaders.AUTH_KEY) String key);
}
