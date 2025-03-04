package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.beans.Contributor;
import com.adobe.aem.guides.wknd.core.models.ContributorList;
import com.day.crx.JcrConstants;
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
        adaptables = {SlingHttpServletRequest.class},
        adapters = ContributorList.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ContributorListImpl implements ContributorList {

    @ValueMapValue
    private String xfPath;

    @SlingObject
    ResourceResolver resourceResolver;

    private static final String SUFFIX_PATH = "/master/"+ JcrConstants.JCR_CONTENT + "/root/container";

    @Getter
    private final List<Contributor> contributors = new ArrayList<>();

    @PostConstruct
    public void init() {
        Resource contributorsResource = resourceResolver.getResource(xfPath);
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

    private Contributor buildContributor(Resource containerResource) {
        Contributor.ContributorBuilder builder = Contributor.builder();
        Iterable<Resource> containerChildren = containerResource.getChildren();
        for (Resource containerChild : containerChildren) {
            switch (containerChild.getResourceType()) {
                case "wknd/components/image":
                    builder.image(containerChild.getValueMap().get("fileReference", String.class));
                    break;
                case "wknd/components/title":
                    String headingLevel = containerChild.getValueMap().get("type", String.class);
                    if ("h3".equals(headingLevel)) {
                        builder.name(containerChild.getValueMap().get("jcr:title", String.class));
                    } else if ("h5".equals(headingLevel)) {
                        builder.role(containerChild.getValueMap().get("jcr:title", String.class));
                    }
                    break;
                default: break;
            }
        }

        return builder.build();
    }

}
