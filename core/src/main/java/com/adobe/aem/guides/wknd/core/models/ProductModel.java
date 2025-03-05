package com.adobe.aem.guides.wknd.core.models;

import com.day.cq.wcm.api.designer.Style;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;

import javax.annotation.PostConstruct;

import static java.util.Objects.nonNull;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductModel {

    @ScriptVariable
    private Style currentStyle;

    @Getter
    private String separator;

    @PostConstruct
    public void init() {
        if (nonNull(currentStyle)) {
            separator = currentStyle.get("separator", "pipe");
        }
    }
}
