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

package org.jclouds.elastichosts.functions;

import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Singleton;

import org.jclouds.elastichosts.domain.ClaimType;
import org.jclouds.elastichosts.domain.DriveInfo;
import org.jclouds.elastichosts.domain.DriveStatus;
import org.jclouds.elastichosts.domain.DriveType;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

/**
 * 
 * @author Adrian Cole
 */
@Singleton
public class MapToDriveInfo implements Function<Map<String, String>, DriveInfo> {
   @Override
   public DriveInfo apply(Map<String, String> from) {
      if (from.size() == 0)
         return null;
      DriveInfo.Builder builder = new DriveInfo.Builder();
      if (from.containsKey("status"))
         builder.status(DriveStatus.fromValue(from.get("status")));
      if (from.containsKey("use"))
         builder.use(Splitter.on(',').split(from.get("use")));
      builder.name(from.get("name"));
      if (from.containsKey("bits"))
         builder.bits(new Integer(from.get("bits")));
      if (from.containsKey("url"))
         builder.url(URI.create(from.get("url")));
      if (from.containsKey("read:bytes"))
         builder.readBytes(new Long(from.get("read:bytes")));
      if (from.containsKey("read:requests"))
         builder.readRequests(new Long(from.get("read:requests")));
      builder.user(from.get("user"));
      builder.encryptionCipher(from.get("encryption:cipher"));
      builder.encryptionKey(from.get("encryption:key"));
      builder.description(from.get("description"));
      builder.uuid(from.get("drive"));
      builder.installNotes(from.get("install_notes"));
      builder.os(from.get("os"));
      if (from.containsKey("write:bytes"))
         builder.writeBytes(new Long(from.get("write:bytes")));
      if (from.containsKey("write:requests"))
         builder.writeRequests(new Long(from.get("write:requests")));
      if (from.containsKey("claim:type"))
         builder.claimType(ClaimType.fromValue(from.get("claim:type")));
      if (from.containsKey("claimed"))
         builder.claimed(Splitter.on(' ').split(from.get("claimed")));
      if (from.containsKey("drive_type"))
         builder.driveType(Splitter.on(',').split(from.get("drive_type")));
      if (from.containsKey("autoexpanding"))
         builder.autoexpanding(new Boolean(from.get("autoexpanding")));
      if (from.containsKey("readers"))
         builder.readers(Splitter.on(' ').split(from.get("readers")));
      if (from.containsKey("free"))
         builder.free(new Boolean(from.get("free")));
      if (from.containsKey("type"))
         builder.type(DriveType.fromValue(from.get("type")));
      if (from.containsKey("size"))
         builder.size(new Long(from.get("size")));
      Map<String, String> metadata = Maps.newLinkedHashMap();
      for (Entry<String, String> entry : from.entrySet()) {
         if (entry.getKey().startsWith("user:"))
            metadata.put(entry.getKey().substring(entry.getKey().indexOf(':') + 1), entry.getValue());
      }
      builder.userMetadata(metadata);
      return builder.build();
   }
}