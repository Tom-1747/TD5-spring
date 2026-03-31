package com.example.ingredientAgain.controller;

import com.example.ingredientAgain.entity.enums.DishTypeEnum;

public class CreateDishRequest {
    private String name;
    private DishTypeEnum dishType;
    private Double sellingPrice;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public DishTypeEnum getDishType() { return dishType; }
    public void setDishType(DishTypeEnum dishType) { this.dishType = dishType; }
    public Double getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(Double sellingPrice) { this.sellingPrice = sellingPrice; }
}
