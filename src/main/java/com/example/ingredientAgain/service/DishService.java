package com.example.ingredientAgain.service;

import com.example.ingredientAgain.controller.CreateDishRequest;
import com.example.ingredientAgain.entity.Dish;
import com.example.ingredientAgain.entity.Ingredient;
import com.example.ingredientAgain.exception.DishAlreadyExistsException;
import com.example.ingredientAgain.repository.DishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishService {

    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public List<Dish> getDishesFiltered(Double priceUnder, Double priceOver, String name) {
        return dishRepository.findAll().stream()
                .filter(d -> priceUnder == null || (d.getSellingPrice() != null && d.getSellingPrice() < priceUnder))
                .filter(d -> priceOver == null || (d.getSellingPrice() != null && d.getSellingPrice() > priceOver))
                .filter(d -> name == null || d.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void updateDishIngredients(Integer dishId, List<Ingredient> ingredients) {
        dishRepository.updateIngredients(dishId, ingredients);
    }

    public List<Dish> createDishes(List<CreateDishRequest> requests) {
        for (CreateDishRequest req : requests) {
            if (dishRepository.existsByName(req.getName())) {
                throw new DishAlreadyExistsException("Dish.name=" + req.getName() + " already exists");
            }
        }
        return dishRepository.createDishes(requests);
    }
}