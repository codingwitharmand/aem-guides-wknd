package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.aem.guides.wknd.core.services.product.ProductService;
import com.adobe.granite.ui.components.ds.DataSource;
import lombok.NonNull;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import java.util.HashMap;

@Component(
        service = Servlet.class,
        property = {
                ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES + "=sling/servlets/wknd/utils/categories",
                ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
        }
)
public class CategoryServlet extends SlingSafeMethodsServlet implements DatasourceAware {

    @Reference
    private transient ProductService productService;

    @Override
    protected void doGet(@NonNull SlingHttpServletRequest request, @NonNull SlingHttpServletResponse response) {

        final var categories = productService.getCategories();
        var categoryMap = new HashMap<String, String>();

        for (var category : categories) {
            categoryMap.put(category.getSlug(), category.getName());
        }

        DataSource dataSource = buildDataSource(categoryMap, request);
        if (request.getAttribute(DataSource.class.getName()) == null) {
            request.setAttribute(DataSource.class.getName(), dataSource);
        }
    }
}
