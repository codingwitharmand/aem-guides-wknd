package com.adobe.aem.guides.wknd.core.services.product.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "WKND Product Catalog Configuration",
        description = "Configuration for the WKND Product Catalog Service"
)
public @interface ProductCatalogConfig {

    @AttributeDefinition(
            name = "API Endpoint",
            description = "API Endpoint to retrieve products information"
    )
    String api_product_endpoint() default "https://dummyjson.com/products";

    @AttributeDefinition(
            name = "Truncate description",
            description = "Truncate description field to max length",
            type = AttributeType.BOOLEAN
    )
    boolean truncate_description() default false;

    @AttributeDefinition(
            name = "Description Max Length",
            description = "Max Length for description field",
            type = AttributeType.INTEGER
    )
    int description_max_length() default 100;
}
