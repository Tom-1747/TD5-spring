package com.example.ingredientAgain.controller;

import com.example.ingredientAgain.entity.Ingredient;
import com.example.ingredientAgain.entity.StockValue;
import com.example.ingredientAgain.entity.enums.Unit;
import com.example.ingredientAgain.service.IngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public List<Ingredient> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable Integer id) {
        Ingredient ingredient = ingredientService.getIngredientById(id);
        return ResponseEntity.ok(ingredient);
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<StockValue> getStock(
            @PathVariable Integer id,
            @RequestParam("at") Instant at,
            @RequestParam("unit") Unit unit) {

        if (at == null || unit == null) {
            return ResponseEntity.badRequest().build();
        }

        StockValue stock = ingredientService.getStockValueAt(id, at, unit);
        return ResponseEntity.ok(stock);
    }
}