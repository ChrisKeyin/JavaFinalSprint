package com.gymmanagement.model;

import java.time.LocalDateTime;

/**
 * Represents a workout class offered by the gym.
 * Contains information about the class type, trainer, schedule, and capacity.
 * Workout classes are created and managed by trainers.
 */
public class WorkoutClass {

    // Class identification and details
    private int workoutClassId;
    private String workoutClassType;
    private String workoutClassDescription;
    
    // Class scheduling and capacity
    private int trainerId;
    private LocalDateTime scheduleTime;
    private int capacity;

    /**
     * Default constructor for WorkoutClass.
     * Creates an empty workout class instance.
     */
    public WorkoutClass() {
    }

    /**
     * Constructor for WorkoutClass with all properties.
     * 
     * @param workoutClassId the unique identifier for the workout class
     * @param workoutClassType the type of workout (e.g., Yoga, HIIT, Pilates)
     * @param workoutClassDescription a description of the workout class and its benefits
     * @param trainerId the ID of the trainer who leads this class
     * @param scheduleTime the date and time when the class is scheduled
     * @param capacity the maximum number of members who can attend this class
     */
    public WorkoutClass(int workoutClassId, String workoutClassType, String workoutClassDescription,
                        int trainerId, LocalDateTime scheduleTime, int capacity) {
        this.workoutClassId = workoutClassId;
        this.workoutClassType = workoutClassType;
        this.workoutClassDescription = workoutClassDescription;
        this.trainerId = trainerId;
        this.scheduleTime = scheduleTime;
        this.capacity = capacity;
    }

    // Getters and Setters

    /**
     * Gets the unique workout class ID.
     */
    public int getWorkoutClassId() {
        return workoutClassId;
    }

    /**
     * Sets the workout class ID.
     */
    public void setWorkoutClassId(int workoutClassId) {
        this.workoutClassId = workoutClassId;
    }

    /**
     * Gets the type of workout class.
     */
    public String getWorkoutClassType() {
        return workoutClassType;
    }

    /**
     * Sets the type of workout class.
     */
    public void setWorkoutClassType(String workoutClassType) {
        this.workoutClassType = workoutClassType;
    }

    /**
     * Gets the description of the workout class.
     */
    public String getWorkoutClassDescription() {
        return workoutClassDescription;
    }

    /**
     * Sets the description of the workout class.
     */
    public void setWorkoutClassDescription(String workoutClassDescription) {
        this.workoutClassDescription = workoutClassDescription;
    }

    /**
     * Gets the ID of the trainer who leads this class.
     */
    public int getTrainerId() {
        return trainerId;
    }

    /**
     * Sets the ID of the trainer who leads this class.
     */
    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }

    /**
     * Gets the scheduled date and time of the workout class.
     */
    public LocalDateTime getScheduleTime() {
        return scheduleTime;
    }

    /**
     * Sets the scheduled date and time of the workout class.
     */
    public void setScheduleTime(LocalDateTime scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    /**
     * Gets the maximum capacity of the workout class.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the maximum capacity of the workout class.
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Returns a string representation of the WorkoutClass object.
     * Includes all workout class details in a readable format.
     */
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
