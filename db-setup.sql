-- Job Application Tracker Database Setup
-- Run this script against your MySQL 8.0 server:
--   mysql -u root -p < db-setup.sql

CREATE DATABASE IF NOT EXISTS job_tracker;
USE job_tracker;

-- Drop tables in reverse dependency order
DROP TABLE IF EXISTS applications;
DROP TABLE IF EXISTS users;

-- Users table
CREATE TABLE users (
    id         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(150) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,   -- stored as MD5 hash
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Applications table
CREATE TABLE applications (
    id             INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id        INT          NOT NULL,
    company_name   VARCHAR(150) NOT NULL,
    role           VARCHAR(150) NOT NULL,
    job_id         VARCHAR(100),
    applied_date   DATE,
    status         ENUM(
                       'Applied',
                       'OA Received',
                       'Interview Scheduled',
                       'Rejected',
                       'Offer Received',
                       'No Response'
                   ) NOT NULL DEFAULT 'Applied',
    follow_up_date DATE,
    notes          TEXT,
    job_url        VARCHAR(255),
    created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Sample users (passwords are MD5 hashes)
-- alice password: password123  -> MD5: 482c811da5d5b4bc6d497ffa98491e38
-- bob   password: bob456       -> MD5: e10adc3949ba59abbe56e057f20f883e
INSERT INTO users (name, email, password) VALUES
    ('Alice Johnson', 'alice@example.com', MD5('password123')),
    ('Bob Smith',     'bob@example.com',   MD5('bob456'));

-- Sample applications for Alice (user_id = 1)
INSERT INTO applications
    (user_id, company_name, role, job_id, applied_date, status, follow_up_date, notes, job_url)
VALUES
    (1, 'Google',    'Software Engineer',       'G-2024-001', '2026-03-10', 'Interview Scheduled', '2026-04-01',
     'Phone screen done, waiting for on-site invite.',
     'https://careers.google.com/jobs/results/'),

    (1, 'Amazon',    'SDE II',                  'AMZ-45921',  '2026-03-15', 'OA Received',         '2026-04-10',
     'Online assessment link received. 90 min coding test.',
     'https://www.amazon.jobs/en/'),

    (1, 'Microsoft', 'Full Stack Developer',    'MS-FSD-0321','2026-03-20', 'Applied',              '2026-04-20',
     'Applied via LinkedIn Easy Apply.',
     'https://jobs.microsoft.com/en/'),

    (1, 'Meta',      'Frontend Engineer',       NULL,         '2026-03-05', 'Rejected',             NULL,
     'Received rejection email after initial screen.',
     'https://www.metacareers.com/'),

    (1, 'Netflix',   'Backend Engineer (Java)', 'NF-BE-0089', '2026-02-28', 'No Response',         '2026-03-15',
     'Applied three weeks ago, no reply yet. May follow up.',
     'https://jobs.netflix.com/');
