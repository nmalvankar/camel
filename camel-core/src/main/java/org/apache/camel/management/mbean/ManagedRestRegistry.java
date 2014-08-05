/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.management.mbean;

import java.util.List;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;

import org.apache.camel.CamelContext;
import org.apache.camel.api.management.ManagedResource;
import org.apache.camel.api.management.mbean.CamelOpenMBeanTypes;
import org.apache.camel.api.management.mbean.ManagedRestRegistryMBean;
import org.apache.camel.spi.RestRegistry;
import org.apache.camel.util.ObjectHelper;

/**
 *
 */
@ManagedResource(description = "Managed RestRegistry")
public class ManagedRestRegistry extends ManagedService implements ManagedRestRegistryMBean {

    private final RestRegistry registry;

    public ManagedRestRegistry(CamelContext context, RestRegistry registry) {
        super(context, registry);
        this.registry = registry;
    }

    public RestRegistry getRegistry() {
        return registry;
    }

    @Override
    public int getNumberOfRestServices() {
        return registry.size();
    }

    @Override
    public TabularData listRestServices() {
        try {
            TabularData answer = new TabularDataSupport(CamelOpenMBeanTypes.listRestServicesTabularType());
            List<RestRegistry.RestService> services = registry.listAllRestServices();
            for (RestRegistry.RestService entry : services) {
                CompositeType ct = CamelOpenMBeanTypes.listRestServicesCompositeType();
                String url = entry.getUrl();
                String method = entry.getMethod();
                String uriTemplate = entry.getUriTemplate();
                String consumes = entry.getConsumes();
                String produces = entry.getProduces();
                String state = entry.getState();
                CompositeData data = new CompositeDataSupport(ct, new String[]{"url", "method", "uriTemplate", "consumes", "produces", "state"},
                        new Object[]{url, method, uriTemplate, consumes, produces, state});
                answer.put(data);
            }
            return answer;
        } catch (Exception e) {
            throw ObjectHelper.wrapRuntimeCamelException(e);
        }
    }
}
