package com.gymmanagement.dao;

import com.gymmanagement.model.WorkoutClass;
import com.gymmanagement.util.DBConnection;
import com.gymmanagement.util.LoggerUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) for managing workout classes in the database.
 * Provides methods to create, update, retrieve, and delete workout class records.
 * Ensures that only the trainer who owns a class can modify or delete it.
 */
public class WorkoutClassDAO {

    private static final Logger LOGGER = LoggerUtil.getLogger();

    /**
     * Creates a new workout class in the database.
     * Inserts the class details and retrieves the auto-generated ID.
     * 
     * @param workoutClass the WorkoutClass object containing class details
     * @return the created WorkoutClass object with the generated ID, or null if creation fails
     */
    public WorkoutClass createWorkoutClass(WorkoutClass workoutClass) {
        String sql = "INSERT INTO workout_classes " +
                "(workout_class_type, workout_class_description, trainer_id, schedule_time, capacity) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set the prepared statement parameters
            stmt.setString(1, workoutClass.getWorkoutClassType());
            stmt.setString(2, workoutClass.getWorkoutClassDescription());
            stmt.setInt(3, workoutClass.getTrainerId());
            stmt.setTimestamp(4, Timestamp.valueOf(workoutClass.getScheduleTime()));
            stmt.setInt(5, workoutClass.getCapacity());

            // Execute the insert and check if rows were affected
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Creating workout class failed, no rows affected.");
            }

            // Retrieve the auto-generated ID and set it on the workout class object
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    workoutClass.setWorkoutClassId(rs.getInt(1));
                }
            }

            LOGGER.info("Created workout class: " + workoutClass.getWorkoutClassType() +
                    " by trainerId=" + workoutClass.getTrainerId());
            return workoutClass;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating workout class", e);
            return null;
        }
    }

    /**
     * Updates an existing workout class.
     * Only allows updates if the class belongs to the specified trainer (ownership check).
     * 
     * @param workoutClass the WorkoutClass object containing updated details
     * @return true if the update was successful, false otherwise
     */
    public boolean updateWorkoutClass(WorkoutClass workoutClass) {
        String sql = "UPDATE workout_classes SET " +
                "workout_class_type = ?, " +
                "workout_class_description = ?, " +
                "schedule_time = ?, " +
                "capacity = ? " +
                "WHERE workout_class_id = ? AND trainer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the prepared statement parameters
            stmt.setString(1, workoutClass.getWorkoutClassType());
            stmt.setString(2, workoutClass.getWorkoutClassDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(workoutClass.getScheduleTime()));
            stmt.setInt(4, workoutClass.getCapacity());
            stmt.setInt(5, workoutClass.getWorkoutClassId());
            // Check ownership: only allow update if trainer_id matches
            stmt.setInt(6, workoutClass.getTrainerId());

            // Execute the update and check if any rows were affected
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating workout class", e);
            return false;
        }
    }

    /**
     * Deletes a workout class from the database.
     * Only allows deletion if the class belongs to the specified trainer (ownership check).
     * 
     * @param workoutClassId the ID of the workout class to delete
     * @param trainerId the ID of the trainer who owns the class
     * @return true if the deletion was successful, false otherwise
     */
    public boolean deleteWorkoutClass(int workoutClassId, int trainerId) {
        String sql = "DELETE FROM workout_classes WHERE workout_class_id = ? AND trainer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, workoutClassId);
            // Check ownership: only allow deletion if trainer_id matches
            stmt.setInt(2, trainerId);

            // Execute the delete and check if any rows were affected
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting workout class", e);
            return false;
        }
    }

    /**
     * Retrieves all workout classes from the database.
     * Results are ordered by schedule_time.
     * 
     * @return a list of all WorkoutClass objects, or empty list if none found or on error
     */
    public List<WorkoutClass> getAllClasses() {
        String sql = "SELECT * FROM workout_classes ORDER BY schedule_time";
        List<WorkoutClass> classes = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Map each row from the result set to a WorkoutClass object
            while (rs.next()) {
                classes.add(mapRowToWorkoutClass(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all workout classes", e);
        }

        return classes;
    }

    /**
     * Retrieves all workout classes created by a specific trainer.
     * Results are ordered by schedule_time.
     * 
     * @param trainerId the ID of the trainer to retrieve classes for
     * @return a list of WorkoutClass objects created by the trainer, or empty list if none found
     */
    public List<WorkoutClass> getClassesByTrainerId(int trainerId) {
        String sql = "SELECT * FROM workout_classes WHERE trainer_id = ? ORDER BY schedule_time";
        List<WorkoutClass> classes = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, trainerId);
            // Map each row from the result set to a WorkoutClass object
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    classes.add(mapRowToWorkoutClass(rs));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching classes for trainerId=" + trainerId, e);
        }

        return classes;
    }

    /**
     * Helper method to map a database result set row to a WorkoutClass object.
     * Converts SQL Timestamp objects to LocalDateTime for date/time handling.
     * 
     * @param rs the ResultSet positioned at the row to map
     * @return a new WorkoutClass object populated with data from the result set row
     * @throws SQLException if there's an error accessing the result set
     */
    private WorkoutClass mapRowToWorkoutClass(ResultSet rs) throws SQLException {
        int id = rs.getInt("workout_class_id");
        String type = rs.getString("workout_class_type");
        String description = rs.getString("workout_class_description");
        int trainerId = rs.getInt("trainer_id");
        Timestamp ts = rs.getTimestamp("schedule_time");
        int capacity = rs.getInt("capacity");

        // Convert SQL Timestamp to LocalDateTime, handling null values
        LocalDateTime scheduleTime = ts != null ? ts.toLocalDateTime() : null;

        return new WorkoutClass(id, type, description, trainerId, scheduleTime, capacity);
    }
}
