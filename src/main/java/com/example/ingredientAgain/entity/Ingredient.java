package com.example.ingredientAgain.entity;

import com.example.ingredientAgain.entity.enums.CategoryEnum;

public class Ingredient {
    private Integer id;
    private String name;
    private Double price;
    private CategoryEnum category;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public CategoryEnum getCategory() { return category; }
    public void setCategory(CategoryEnum category) { this.category = category; }
}
