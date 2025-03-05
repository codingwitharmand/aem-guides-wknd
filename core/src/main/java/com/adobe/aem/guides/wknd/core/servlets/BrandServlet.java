package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import lombok.NonNull;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.day.cq.commons.jcr.JcrConstants.NT_UNSTRUCTURED;

@Component(
        service = Servlet.class,
        property = {
                ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES + "=sling/servlets/wknd/utils/brands",
                ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
        }
)
public class BrandServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(@NonNull SlingHttpServletRequest request, @NonNull SlingHttpServletResponse response) {
        final var brands = Map.of("apple", "Apple", "samsung", "Samsung", "huawei", "Huawei");
        final var vmResources = new ArrayList<Resource>();

        for (final var entry : brands.entrySet()) {
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

        DataSource dataSource = new SimpleDataSource(vmResources.iterator());
        if (request.getAttribute(DataSource.class.getName()) == null) {
            request.setAttribute(DataSource.class.getName(), dataSource);
        }
    }
}
