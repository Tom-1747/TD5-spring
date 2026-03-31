package com.example.ingredientAgain.service;

import com.example.ingredientAgain.entity.Dish;
import com.example.ingredientAgain.entity.Ingredient;
import com.example.ingredientAgain.repository.DishRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {

    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public void updateDishIngredients(Integer dishId, List<Ingredient> ingredients) {
        dishRepository.updateIngredients(dishId, ingredients);
    }
}