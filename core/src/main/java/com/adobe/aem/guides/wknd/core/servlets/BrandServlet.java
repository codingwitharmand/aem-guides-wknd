package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.commons.jcr.JcrConstants;
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
import java.util.List;
import java.util.Map;

@Component(
        service = Servlet.class,
        property = {
                ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES + "=" + "sling/servlets/wknd/utils/brands",
                ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
        }
)
public class BrandServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(@NonNull SlingHttpServletRequest request, @NonNull SlingHttpServletResponse response) {
        Map<String, String> brands = Map.of("apple", "Apple", "samsung", "Samsung", "huawei", "Huawei");
        List<Resource> valueMapResourceList = new ArrayList<>();

        for (Map.Entry<String, String> brand : brands.entrySet()) {
            ValueMap vm = new ValueMapDecorator(new HashMap<>());
            vm.put("text", brand.getValue());
            vm.put("value", brand.getKey());
            valueMapResourceList.add(new ValueMapResource(
                    request.getResourceResolver(),
                    new ResourceMetadata(),
                    JcrConstants.NT_UNSTRUCTURED,
                    vm)
            );
        }

        DataSource datasource = new SimpleDataSource(valueMapResourceList.iterator());
        if (request.getAttribute(DataSource.class.getName()) == null) {
            request.setAttribute(DataSource.class.getName(), datasource);
        }
    }
}
