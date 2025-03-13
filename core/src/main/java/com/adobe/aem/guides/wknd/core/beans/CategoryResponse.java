package com.adobe.aem.guides.wknd.core.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryResponse extends ArrayList<Category> {
}
