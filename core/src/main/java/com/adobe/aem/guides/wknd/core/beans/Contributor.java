package com.adobe.aem.guides.wknd.core.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contributor {
    private String name;
    private String role;
    private String image;
    private List<SocialNetworkProfile> profiles;
}
