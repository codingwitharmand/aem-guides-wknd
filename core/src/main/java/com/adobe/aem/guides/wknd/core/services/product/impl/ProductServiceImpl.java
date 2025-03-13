package com.adobe.aem.guides.wknd.core.services.product.impl;

import com.adobe.aem.guides.wknd.core.beans.Category;
import com.adobe.aem.guides.wknd.core.beans.CategoryResponse;
import com.adobe.aem.guides.wknd.core.beans.Product;
import com.adobe.aem.guides.wknd.core.beans.ProductResponse;
import com.adobe.aem.guides.wknd.core.services.httpclient.HttpClientService;
import com.adobe.aem.guides.wknd.core.services.product.ProductService;
import com.adobe.aem.guides.wknd.core.services.product.config.ProductCatalogConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.nonNull;

@Slf4j
@Component(
        service = ProductService.class,
        configurationPolicy = ConfigurationPolicy.REQUIRE,
        immediate = true
)
@Designate(ocd = ProductCatalogConfig.class, factory = true)
public class ProductServiceImpl implements ProductService {

    private static final String ERROR_MESSAGE_TEMPLATE = "Exception while creating URI: %s";

    private ProductCatalogConfig config;

    @Reference
    private HttpClientService httpClientService;

    @Activate
    public void activate(ProductCatalogConfig config) {
        this.config = config;
    }

    @Override
    public List<Product> getProducts(int limit) {
        try {
            var uri = new URIBuilder(config.api_product_endpoint());
            if (limit > 0) {
                uri = uri.setParameter("limit", String.valueOf(limit));
            }
            return getProducts(uri.toString());
        } catch (URISyntaxException e) {
            log.error(String.format(ERROR_MESSAGE_TEMPLATE, e.getMessage()));
            return Collections.emptyList();
        }
    }

    @Override
    public List<Product> getProductByCategory(String category, int limit) {
        try {
            var uri = new URIBuilder(String.format("%s/category/%s", config.api_product_endpoint(), category));
            uri.setParameter("limit", String.valueOf(limit));
            return getProducts(uri.toString());
        } catch (URISyntaxException e) {
            log.error(String.format(ERROR_MESSAGE_TEMPLATE, e.getMessage()));
            return Collections.emptyList();
        }
    }

    @Override
    public List<Category> getCategories() {
        try {
            var uri = new URIBuilder(String.format("%s/categories", config.api_product_endpoint()));
            var response = httpClientService.get(uri.toString(), CategoryResponse.class);
            return nonNull(response.getBody()) ?
                    response.getBody() :
                    Collections.emptyList();
        } catch (URISyntaxException e) {
            log.error(String.format(ERROR_MESSAGE_TEMPLATE, e.getMessage()));
            return Collections.emptyList();
        }
    }

    private String truncateDescription(String description, int length) {
        return description.length() > length ? String.format("%s...", description.substring(0, length)) : description;
    }

    private List<Product> getProducts(String uri) {
        var response = httpClientService.get(uri, ProductResponse.class);
        if (nonNull(response.getBody())) {
            var products = response.getBody().getProducts();
            if (config.truncate_description()) {
                products.forEach(p -> p.setDescription(truncateDescription(p.getDescription(), config.description_max_length())));
            }
            return products;
        }
        return Collections.emptyList();
    }
}
