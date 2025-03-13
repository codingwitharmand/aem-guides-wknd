package com.adobe.aem.guides.wknd.core.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.util.Objects.nonNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private Double discountPercentage;
    private Double rating;
    private String category;
    private List<String> tags;
    private String brand;
    private List<String> images;

    public Double getPrice() {
        price = nonNull(rating) ?
                price - price * (discountPercentage/100) :
                price;
        return Math.round(price * Math.pow(10, 2)) / Math.pow(10, 2);
    }
}
