/**
 *
 * Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.vdc/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */

package org.jclouds.vcloud.xml;

import static org.jclouds.vcloud.util.Utils.cleanseAttributes;
import static org.jclouds.vcloud.util.Utils.newReferenceType;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jclouds.http.functions.ParseSax;
import org.jclouds.vcloud.domain.ReferenceType;
import org.jclouds.vcloud.domain.Status;
import org.jclouds.vcloud.domain.Task;
import org.jclouds.vcloud.domain.Vm;
import org.jclouds.vcloud.domain.internal.VmImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.google.common.collect.Lists;

/**
 * @author Adrian Cole
 */
public class VmHandler extends ParseSax.HandlerWithResult<Vm> {

   protected final TaskHandler taskHandler;

   @Inject
   public VmHandler(TaskHandler taskHandler) {
      this.taskHandler = taskHandler;
   }

   protected StringBuilder currentText = new StringBuilder();

   protected ReferenceType vm;
   protected Status status;
   protected ReferenceType vdc;
   protected String description;
   protected List<Task> tasks = Lists.newArrayList();
   protected String vAppScopedLocalId;

   private boolean inTasks;

   public Vm getResult() {
      return new VmImpl(vm.getName(), vm.getType(), vm.getHref(), status, vdc, description, tasks, vAppScopedLocalId);
   }

   @Override
   public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
      Map<String, String> attributes = cleanseAttributes(attrs);
      if (qName.equals("Tasks")) {
         inTasks = true;
      }
      if (inTasks) {
         taskHandler.startElement(uri, localName, qName, attrs);
      } else if (qName.equals("Vm")) {
         vm = newReferenceType(attributes);
         String status = attributes.get("status");
         if (status != null)
            this.status = Status.fromValue(Integer.parseInt(status));
      } else if (qName.equals("Link") && "up".equals(attributes.get("rel"))) {
         vdc = newReferenceType(attributes);
      }
   }

   public void endElement(String uri, String name, String qName) {
      if (qName.equals("Tasks")) {
         inTasks = false;
      }
      if (inTasks) {
         taskHandler.endElement(uri, name, qName);
         if (qName.equals("Task")) {
            this.tasks.add(taskHandler.getResult());
         }
      } else if (qName.equals("Description")) {
         description = currentOrNull();
      } else if (qName.equals("VAppScopedLocalId")) {
         vAppScopedLocalId = currentOrNull();
      }
      currentText = new StringBuilder();
   }

   public void characters(char ch[], int start, int length) {
      currentText.append(ch, start, length);
   }

   protected String currentOrNull() {
      String returnVal = currentText.toString().trim();
      return returnVal.equals("") ? null : returnVal;
   }
}