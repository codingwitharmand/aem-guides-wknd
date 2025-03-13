package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.beans.Product;
import com.adobe.aem.guides.wknd.core.models.ProductCatalogModel;
import com.adobe.aem.guides.wknd.core.services.product.ProductService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Slf4j
@Model(
        adaptables = {SlingHttpServletRequest.class, Resource.class},
        adapters = ProductCatalogModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductCatalogModelImpl implements ProductCatalogModel {

    @OSGiService
    private ProductService productService;

    @ValueMapValue
    private int limit;

    @ValueMapValue
    private String category;

    @Getter
    List<Product> products = new ArrayList<>();

    @PostConstruct
    public void init() {
        products = isNull(category) ?
                productService.getProducts(limit) :
                productService.getProductByCategory(category, limit);
    }
}
