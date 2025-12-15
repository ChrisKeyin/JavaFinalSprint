package com.gymmanagement.service;

import com.gymmanagement.dao.WorkoutClassDAO;
import com.gymmanagement.model.WorkoutClass;
import com.gymmanagement.util.LoggerUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service layer for operations on {@link WorkoutClass} entities.
 * <p>
 * This class enforces simple business rules before delegating to the DAO.
 */
public class WorkoutClassService {

    private final WorkoutClassDAO workoutClassDAO;
    private static final Logger LOGGER = LoggerUtil.getLogger();

    public WorkoutClassService() {
        this.workoutClassDAO = new WorkoutClassDAO();
    }

    public WorkoutClassService(WorkoutClassDAO workoutClassDAO) {
        this.workoutClassDAO = workoutClassDAO;
    }

    /**
     * Creates a new workout class for a trainer.
     *
     * @param trainerId    ID of the trainer
     * @param type         class type (e.g. Yoga, HIIT)
     * @param description  description of the class
     * @param scheduleTime date and time the class starts
     * @param capacity     maximum number of participants
     * @return created {@link WorkoutClass} or {@code null} if creation failed
     */
    public WorkoutClass createClass(int trainerId,
                                    String type,
                                    String description,
                                    LocalDateTime scheduleTime,
                                    int capacity) {

        WorkoutClass workoutClass = new WorkoutClass(
                0,
                type,
                description,
                trainerId,
                scheduleTime,
                capacity
        );

        WorkoutClass created = workoutClassDAO.createWorkoutClass(workoutClass);
        if (created != null) {
            LOGGER.info("Workout class created by trainerId=" + trainerId +
                    ", type=" + type);
        } else {
            LOGGER.warning("Workout class creation failed for trainerId=" + trainerId);
        }
        return created;
    }

    /**
     * Updates an existing workout class.
     *
     * @param workoutClass updated workout class data
     * @return {@code true} if the class was updated; {@code false} otherwise
     */
    public boolean updateClass(WorkoutClass workoutClass) {
        boolean updated = workoutClassDAO.updateWorkoutClass(workoutClass);
        if (updated) {
            LOGGER.info("Workout class updated: id=" + workoutClass.getWorkoutClassId());
        } else {
            LOGGER.warning("Workout class update failed: id=" + workoutClass.getWorkoutClassId());
        }
        return updated;
    }

    /**
     * Deletes a workout class owned by a trainer.
     *
     * @param classId   ID of the class to delete
     * @param trainerId ID of the trainer who owns the class
     * @return {@code true} if deletion succeeded; {@code false} otherwise
     */
    public boolean deleteClass(int classId, int trainerId) {
        boolean deleted = workoutClassDAO.deleteWorkoutClass(classId, trainerId);
        if (deleted) {
            LOGGER.info("Workout class deleted: id=" + classId + ", trainerId=" + trainerId);
        } else {
            LOGGER.warning("Workout class delete failed: id=" + classId + ", trainerId=" + trainerId);
        }
        return deleted;
    }

    /**
     * Returns all workout classes.
     *
     * @return list of classes
     */
    public List<WorkoutClass> getAllClasses() {
        return workoutClassDAO.getAllClasses();
    }

    /**
     * Returns all workout classes owned by a specific trainer.
     *
     * @param trainerId the trainer's ID
     * @return list of classes for that trainer
     */
    public List<WorkoutClass> getClassesForTrainer(int trainerId) {
        return workoutClassDAO.getClassesByTrainerId(trainerId);
    }
}
