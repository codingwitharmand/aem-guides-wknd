package com.adobe.aem.guides.wknd.core.services.product;

import com.adobe.aem.guides.wknd.core.beans.Category;
import com.adobe.aem.guides.wknd.core.beans.Product;

import java.util.List;

public interface ProductService {

    /**
     * Get featured products from the catalog
     * @param limit Maximum number of products to return
     * @return List of featured products
     */
    List<Product> getProducts(int limit);

    /**
     * Get products by category
     * @param category The category to filter by
     * @param limit Maximum number of products to return
     * @return List of products in the specified category
     */
    List<Product> getProductByCategory(String category, int limit);

    /**
     * Get categories
     * @return List of categories
     */
    List<Category> getCategories();
}
