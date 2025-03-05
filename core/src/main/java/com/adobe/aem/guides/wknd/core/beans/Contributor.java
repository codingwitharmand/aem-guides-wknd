package com.adobe.aem.guides.wknd.core.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contributor {
    private String name;
    private String image;
    private String role;
}
