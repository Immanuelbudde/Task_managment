package com.jobtracker.dao;

import com.jobtracker.model.Application;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ApplicationDAO {

    /**
     * Inserts a new job application into the database.
     * Returns true if successful.
     */
    public boolean addApplication(Application app) {
        String sql = "INSERT INTO applications " +
                     "(user_id, company_name, role, job_id, applied_date, status, follow_up_date, notes, job_url) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, app.getUserId());
            ps.setString(2, app.getCompanyName());
            ps.setString(3, app.getRole());
            ps.setString(4, app.getJobId());
            ps.setDate(5, app.getAppliedDate());
            ps.setString(6, app.getStatus());
            ps.setDate(7, app.getFollowUpDate());
            ps.setString(8, app.getNotes());
            ps.setString(9, app.getJobUrl());
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("ApplicationDAO.addApplication() error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves applications for a user.
     * Optionally filters by status and/or searches by company name (case-insensitive).
     * Pass null or empty string to skip a filter.
     */
    public List<Application> getApplications(int userId, String statusFilter, String searchQuery) {
        List<Application> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT id, user_id, company_name, role, job_id, applied_date, status, " +
            "follow_up_date, notes, job_url, created_at FROM applications WHERE user_id = ?");

        boolean hasStatus = (statusFilter != null && !statusFilter.isEmpty());
        boolean hasSearch = (searchQuery  != null && !searchQuery.isEmpty());

        if (hasStatus) {
            sql.append(" AND status = ?");
        }
        if (hasSearch) {
            sql.append(" AND LOWER(company_name) LIKE ?");
        }
        sql.append(" ORDER BY created_at DESC");

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            int idx = 1;
            ps.setInt(idx++, userId);
            if (hasStatus) {
                ps.setString(idx++, statusFilter);
            }
            if (hasSearch) {
                ps.setString(idx++, "%" + searchQuery.toLowerCase() + "%");
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Application app = new Application();
                app.setId(rs.getInt("id"));
                app.setUserId(rs.getInt("user_id"));
                app.setCompanyName(rs.getString("company_name"));
                app.setRole(rs.getString("role"));
                app.setJobId(rs.getString("job_id"));
                app.setAppliedDate(rs.getDate("applied_date"));
                app.setStatus(rs.getString("status"));
                app.setFollowUpDate(rs.getDate("follow_up_date"));
                app.setNotes(rs.getString("notes"));
                app.setJobUrl(rs.getString("job_url"));
                app.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(app);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("ApplicationDAO.getApplications() error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Updates the status of an application.
     * The userId check prevents users from editing others' applications.
     */
    public boolean updateStatus(int appId, String status, int userId) {
        String sql = "UPDATE applications SET status = ? WHERE id = ? AND user_id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, appId);
            ps.setInt(3, userId);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("ApplicationDAO.updateStatus() error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes an application.
     * The userId check prevents users from deleting others' applications.
     */
    public boolean deleteApplication(int appId, int userId) {
        String sql = "DELETE FROM applications WHERE id = ? AND user_id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, appId);
            ps.setInt(2, userId);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("ApplicationDAO.deleteApplication() error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns a map of status -> count for the dashboard stat cards.
     * All statuses are included (count = 0 if none exist for that status).
     */
    public Map<String, Integer> getStatusCounts(int userId) {
        // Pre-populate with all statuses at 0 so the dashboard always shows all cards
        Map<String, Integer> counts = new LinkedHashMap<>();
        counts.put("Applied",              0);
        counts.put("OA Received",          0);
        counts.put("Interview Scheduled",  0);
        counts.put("Rejected",             0);
        counts.put("Offer Received",       0);
        counts.put("No Response",          0);

        String sql = "SELECT status, COUNT(*) AS cnt FROM applications " +
                     "WHERE user_id = ? GROUP BY status";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String status = rs.getString("status");
                int    cnt    = rs.getInt("cnt");
                counts.put(status, cnt);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("ApplicationDAO.getStatusCounts() error: " + e.getMessage());
        }
        return counts;
    }

    /**
     * Returns the total number of applications for a user.
     */
    public int getTotalCount(int userId) {
        String sql = "SELECT COUNT(*) FROM applications WHERE user_id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                ps.close();
                return count;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("ApplicationDAO.getTotalCount() error: " + e.getMessage());
        }
        return 0;
    }
}
