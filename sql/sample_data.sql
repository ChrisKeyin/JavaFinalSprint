-- ============================================
-- Gym Management System - Sample Data
-- ============================================
-- IMPORTANT:
-- Users, memberships, and workout classes are
-- usually created via the Java application so
-- that passwords are properly hashed using BCrypt.
--
-- This script focuses on simple sample data for
-- GYM MERCH only. You can add more as needed.
-- ============================================

-- Make sure you are connected to gym_management_db

-- Sample merchandise
-- ============================================
INSERT INTO gym_merch (merch_name, merch_type, merch_price, quantity_in_stock) VALUES
('Gym T-Shirt', 'Gear', 24.99, 30),
('Water Bottle', 'Drink', 9.99, 50),
('Protein Bar', 'Food', 3.49, 100),
('Yoga Mat', 'Gear', 39.99, 20);
