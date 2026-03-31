package com.example.ingredientAgain.controller;

import com.example.ingredientAgain.entity.Dish;
import com.example.ingredientAgain.entity.Ingredient;
import com.example.ingredientAgain.service.DishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping
    public List<Dish> getAllDishes() {
        return dishService.getAllDishes();
    }

    @PutMapping("/{id}/ingredients")
    public ResponseEntity<Void> updateDishIngredients(
            @PathVariable Integer id,
            @RequestBody(required = true) List<Ingredient> ingredients) {

        if (ingredients == null || ingredients.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        dishService.updateDishIngredients(id, ingredients);
        return ResponseEntity.ok().build();
    }
}