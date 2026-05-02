package com.jobtracker.dao;

import com.jobtracker.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    /**
     * Registers a new user. Password is stored as MD5 hash.
     * Returns true if registration succeeded, false if email already exists or error.
     */
    public boolean register(User user) {
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, MD5(?), ?)";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("UserDAO.register() error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns the User object if credentials match, null otherwise.
     * Password is compared against its MD5 hash stored in DB.
     */
    public User login(String email, String password) {
        String sql = "SELECT id, name, email, password, role, created_at FROM users " +
                     "WHERE email = ? AND password = MD5(?)";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                rs.close();
                ps.close();
                return user;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("UserDAO.login() error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Checks whether the given email is already registered.
     */
    public boolean emailExists(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();
            rs.close();
            ps.close();
            return exists;
        } catch (SQLException e) {
            System.err.println("UserDAO.emailExists() error: " + e.getMessage());
            return false;
        }
    }
}
