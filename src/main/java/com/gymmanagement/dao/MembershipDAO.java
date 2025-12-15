package com.gymmanagement.dao;

import com.gymmanagement.model.Membership;
import com.gymmanagement.util.DBConnection;
import com.gymmanagement.util.LoggerUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) for managing gym memberships in the database.
 * Provides methods to create, retrieve, and calculate revenue from membership records.
 */
public class MembershipDAO {

    private static final Logger LOGGER = LoggerUtil.getLogger();

    /**
     * Creates a new membership record in the database.
     * Inserts membership details and retrieves the auto-generated ID.
     * 
     * @param membership the Membership object containing membership details
     * @return the created Membership object with the generated ID, or null if creation fails
     */
    public Membership createMembership(Membership membership) {
        String sql = "INSERT INTO memberships " +
                "(membership_type, membership_description, membership_cost, member_id, start_date, end_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set the prepared statement parameters
            stmt.setString(1, membership.getMembershipType());
            stmt.setString(2, membership.getMembershipDescription());
            stmt.setBigDecimal(3, membership.getMembershipCost());
            stmt.setInt(4, membership.getMemberId());
            stmt.setDate(5, Date.valueOf(membership.getStartDate()));
            // Handle nullable end_date field
            if (membership.getEndDate() != null) {
                stmt.setDate(6, Date.valueOf(membership.getEndDate()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            // Execute the insert and check if rows were affected
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Creating membership failed, no rows affected.");
            }

            // Retrieve the auto-generated ID and set it on the membership object
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    membership.setMembershipId(rs.getInt(1));
                }
            }

            LOGGER.info("Created membership for memberId=" + membership.getMemberId() +
                    " type=" + membership.getMembershipType());
            return membership;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating membership", e);
            return null;
        }
    }

    /**
     * Retrieves all memberships for a specific member.
     * Results are ordered by start_date in descending order (most recent first).
     * 
     * @param memberId the ID of the member to retrieve memberships for
     * @return a list of Membership objects for the specified member, or empty list if none found
     */
    public List<Membership> getMembershipsByMemberId(int memberId) {
        String sql = "SELECT * FROM memberships WHERE member_id = ? ORDER BY start_date DESC";
        List<Membership> memberships = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            // Map each row from the result set to a Membership object
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    memberships.add(mapRowToMembership(rs));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching memberships for memberId=" + memberId, e);
        }
        return memberships;
    }

    /**
     * Retrieves all membership records from the database.
     * Results are ordered by membership_id.
     * 
     * @return a list of all Membership objects, or empty list if none found or on error
     */
    public List<Membership> getAllMemberships() {
        String sql = "SELECT * FROM memberships ORDER BY membership_id";
        List<Membership> memberships = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Map each row from the result set to a Membership object
            while (rs.next()) {
                memberships.add(mapRowToMembership(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all memberships", e);
        }
        return memberships;
    }

    /**
     * Calculates the total revenue from all memberships.
     * Sums up all membership costs from the database.
     * 
     * @return the total membership revenue as BigDecimal, or zero if no memberships or on error
     */
    public BigDecimal getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(membership_cost), 0) AS total_revenue FROM memberships";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Return the aggregated total revenue from the database query
            if (rs.next()) {
                return rs.getBigDecimal("total_revenue");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error calculating total membership revenue", e);
        }
        return BigDecimal.ZERO;
    }

    /**
     * Helper method to map a database result set row to a Membership object.
     * Converts SQL Date objects to LocalDate for date handling.
     * 
     * @param rs the ResultSet positioned at the row to map
     * @return a new Membership object populated with data from the result set row
     * @throws SQLException if there's an error accessing the result set
     */
    private Membership mapRowToMembership(ResultSet rs) throws SQLException {
        int id = rs.getInt("membership_id");
        String type = rs.getString("membership_type");
        String description = rs.getString("membership_description");
        BigDecimal cost = rs.getBigDecimal("membership_cost");
        int memberId = rs.getInt("member_id");
        Date start = rs.getDate("start_date");
        Date end = rs.getDate("end_date");

        // Convert SQL dates to LocalDate, handling null values
        LocalDate startDate = start != null ? start.toLocalDate() : null;
        LocalDate endDate = end != null ? end.toLocalDate() : null;

        return new Membership(id, type, description, cost, memberId, startDate, endDate);
    }
}
