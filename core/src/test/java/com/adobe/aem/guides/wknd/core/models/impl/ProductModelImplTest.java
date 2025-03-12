package com.adobe.aem.guides.wknd.core.models.impl;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.testing.resourceresolver.MockValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ProductModelImplTest {

    private final AemContext context = new AemContext();

    private ProductModelImpl model;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(ProductModelImpl.class);
        context.load().json("/com/adobe/aem/guides/wknd/core/models/impl/ProductModelImplTest.json", "/content/wknd");
    }

    @Test
    void initShouldSetSeparatorToPipe() {
        context.currentResource("/content/wknd/us/jcr:content/product/jcr:content");

        model = context.request().adaptTo(ProductModelImpl.class);

        assertNotNull(model);
        assertEquals("pipe", model.getSeparator());
    }

    @Test
    void initShouldSetSeparatorWithCurrentStyle() {
        context.currentResource("/content/wknd/us/jcr:content/product/jcr:content");
        final var mockValueMap = new MockValueMap(context.currentResource());
        mockValueMap.put("separator", "slash");

        context.contentPolicyMapping(context.currentResource().getResourceType(), mockValueMap);

        model = context.request().adaptTo(ProductModelImpl.class);

        assertNotNull(model);
        assertEquals("slash", model.getSeparator());
    }

    @ParameterizedTest
    @CsvSource({
            "/content/wknd/us/jcr:content/product/jcr:content, $1500.00",
            "/content/wknd/ca/jcr:content/product/jcr:content, $1200.00",
            "/content/wknd/fr/jcr:content/product/jcr:content, 1350.00€"
    })
    void initShouldSetComputedPrice(String resourcePath, String expectedPrice) {
        context.currentResource(resourcePath);

        model = context.request().adaptTo(ProductModelImpl.class);

        assertNotNull(model);
        assertEquals(expectedPrice, model.getComputedPrice());
    }
}