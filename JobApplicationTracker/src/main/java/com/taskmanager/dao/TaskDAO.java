package com.taskmanager.dao;

import com.taskmanager.model.Task;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskDAO {

    public boolean createTask(Task task) {
        String sql = "INSERT INTO tasks (project_id, assigned_to, title, description, due_date, status, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, task.getProjectId());
            ps.setInt(2, task.getAssignedTo());
            ps.setString(3, task.getTitle());
            ps.setString(4, task.getDescription());
            ps.setDate(5, task.getDueDate());
            ps.setString(6, task.getStatus());
            ps.setString(7, task.getPriority());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Task> getTasksByUser(int userId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.*, p.name as project_name, u.name as assigned_name FROM tasks t " +
                     "JOIN projects p ON t.project_id = p.id " +
                     "JOIN users u ON t.assigned_to = u.id " +
                     "WHERE t.assigned_to = ? ORDER BY t.due_date ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Task task = mapRow(rs);
                    task.setProjectName(rs.getString("project_name"));
                    task.setAssignedToName(rs.getString("assigned_name"));
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.*, p.name as project_name, u.name as assigned_name FROM tasks t " +
                     "JOIN projects p ON t.project_id = p.id " +
                     "JOIN users u ON t.assigned_to = u.id " +
                     "ORDER BY t.due_date ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Task task = mapRow(rs);
                task.setProjectName(rs.getString("project_name"));
                task.setAssignedToName(rs.getString("assigned_name"));
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public boolean updateStatus(int taskId, String status) {
        String sql = "UPDATE tasks SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, taskId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTask(int taskId) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taskId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Integer> getStatusCounts(int userId, String role) {
        Map<String, Integer> counts = new HashMap<>();
        String sql = "SELECT status, COUNT(*) as count FROM tasks ";
        if ("MEMBER".equals(role)) {
            sql += "WHERE assigned_to = ? ";
        }
        sql += "GROUP BY status";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if ("MEMBER".equals(role)) {
                ps.setInt(1, userId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    counts.put(rs.getString("status"), rs.getInt("count"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counts;
    }

    private Task mapRow(ResultSet rs) throws SQLException {
        return new Task(
            rs.getInt("id"),
            rs.getInt("project_id"),
            rs.getInt("assigned_to"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getDate("due_date"),
            rs.getString("status"),
            rs.getString("priority"),
            rs.getTimestamp("created_at")
        );
    }
}
