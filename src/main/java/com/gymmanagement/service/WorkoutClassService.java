package com.gymmanagement.service;

import com.gymmanagement.dao.WorkoutClassDAO;
import com.gymmanagement.model.WorkoutClass;
import com.gymmanagement.util.LoggerUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

public class WorkoutClassService {

    private final WorkoutClassDAO workoutClassDAO;
    private static final Logger LOGGER = LoggerUtil.getLogger();

    public WorkoutClassService() {
        this.workoutClassDAO = new WorkoutClassDAO();
    }

    public WorkoutClassService(WorkoutClassDAO workoutClassDAO) {
        this.workoutClassDAO = workoutClassDAO;
    }

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

    public boolean updateClass(WorkoutClass workoutClass) {
        boolean updated = workoutClassDAO.updateWorkoutClass(workoutClass);
        if (updated) {
            LOGGER.info("Workout class updated: id=" + workoutClass.getWorkoutClassId());
        } else {
            LOGGER.warning("Workout class update failed: id=" + workoutClass.getWorkoutClassId());
        }
        return updated;
    }

    public boolean deleteClass(int classId, int trainerId) {
        boolean deleted = workoutClassDAO.deleteWorkoutClass(classId, trainerId);
        if (deleted) {
            LOGGER.info("Workout class deleted: id=" + classId + ", trainerId=" + trainerId);
        } else {
            LOGGER.warning("Workout class delete failed: id=" + classId + ", trainerId=" + trainerId);
        }
        return deleted;
    }

    public List<WorkoutClass> getAllClasses() {
        return workoutClassDAO.getAllClasses();
    }

    public List<WorkoutClass> getClassesForTrainer(int trainerId) {
        return workoutClassDAO.getClassesByTrainerId(trainerId);
    }
}
