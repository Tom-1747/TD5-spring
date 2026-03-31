package com.example.ingredientAgain.service;

import com.example.ingredientAgain.entity.Ingredient;
import com.example.ingredientAgain.entity.StockValue;
import com.example.ingredientAgain.entity.enums.Unit;
import com.example.ingredientAgain.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient getIngredientById(Integer id) {
        return ingredientRepository.findById(id);
    }

    public StockValue getStockValueAt(Integer ingredientId, Instant at, Unit unit) {
        return ingredientRepository.getStockValueAt(ingredientId, at, unit);
    }
}