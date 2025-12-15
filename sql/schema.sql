-- Gym Management System - Database Schema
-- ============================================

-- Create database (run this once at the server level, or do it manually in pgAdmin)
-- NOTE: If the DB already exists, skip this step.
-- CREATE DATABASE gym_management_db;

-- Switch to the database:

-- Drop tables if they already exist
-- NOTE: Order matters due to foreign keys
DROP TABLE IF EXISTS memberships;
DROP TABLE IF EXISTS workout_classes;
DROP TABLE IF EXISTS gym_merch;
DROP TABLE IF EXISTS users;

-- USERS TABLE
-- ============================================
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    address VARCHAR(255),
    role VARCHAR(20) NOT NULL  -- 'ADMIN', 'TRAINER', 'MEMBER'
);

-- MEMBERSHIPS TABLE
-- ============================================
CREATE TABLE memberships (
    membership_id SERIAL PRIMARY KEY,
    membership_type VARCHAR(50) NOT NULL,
    membership_description TEXT,
    membership_cost NUMERIC(10, 2) NOT NULL,
    member_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    CONSTRAINT fk_membership_user
        FOREIGN KEY (member_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);


-- WORKOUT CLASSES TABLE
-- ============================================
CREATE TABLE workout_classes (
    workout_class_id SERIAL PRIMARY KEY,
    workout_class_type VARCHAR(50) NOT NULL,
    workout_class_description TEXT,
    trainer_id INT NOT NULL,
    schedule_time TIMESTAMP NOT NULL,
    capacity INT NOT NULL,
    CONSTRAINT fk_workout_trainer
        FOREIGN KEY (trainer_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);


-- GYM MERCH TABLE
-- ============================================
CREATE TABLE gym_merch (
    merch_id SERIAL PRIMARY KEY,
    merch_name VARCHAR(100) NOT NULL,
    merch_type VARCHAR(50) NOT NULL,
    merch_price NUMERIC(10, 2) NOT NULL,
    quantity_in_stock INT NOT NULL
);
