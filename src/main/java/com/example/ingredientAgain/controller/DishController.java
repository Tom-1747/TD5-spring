package com.example.ingredientAgain.controller;

import com.example.ingredientAgain.entity.Dish;
import com.example.ingredientAgain.entity.Ingredient;
import com.example.ingredientAgain.repository.DishRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {

    private final DishRepository dishRepository;

    public DishController(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @GetMapping
    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    @PutMapping("/{id}/ingredients")
    public ResponseEntity<Void> updateDishIngredients(
            @PathVariable Integer id,
            @RequestBody(required = true) List<Ingredient> ingredients) {

        if (ingredients == null || ingredients.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            dishRepository.updateIngredients(id, ingredients);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
