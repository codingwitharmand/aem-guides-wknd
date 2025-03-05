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
import java.util.Map;

@Component(
        service = Servlet.class,
        property = {
                ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES + "=sling/servlets/wknd/utils/brands",
                ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
        }
)
public class BrandServlet extends SlingSafeMethodsServlet implements DatasourceAware {

    @Override
    protected void doGet(@NonNull SlingHttpServletRequest request, @NonNull SlingHttpServletResponse response) {
        final var brands = Map.of("apple", "Apple", "samsung", "Samsung", "huawei", "Huawei");

        DataSource dataSource = buildDataSource(brands, request);
        if (request.getAttribute(DataSource.class.getName()) == null) {
            request.setAttribute(DataSource.class.getName(), dataSource);
        }
    }
}
