package com.example.ingredientAgain.controller;

import com.example.ingredientAgain.entity.Dish;
import com.example.ingredientAgain.entity.Ingredient;
import com.example.ingredientAgain.exception.DishAlreadyExistsException;
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
    public ResponseEntity<List<Dish>> getAllDishes(
            @RequestParam(required = false) Double priceUnder,
            @RequestParam(required = false) Double priceOver,
            @RequestParam(required = false) String name) {

        List<Dish> filtered = dishService.getDishesFiltered(priceUnder, priceOver, name);
        return ResponseEntity.ok(filtered);
    }

    @PutMapping("/{id}/ingredients")
    public ResponseEntity<Void> updateDishIngredients(
            @PathVariable Integer id,
            @RequestBody List<Ingredient> ingredients) {

        if (ingredients == null || ingredients.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        dishService.updateDishIngredients(id, ingredients);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> createDishes(@RequestBody List<CreateDishRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            List<Dish> created = dishService.createDishes(requests);
            return ResponseEntity.status(201).body(created);
        } catch (DishAlreadyExistsException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // pour debug
            return ResponseEntity.status(500).body("Erreur création plats: " + e.getMessage());
        }
    }
}