-- ==========================================================
-- Job Application Tracker — Database Schema
-- ==========================================================

-- 1. Create Database
CREATE DATABASE IF NOT EXISTS job_tracker;
USE job_tracker;

-- 2. Users Table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password CHAR(32) NOT NULL, -- Storing MD5 hash
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Applications Table
CREATE TABLE IF NOT EXISTS applications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    company_name VARCHAR(150) NOT NULL,
    role VARCHAR(150) NOT NULL,
    job_id VARCHAR(50),
    applied_date DATE,
    status ENUM('Applied', 'OA Received', 'Interview Scheduled', 'Rejected', 'Offer Received', 'No Response') DEFAULT 'Applied',
    follow_up_date DATE,
    notes TEXT,
    job_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Note: Password storage uses MD5 in UserDAO for simplicity. 
-- In a real production app, use BCrypt or similar strong hashing.
