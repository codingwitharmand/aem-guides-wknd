package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.aem.guides.wknd.core.services.query.QueryService;
import com.adobe.granite.ui.components.ds.DataSource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component(
        service = Servlet.class,
        property = {
                ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES + "=sling/servlets/wknd/utils/suppliers",
                ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
        }
)
public class SupplierServlet extends SlingSafeMethodsServlet implements DatasourceAware {

    @Reference
    private transient QueryService queryService;

    @Override
    protected void doGet(@NonNull SlingHttpServletRequest request, @NonNull SlingHttpServletResponse response) {
        try {
            var resourceResolver = request.getResourceResolver();
            var queryParams = Map.of(
                    "path", "/content/suppliers/",
                    "type", "nt:unstructured"
            );
            var result = queryService.getResult(queryParams, resourceResolver.adaptTo(Session.class));

            var supplierMap = new HashMap<String, String>();
            for (var hit : result.getHits()) {
                var supplier = hit.getResource();
                supplierMap.put(supplier.getName(), supplier.getValueMap().get("text", String.class));
            }

            DataSource dataSource = buildDataSource(supplierMap, request);
            if (request.getAttribute(DataSource.class.getName()) == null) {
                request.setAttribute(DataSource.class.getName(), dataSource);
            }
        } catch (RepositoryException e) {
            log.error(e.getMessage(), e);
        }
    }
}
