package com.adobe.aem.guides.wknd.core.services.product.impl;

import com.adobe.aem.guides.wknd.core.beans.ApiResponse;
import com.adobe.aem.guides.wknd.core.beans.ProductResponse;
import com.adobe.aem.guides.wknd.core.services.httpclient.HttpClientService;
import com.adobe.aem.guides.wknd.core.services.product.config.ProductCatalogConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class ProductCatalogModelServiceImplTest {

    @Mock
    private HttpClientService httpClientService;

    @InjectMocks
    private ProductServiceImpl productCatalogService;

    @BeforeEach
    void setUp() {
        productCatalogService.activate(getProductCatalogConfig());
    }

    @Test
    void getProductsShouldReturnProducts() {
        when(httpClientService.get("https://dummy.com?limit=10", ProductResponse.class))
                .thenReturn(ApiResponse.<ProductResponse>builder()
                        .body(ProductResponse.builder()
                                .products(List.of(

                                ))
                                .build())
                        .build());

        var response = productCatalogService.getProducts(10);

        assertNotNull(response);
    }

    static ProductCatalogConfig getProductCatalogConfig() {
        final var config = spy(ProductCatalogConfig.class);
        when(config.api_product_endpoint()).thenReturn("https://dummy.com");
        lenient().when(config.description_max_length()).thenReturn(50);
        lenient().when(config.truncate_description()).thenReturn(false);
        return config;
    }

}