package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.ProductModel;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Style;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = ProductModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductModelImpl implements ProductModel {

    private static final List<String> DOLLAR_COUNTRIES = List.of("us", "ca");

    @ScriptVariable
    private Style currentStyle;

    @ValueMapValue
    private Double price;

    @ValueMapValue
    private boolean discount;

    @ValueMapValue
    private int discountPercentage;

    @ValueMapValue
    private String description;

    @Getter
    private String separator;

    @Getter
    private String computedPrice;

    @ScriptVariable
    private Page currentPage;

    @PostConstruct
    public void init() {
        if (nonNull(currentStyle)) {
            separator = currentStyle.get("separator", "pipe");
        }

        // Compute Price
        if (discount) {
            price = price - (discountPercentage * price / 100);
        }
        computedPrice = formatPrice(price, getCurrency());
    }

    private String getCurrency() {
        final var country = currentPage.getAbsoluteParent(2);

        if (isNull(country)) {
            return StringUtils.EMPTY;
        }

        return DOLLAR_COUNTRIES.contains(country.getName()) ? "$" : "€";
    }

    private String formatPrice(double price, String currency) {
        return "$".equals(currency) ?
                String.format("%s%.2f", currency, price) :
                String.format("%.2f%s", price, currency);
    }
}
