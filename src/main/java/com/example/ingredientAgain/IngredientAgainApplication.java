package com.example.ingredientAgain;

import com.example.ingredientAgain.datasource.DataSource;
import com.example.ingredientAgain.repository.DishRepository;
import com.example.ingredientAgain.repository.IngredientRepository;
import com.example.ingredientAgain.service.DishService;
import com.example.ingredientAgain.service.IngredientService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class IngredientAgainApplication {

    public static void main(String[] args) {
        SpringApplication.run(IngredientAgainApplication.class, args);
    }

    @Bean
    public DataSource dataSource() {
        return new DataSource();
    }

    @Bean
    public IngredientRepository ingredientRepository(DataSource dataSource) {
        return new IngredientRepository(dataSource);
    }

    @Bean
    public DishRepository dishRepository(DataSource dataSource) {
        return new DishRepository(dataSource);
    }

    @Bean
    public IngredientService ingredientService(IngredientRepository ingredientRepository) {
        return new IngredientService(ingredientRepository);
    }

    @Bean
    public DishService dishService(DishRepository dishRepository) {
        return new DishService(dishRepository);
    }
}