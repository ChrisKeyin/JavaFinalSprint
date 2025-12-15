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
 * Data Access Object (DAO) for managing {@link Membership} entities.
 * <p>
 * Provides methods to create memberships, list memberships by user,
 * list all memberships, and calculate total revenue.
 */
public class MembershipDAO {

    private static final Logger LOGGER = LoggerUtil.getLogger();

    /**
     * Inserts a new membership into the database.
     *
     * @param membership the membership to create
     * @return the created membership with generated ID, or {@code null} if creation failed
     */
    public Membership createMembership(Membership membership) {
        String sql = "INSERT INTO memberships " +
                "(membership_type, membership_description, membership_cost, member_id, start_date, end_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, membership.getMembershipType());
            stmt.setString(2, membership.getMembershipDescription());
            stmt.setBigDecimal(3, membership.getMembershipCost());
            stmt.setInt(4, membership.getMemberId());
            stmt.setDate(5, Date.valueOf(membership.getStartDate()));
            if (membership.getEndDate() != null) {
                stmt.setDate(6, Date.valueOf(membership.getEndDate()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Creating membership failed, no rows affected.");
            }

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
     * Returns all memberships for a specific member, ordered by start date descending.
     *
     * @param memberId the ID of the member
     * @return list of memberships for that member
     */
    public List<Membership> getMembershipsByMemberId(int memberId) {
        String sql = "SELECT * FROM memberships WHERE member_id = ? ORDER BY start_date DESC";
        List<Membership> memberships = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
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
     * Returns all memberships in the system.
     *
     * @return list of all memberships
     */
    public List<Membership> getAllMemberships() {
        String sql = "SELECT * FROM memberships ORDER BY membership_id";
        List<Membership> memberships = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

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
     *
     * @return sum of all membership costs, or {@link BigDecimal#ZERO} if none
     */
    public BigDecimal getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(membership_cost), 0) AS total_revenue FROM memberships";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getBigDecimal("total_revenue");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error calculating total membership revenue", e);
        }
        return BigDecimal.ZERO;
    }

    /**
     * Maps a result set row to a {@link Membership} object.
     *
     * @param rs the result set positioned on a membership row
     * @return the mapped {@link Membership}
     * @throws SQLException if an error occurs reading from the result set
     */
    private Membership mapRowToMembership(ResultSet rs) throws SQLException {
        int id = rs.getInt("membership_id");
        String type = rs.getString("membership_type");
        String description = rs.getString("membership_description");
        BigDecimal cost = rs.getBigDecimal("membership_cost");
        int memberId = rs.getInt("member_id");
        Date start = rs.getDate("start_date");
        Date end = rs.getDate("end_date");

        LocalDate startDate = start != null ? start.toLocalDate() : null;
        LocalDate endDate = end != null ? end.toLocalDate() : null;

        return new Membership(id, type, description, cost, memberId, startDate, endDate);
    }
}
