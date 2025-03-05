package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.beans.Contributor;
import com.adobe.aem.guides.wknd.core.models.Contributors;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Model(
        adaptables = { SlingHttpServletRequest.class, Resource.class },
        adapters = Contributors.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ContributorsImpl implements Contributors {

    private static final String SUFFIX_PATH = "/master/jcr:content/root/container";

    @ValueMapValue
    private String xfPath;

    @SlingObject
    private ResourceResolver resourceResolver;

    @Getter
    private final List<Contributor> contributors = new ArrayList<>();

    @PostConstruct
    public void init() {
        final Resource contributorsResource = resourceResolver.getResource(xfPath);

        if (nonNull(contributorsResource)) {
            Iterable<Resource> children = contributorsResource.getChildren();
            for (Resource child : children) {
                Resource containerResource = resourceResolver.getResource(child.getPath() + SUFFIX_PATH);
                if (nonNull(containerResource)) {
                    contributors.add(buildContributor(containerResource));
                }
            }
        }
    }

    private Contributor buildContributor(Resource resource) {
        Contributor contributor = new Contributor();
        Iterable<Resource> children = resource.getChildren();
        for (Resource child : children) {
            switch (child.getResourceType()) {
                case "wknd/components/image":
                    contributor.setImage(child.getValueMap().get("fileReference", String.class));
                    break;
                case "wknd/components/title":
                    String headingLevel = child.getValueMap().get("type", String.class);
                    if ("h3".equals(headingLevel)) {
                        contributor.setName(child.getValueMap().get("jcr:title", String.class));
                    } else if ("h5".equals(headingLevel)) {
                        contributor.setRole(child.getValueMap().get("jcr:title", String.class));
                    }
                    break;
                default: break;
            }
        }
        return contributor;
    }
}
