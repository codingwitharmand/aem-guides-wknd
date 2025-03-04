package com.adobe.aem.guides.wknd.core.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocialNetworkProfile {
    private String text;
    private String link;
    private String icon;
}
