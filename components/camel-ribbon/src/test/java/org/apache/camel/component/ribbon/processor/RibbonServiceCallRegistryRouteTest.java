/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.ribbon.processor;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.model.ServiceCallConfigurationDefinition;

public class RibbonServiceCallRegistryRouteTest extends RibbonServiceCallRouteTest {

    private JndiRegistry registry;

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        registry = super.createRegistry();
        return registry;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // setup a static ribbon server list with these 2 servers to start with
                RibbonServiceCallStaticServerListStrategy servers = new RibbonServiceCallStaticServerListStrategy();
                servers.addServer("localhost", 9090);
                servers.addServer("localhost", 9091);

                // configure camel service call
                ServiceCallConfigurationDefinition config = new ServiceCallConfigurationDefinition();
                config.setServerListStrategy(servers);

                // add the config to the registry so service call can use it
                registry.bind("myConfig", config);

                from("direct:start")
                        .serviceCall("myService")
                        .to("mock:result");

                from("jetty:http://localhost:9090")
                    .to("mock:9090")
                    .transform().constant("9090");

                from("jetty:http://localhost:9091")
                    .to("mock:9091")
                    .transform().constant("9091");
            }
        };
    }
}

