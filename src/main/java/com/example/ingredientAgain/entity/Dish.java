package com.example.ingredientAgain.entity;

import com.example.ingredientAgain.entity.enums.DishTypeEnum;

import java.util.List;

public class Dish {
    private Integer id;
    private String name;
    private DishTypeEnum dishType;
    private Double sellingPrice;
    private List<Ingredient> ingredients;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public DishTypeEnum getDishType() { return dishType; }
    public void setDishType(DishTypeEnum dishType) { this.dishType = dishType; }

    public Double getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(Double sellingPrice) { this.sellingPrice = sellingPrice; }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }
}
