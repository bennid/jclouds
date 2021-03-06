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

package org.jclouds.servermanager.compute;

import java.util.List;
import java.util.Properties;

import org.jclouds.compute.ComputeServiceAdapter;
import org.jclouds.compute.StandaloneComputeServiceContextBuilder;
import org.jclouds.compute.config.StandaloneComputeServiceContextModule;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.suppliers.DefaultLocationSupplier;
import org.jclouds.domain.Location;
import org.jclouds.servermanager.Datacenter;
import org.jclouds.servermanager.Hardware;
import org.jclouds.servermanager.Image;
import org.jclouds.servermanager.Server;
import org.jclouds.servermanager.compute.functions.DatacenterToLocation;
import org.jclouds.servermanager.compute.functions.ServerManagerHardwareToHardware;
import org.jclouds.servermanager.compute.functions.ServerManagerImageToImage;
import org.jclouds.servermanager.compute.functions.ServerToNodeMetadata;
import org.jclouds.servermanager.compute.strategy.ServerManagerComputeServiceAdapter;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;

/**
 * 
 * @author Adrian Cole
 */
public class ServerManagerComputeServiceContextBuilder extends StandaloneComputeServiceContextBuilder {

   public ServerManagerComputeServiceContextBuilder(Properties props) {
      super(props);
   }

   @Override
   protected void addContextModule(List<Module> modules) {
      modules.add(createContextModule());
   }

   public static StandaloneComputeServiceContextModule<Server, Hardware, Image, Datacenter> createContextModule() {
      return new StandaloneComputeServiceContextModule<Server, Hardware, Image, Datacenter>() {

         @Override
         protected void configure() {
            super.configure();
            bind(new TypeLiteral<ComputeServiceAdapter<Server, Hardware, Image, Datacenter>>() {
            }).to(ServerManagerComputeServiceAdapter.class);
            bind(new TypeLiteral<Supplier<Location>>() {
            }).to(DefaultLocationSupplier.class);
            bind(new TypeLiteral<Function<Server, NodeMetadata>>() {
            }).to(ServerToNodeMetadata.class);
            bind(new TypeLiteral<Function<Image, org.jclouds.compute.domain.Image>>() {
            }).to(ServerManagerImageToImage.class);
            bind(new TypeLiteral<Function<Hardware, org.jclouds.compute.domain.Hardware>>() {
            }).to(ServerManagerHardwareToHardware.class);
            bind(new TypeLiteral<Function<Datacenter, Location>>() {
            }).to(DatacenterToLocation.class);
         }

      };
   }

}
