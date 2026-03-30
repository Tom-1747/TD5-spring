package com.example.ingredientAgain.controller;

import com.example.ingredientAgain.entity.Ingredient;
import com.example.ingredientAgain.entity.StockValue;
import com.example.ingredientAgain.entity.enums.Unit;
import com.example.ingredientAgain.repository.IngredientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientRepository ingredientRepository;

    public IngredientController(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping
    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable Integer id) {
        try {
            Ingredient ingredient = ingredientRepository.findById(id);
            return ResponseEntity.ok(ingredient);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<StockValue> getStock(
            @PathVariable Integer id,
            @RequestParam("at") Instant at,
            @RequestParam("unit") Unit unit) {

        if (at == null || unit == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            StockValue stock = ingredientRepository.getStockValueAt(id, at, unit);
            return ResponseEntity.ok(stock);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}