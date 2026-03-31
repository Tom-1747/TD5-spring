package com.example.ingredientAgain.repository;

import com.example.ingredientAgain.datasource.DataSource;
import com.example.ingredientAgain.controller.CreateDishRequest;
import com.example.ingredientAgain.entity.Dish;
import com.example.ingredientAgain.entity.Ingredient;
import com.example.ingredientAgain.entity.enums.DishTypeEnum;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DishRepository {

    private final DataSource dataSource;

    public DishRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean existsByName(String name) {
        String sql = "SELECT 1 FROM dish WHERE LOWER(name) = LOWER(?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur vérification nom plat", e);
        }
    }

    public List<Dish> findAll() {
        List<Dish> dishes = new ArrayList<>();
        String sql = "SELECT id, name, dish_type, selling_price FROM dish ORDER BY id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Dish dish = new Dish();
                dish.setId(rs.getInt("id"));
                dish.setName(rs.getString("name"));
                dish.setDishType(DishTypeEnum.valueOf(rs.getString("dish_type")));
                dish.setSellingPrice(rs.getObject("selling_price") != null ? rs.getDouble("selling_price") : null);
                dish.setIngredients(new ArrayList<>());
                dishes.add(dish);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur récupération plats", e);
        }
        return dishes;
    }

    public void updateIngredients(Integer dishId, List<Ingredient> ingredients) {
        String deleteSql = "DELETE FROM dish_ingredient WHERE id_dish = ?";
        String insertSql = "INSERT INTO dish_ingredient (id_dish, id_ingredient, required_quantity, unit) VALUES (?, ?, 0.0, 'KG')";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, dishId);
                deleteStmt.executeUpdate();
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                for (Ingredient ing : ingredients) {
                    insertStmt.setInt(1, dishId);
                    insertStmt.setInt(2, ing.getId());
                    insertStmt.executeUpdate();
                }
            }

            conn.commit();

        } catch (Exception e) {
            throw new RuntimeException("Erreur update ingrédients", e);
        }
    }

    public List<Dish> createDishes(List<CreateDishRequest> requests) {
        List<Dish> created = new ArrayList<>();
        String sql = "INSERT INTO dish (name, dish_type, selling_price) VALUES (?, ?::dish_type, ?) RETURNING id, name, dish_type, selling_price";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            for (CreateDishRequest req : requests) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, req.getName());
                    stmt.setString(2, req.getDishType().name());
                    if (req.getSellingPrice() != null) {
                        stmt.setDouble(3, req.getSellingPrice());
                    } else {
                        stmt.setNull(3, Types.NUMERIC);
                    }

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            Dish dish = new Dish();
                            dish.setId(rs.getInt("id"));
                            dish.setName(rs.getString("name"));
                            dish.setDishType(DishTypeEnum.valueOf(rs.getString("dish_type")));
                            dish.setSellingPrice(rs.getObject("selling_price") != null ? rs.getDouble("selling_price") : null);
                            dish.setIngredients(new ArrayList<>());
                            created.add(dish);
                        }
                    }
                }
            }

            conn.commit();

        } catch (Exception e) {
            throw new RuntimeException("Erreur création plats", e);
        }
        return created;
    }
}