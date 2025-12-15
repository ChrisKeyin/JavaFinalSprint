package com.gymmanagement.dao;

import com.gymmanagement.model.GymMerch;
import com.gymmanagement.util.DBConnection;
import com.gymmanagement.util.LoggerUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) for managing gym merchandise in the database.
 * Provides methods to create, retrieve, and calculate statistics for merchandise items.
 */
public class GymMerchDAO {

    private static final Logger LOGGER = LoggerUtil.getLogger();

    /**
     * Creates a new merchandise item in the database.
     * Inserts the merch details and retrieves the auto-generated ID.
     * 
     * @param merch the GymMerch object containing merchandise details
     * @return the created GymMerch object with the generated ID, or null if creation fails
     */
    public GymMerch createMerch(GymMerch merch) {
        String sql = "INSERT INTO gym_merch (merch_name, merch_type, merch_price, quantity_in_stock) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set the prepared statement parameters
            stmt.setString(1, merch.getMerchName());
            stmt.setString(2, merch.getMerchType());
            stmt.setBigDecimal(3, merch.getMerchPrice());
            stmt.setInt(4, merch.getQuantityInStock());

            // Execute the insert and check if rows were affected
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Creating merch item failed, no rows affected.");
            }

            // Retrieve the auto-generated ID and set it on the merch object
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    merch.setMerchId(rs.getInt(1));
                }
            }

            LOGGER.info("Created merch item: " + merch.getMerchName());
            return merch;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating merch item", e);
            return null;
        }
    }

    /**
     * Retrieves all merchandise items from the database.
     * Results are ordered by merchandise ID.
     * 
     * @return a list of all GymMerch objects, or an empty list if none found or on error
     */
    public List<GymMerch> getAllMerch() {
        String sql = "SELECT * FROM gym_merch ORDER BY merch_id";
        List<GymMerch> merchList = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Map each row from the result set to a GymMerch object
            while (rs.next()) {
                merchList.add(mapRowToMerch(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all merch items", e);
        }

        return merchList;
    }

    /**
     * Calculates the total monetary value of all merchandise in stock.
     * Formula: sum of (merch_price * quantity_in_stock) for all items.
     * 
     * @return the total stock value as BigDecimal, or zero if no items or on error
     */
    public BigDecimal getTotalStockValue() {
        String sql = "SELECT COALESCE(SUM(merch_price * quantity_in_stock), 0) AS total_value FROM gym_merch";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Return the calculated total value from the database query
            if (rs.next()) {
                return rs.getBigDecimal("total_value");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error calculating total stock value", e);
        }
        return BigDecimal.ZERO;
    }

    /**
     * Helper method to map a database result set row to a GymMerch object.
     * Extracts all merchandise columns from the current row.
     * 
     * @param rs the ResultSet positioned at the row to map
     * @return a new GymMerch object populated with data from the result set row
     * @throws SQLException if there's an error accessing the result set
     */
    private GymMerch mapRowToMerch(ResultSet rs) throws SQLException {
        int id = rs.getInt("merch_id");
        String name = rs.getString("merch_name");
        String type = rs.getString("merch_type");
        BigDecimal price = rs.getBigDecimal("merch_price");
        int quantity = rs.getInt("quantity_in_stock");

        return new GymMerch(id, name, type, price, quantity);
    }
}
