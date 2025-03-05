package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import lombok.NonNull;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import java.util.HashMap;

@Component(
        service = Servlet.class,
        property = {
                ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES + "=sling/servlets/wknd/utils/suppliers",
                ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
        }
)
public class SupplierServlet extends SlingSafeMethodsServlet implements DatasourceAware {

    @Override
    protected void doGet(@NonNull SlingHttpServletRequest request, @NonNull SlingHttpServletResponse response) {
        var resourceResolver = request.getResourceResolver();
        var supplierResource = resourceResolver.getResource("/content/suppliers");
        var suppliers = supplierResource.getChildren();
        var supplierMap = new HashMap<String, String>();
        for (var supplier : suppliers) {
            supplierMap.put(supplier.getName(), supplier.getValueMap().get("text", String.class));
        }

        DataSource dataSource = buildDataSource(supplierMap, request);
        if (request.getAttribute(DataSource.class.getName()) == null) {
            request.setAttribute(DataSource.class.getName(), dataSource);
        }
    }
}
