package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.day.cq.commons.jcr.JcrConstants.NT_UNSTRUCTURED;

public interface DatasourceAware {

    default DataSource buildDataSource(Map<String, String> properties, SlingHttpServletRequest request) {
        final var vmResources = new ArrayList<Resource>();

        for (final var entry : properties.entrySet()) {
            ValueMap vm = new ValueMapDecorator(new HashMap<>());
            vm.put("text", entry.getValue());
            vm.put("value", entry.getKey());
            vmResources.add(new ValueMapResource(
                    request.getResourceResolver(),
                    new ResourceMetadata(),
                    NT_UNSTRUCTURED,
                    vm
            ));
        }
        return new SimpleDataSource(vmResources.iterator());
    }
}
