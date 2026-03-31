package com.example.ingredientAgain.repository;

import com.example.ingredientAgain.datasource.DataSource;
import com.example.ingredientAgain.entity.Ingredient;
import com.example.ingredientAgain.entity.StockValue;
import com.example.ingredientAgain.entity.enums.CategoryEnum;
import com.example.ingredientAgain.entity.enums.Unit;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class IngredientRepository {

    private final DataSource dataSource;

    public IngredientRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Ingredient> findAll() {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT id, name, price, category FROM ingredient ORDER BY id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Ingredient ing = new Ingredient();
                ing.setId(rs.getInt("id"));
                ing.setName(rs.getString("name"));
                ing.setPrice(rs.getDouble("price"));
                ing.setCategory(CategoryEnum.valueOf(rs.getString("category")));
                ingredients.add(ing);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des ingrédients", e);
        }
        return ingredients;
    }

    public Ingredient findById(Integer id) {
        String sql = "SELECT id, name, price, category FROM ingredient WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Ingredient ing = new Ingredient();
                    ing.setId(rs.getInt("id"));
                    ing.setName(rs.getString("name"));
                    ing.setPrice(rs.getDouble("price"));
                    ing.setCategory(CategoryEnum.valueOf(rs.getString("category")));
                    return ing;
                } else {
                    throw new RuntimeException("Ingredient.id=" + id + " is not found");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de l'ingrédient id=" + id, e);
        }
    }

    public StockValue getStockValueAt(Integer ingredientId, Instant at, Unit unit) {
        findById(ingredientId);

        String sql = """
            SELECT COALESCE(SUM(CASE WHEN type = 'IN' THEN quantity ELSE -quantity END), 0) as stock
            FROM stock_movement
            WHERE id_ingredient = ? AND creation_datetime <= ?
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ingredientId);
            stmt.setTimestamp(2, Timestamp.from(at));

            try (ResultSet rs = stmt.executeQuery()) {
                double quantity = rs.next() ? rs.getDouble("stock") : 0.0;
                return new StockValue(quantity, unit);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du calcul du stock de l'ingrédient id=" + ingredientId, e);
        }
    }
}