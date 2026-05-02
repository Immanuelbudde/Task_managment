package com.taskmanager.model;

import java.sql.Timestamp;

public class User {

    private int id;
    private String name;
    private String email;
    private String password;
    private String role; // ADMIN or MEMBER
    private Timestamp createdAt;

    // No-arg constructor
    public User() {}

    // Full constructor
    public User(int id, String name, String email, String password, String role, Timestamp createdAt) {
        this.id        = id;
        this.name      = name;
        this.email     = email;
        this.password  = password;
        this.role      = role;
        this.createdAt = createdAt;
    }

    // Constructor without id (for registration)
    public User(String name, String email, String password, String role) {
        this.name     = name;
        this.email    = email;
        this.password = password;
        this.role     = role;
    }

    // Getters
    public int getId()             { return id; }
    public String getName()        { return name; }
    public String getEmail()       { return email; }
    public String getPassword()    { return password; }
    public String getRole()        { return role; }
    public Timestamp getCreatedAt(){ return createdAt; }

    // Setters
    public void setId(int id)                   { this.id = id; }
    public void setName(String name)             { this.name = name; }
    public void setEmail(String email)           { this.email = email; }
    public void setPassword(String password)     { this.password = password; }
    public void setRole(String role)             { this.role = role; }
    public void setCreatedAt(Timestamp createdAt){ this.createdAt = createdAt; }
}
