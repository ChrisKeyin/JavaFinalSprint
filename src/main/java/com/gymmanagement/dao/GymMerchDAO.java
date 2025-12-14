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

public class GymMerchDAO {

    private static final Logger LOGGER = LoggerUtil.getLogger();

    public GymMerch createMerch(GymMerch merch) {
        String sql = "INSERT INTO gym_merch (merch_name, merch_type, merch_price, quantity_in_stock) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, merch.getMerchName());
            stmt.setString(2, merch.getMerchType());
            stmt.setBigDecimal(3, merch.getMerchPrice());
            stmt.setInt(4, merch.getQuantityInStock());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Creating merch item failed, no rows affected.");
            }

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

    public List<GymMerch> getAllMerch() {
        String sql = "SELECT * FROM gym_merch ORDER BY merch_id";
        List<GymMerch> merchList = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                merchList.add(mapRowToMerch(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all merch items", e);
        }

        return merchList;
    }

    public BigDecimal getTotalStockValue() {
        String sql = "SELECT COALESCE(SUM(merch_price * quantity_in_stock), 0) AS total_value FROM gym_merch";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getBigDecimal("total_value");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error calculating total stock value", e);
        }
        return BigDecimal.ZERO;
    }

    private GymMerch mapRowToMerch(ResultSet rs) throws SQLException {
        int id = rs.getInt("merch_id");
        String name = rs.getString("merch_name");
        String type = rs.getString("merch_type");
        BigDecimal price = rs.getBigDecimal("merch_price");
        int quantity = rs.getInt("quantity_in_stock");

        return new GymMerch(id, name, type, price, quantity);
    }
}
