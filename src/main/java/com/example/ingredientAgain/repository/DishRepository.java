package com.example.ingredientAgain.repository;

import com.example.ingredientAgain.datasource.DataSource;
import com.example.ingredientAgain.entity.Dish;
import com.example.ingredientAgain.entity.Ingredient;
import com.example.ingredientAgain.entity.enums.DishTypeEnum;
import com.example.ingredientAgain.entity.enums.CategoryEnum;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DishRepository {

    private final DataSource dataSource;

    public DishRepository(DataSource dataSource) {
        this.dataSource = dataSource;
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
                if (rs.getObject("selling_price") != null) {
                    dish.setSellingPrice(rs.getDouble("selling_price"));
                }
                dish.setIngredients(loadIngredientsForDish(conn, dish.getId()));
                dishes.add(dish);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des plats", e);
        }
        return dishes;
    }

    private List<Ingredient> loadIngredientsForDish(Connection conn, Integer dishId) throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = """
            SELECT i.id, i.name, i.price, i.category
            FROM dish_ingredient di
            JOIN ingredient i ON di.id_ingredient = i.id
            WHERE di.id_dish = ?
            ORDER BY i.id
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dishId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Ingredient ing = new Ingredient();
                    ing.setId(rs.getInt("id"));
                    ing.setName(rs.getString("name"));
                    ing.setPrice(rs.getDouble("price"));
                    ing.setCategory(CategoryEnum.valueOf(rs.getString("category")));
                    ingredients.add(ing);
                }
            }
        }
        return ingredients;
    }

    public void updateIngredients(Integer dishId, List<Ingredient> ingredientsToSet) {
        if (!dishExists(dishId)) {
            throw new RuntimeException("Dish.id=" + dishId + " is not found");
        }

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM dish_ingredient WHERE id_dish = ?")) {
                deleteStmt.setInt(1, dishId);
                deleteStmt.executeUpdate();
            }

            String insertSql = "INSERT INTO dish_ingredient (id_dish, id_ingredient, quantity_required, unit) VALUES (?, ?, 0.0, 'KG')";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                for (Ingredient ing : ingredientsToSet) {
                    if (ing.getId() != null && ingredientExists(conn, ing.getId())) {
                        insertStmt.setInt(1, dishId);
                        insertStmt.setInt(2, ing.getId());
                        insertStmt.executeUpdate();
                    }
                }
            }
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour des ingrédients du plat", e);
        }
    }

    private boolean dishExists(Integer dishId) {
        String sql = "SELECT 1 FROM dish WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dishId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean ingredientExists(Connection conn, Integer ingredientId) throws SQLException {
        String sql = "SELECT 1 FROM ingredient WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ingredientId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}