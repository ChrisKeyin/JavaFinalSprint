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
 * Data Access Object (DAO) for managing {@link WorkoutClass} entities.
 * <p>
 * Trainers use this indirectly through the service layer to create,
 * update, delete, and list workout classes.
 */
public class WorkoutClassDAO {

    private static final Logger LOGGER = LoggerUtil.getLogger();

    /**
     * Inserts a new workout class into the database.
     *
     * @param workoutClass the class to create
     * @return the created class with generated ID, or {@code null} if creation failed
     */
    public WorkoutClass createWorkoutClass(WorkoutClass workoutClass) {
        String sql = "INSERT INTO workout_classes " +
                "(workout_class_type, workout_class_description, trainer_id, schedule_time, capacity) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, workoutClass.getWorkoutClassType());
            stmt.setString(2, workoutClass.getWorkoutClassDescription());
            stmt.setInt(3, workoutClass.getTrainerId());
            stmt.setTimestamp(4, Timestamp.valueOf(workoutClass.getScheduleTime()));
            stmt.setInt(5, workoutClass.getCapacity());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Creating workout class failed, no rows affected.");
            }

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
     * Updates an existing workout class. Trainers may only update classes they own.
     *
     * @param workoutClass updated class data, including ID and trainer ID
     * @return {@code true} if the class was updated; {@code false} otherwise
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

            stmt.setString(1, workoutClass.getWorkoutClassType());
            stmt.setString(2, workoutClass.getWorkoutClassDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(workoutClass.getScheduleTime()));
            stmt.setInt(4, workoutClass.getCapacity());
            stmt.setInt(5, workoutClass.getWorkoutClassId());
            stmt.setInt(6, workoutClass.getTrainerId());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating workout class", e);
            return false;
        }
    }

    /**
     * Deletes a workout class. Trainers may only delete classes they own.
     *
     * @param workoutClassId the ID of the class to delete
     * @param trainerId      the ID of the trainer who owns the class
     * @return {@code true} if the class was deleted; {@code false} otherwise
     */
    public boolean deleteWorkoutClass(int workoutClassId, int trainerId) {
        String sql = "DELETE FROM workout_classes WHERE workout_class_id = ? AND trainer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, workoutClassId);
            stmt.setInt(2, trainerId);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting workout class", e);
            return false;
        }
    }

    /**
     * Returns all workout classes in the system.
     *
     * @return list of all classes
     */
    public List<WorkoutClass> getAllClasses() {
        String sql = "SELECT * FROM workout_classes ORDER BY schedule_time";
        List<WorkoutClass> classes = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                classes.add(mapRowToWorkoutClass(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all workout classes", e);
        }

        return classes;
    }

    /**
     * Returns all classes owned by a specific trainer.
     *
     * @param trainerId the trainer's ID
     * @return list of classes for that trainer
     */
    public List<WorkoutClass> getClassesByTrainerId(int trainerId) {
        String sql = "SELECT * FROM workout_classes WHERE trainer_id = ? ORDER BY schedule_time";
        List<WorkoutClass> classes = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, trainerId);
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
     * Maps a result set row to a {@link WorkoutClass}.
     *
     * @param rs result set positioned on a workout class row
     * @return the mapped {@link WorkoutClass}
     * @throws SQLException if something goes wrong reading from the result set
     */
    private WorkoutClass mapRowToWorkoutClass(ResultSet rs) throws SQLException {
        int id = rs.getInt("workout_class_id");
        String type = rs.getString("workout_class_type");
        String description = rs.getString("workout_class_description");
        int trainerId = rs.getInt("trainer_id");
        Timestamp ts = rs.getTimestamp("schedule_time");
        int capacity = rs.getInt("capacity");

        LocalDateTime scheduleTime = ts != null ? ts.toLocalDateTime() : null;

        return new WorkoutClass(id, type, description, trainerId, scheduleTime, capacity);
    }
}
