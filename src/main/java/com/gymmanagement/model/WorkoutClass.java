package com.gymmanagement.model;

import java.time.LocalDateTime;

/**
 * Represents a workout class offered at the gym.
 * <p>
 * Each class is associated with a trainer and includes a type, description,
 * scheduled date and time, and maximum capacity.
 */
public class WorkoutClass {

    private int workoutClassId;
    private String workoutClassType;
    private String workoutClassDescription;
    private int trainerId;
    private LocalDateTime scheduleTime;
    private int capacity;

    public WorkoutClass() {
    }

    public WorkoutClass(int workoutClassId, String workoutClassType, String workoutClassDescription,
                        int trainerId, LocalDateTime scheduleTime, int capacity) {
        this.workoutClassId = workoutClassId;
        this.workoutClassType = workoutClassType;
        this.workoutClassDescription = workoutClassDescription;
        this.trainerId = trainerId;
        this.scheduleTime = scheduleTime;
        this.capacity = capacity;
    }

    public int getWorkoutClassId() {
        return workoutClassId;
    }

    public void setWorkoutClassId(int workoutClassId) {
        this.workoutClassId = workoutClassId;
    }

    public String getWorkoutClassType() {
        return workoutClassType;
    }

    public void setWorkoutClassType(String workoutClassType) {
        this.workoutClassType = workoutClassType;
    }

    public String getWorkoutClassDescription() {
        return workoutClassDescription;
    }

    public void setWorkoutClassDescription(String workoutClassDescription) {
        this.workoutClassDescription = workoutClassDescription;
    }

    public int getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }

    public LocalDateTime getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(LocalDateTime scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "WorkoutClass{" +
                "workoutClassId=" + workoutClassId +
                ", workoutClassType='" + workoutClassType + '\'' +
                ", workoutClassDescription='" + workoutClassDescription + '\'' +
                ", trainerId=" + trainerId +
                ", scheduleTime=" + scheduleTime +
                ", capacity=" + capacity +
                '}';
    }
}
