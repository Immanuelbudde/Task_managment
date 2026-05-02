package com.jobtracker.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Application {

    private int id;
    private int userId;
    private String companyName;
    private String role;
    private String jobId;
    private Date appliedDate;
    private String status;
    private Date followUpDate;
    private String notes;
    private String jobUrl;
    private Timestamp createdAt;

    // No-arg constructor
    public Application() {}

    // Full constructor
    public Application(int id, int userId, String companyName, String role,
                       String jobId, Date appliedDate, String status,
                       Date followUpDate, String notes, String jobUrl, Timestamp createdAt) {
        this.id          = id;
        this.userId      = userId;
        this.companyName = companyName;
        this.role        = role;
        this.jobId       = jobId;
        this.appliedDate = appliedDate;
        this.status      = status;
        this.followUpDate= followUpDate;
        this.notes       = notes;
        this.jobUrl      = jobUrl;
        this.createdAt   = createdAt;
    }

    // Constructor for adding a new application (no id/createdAt yet)
    public Application(int userId, String companyName, String role, String jobId,
                       Date appliedDate, String status, Date followUpDate,
                       String notes, String jobUrl) {
        this.userId      = userId;
        this.companyName = companyName;
        this.role        = role;
        this.jobId       = jobId;
        this.appliedDate = appliedDate;
        this.status      = status;
        this.followUpDate= followUpDate;
        this.notes       = notes;
        this.jobUrl      = jobUrl;
    }

    // Getters
    public int getId()               { return id; }
    public int getUserId()           { return userId; }
    public String getCompanyName()   { return companyName; }
    public String getRole()          { return role; }
    public String getJobId()         { return jobId; }
    public Date getAppliedDate()     { return appliedDate; }
    public String getStatus()        { return status; }
    public Date getFollowUpDate()    { return followUpDate; }
    public String getNotes()         { return notes; }
    public String getJobUrl()        { return jobUrl; }
    public Timestamp getCreatedAt()  { return createdAt; }

    // Setters
    public void setId(int id)                       { this.id = id; }
    public void setUserId(int userId)               { this.userId = userId; }
    public void setCompanyName(String companyName)  { this.companyName = companyName; }
    public void setRole(String role)                { this.role = role; }
    public void setJobId(String jobId)              { this.jobId = jobId; }
    public void setAppliedDate(Date appliedDate)    { this.appliedDate = appliedDate; }
    public void setStatus(String status)            { this.status = status; }
    public void setFollowUpDate(Date followUpDate)  { this.followUpDate = followUpDate; }
    public void setNotes(String notes)              { this.notes = notes; }
    public void setJobUrl(String jobUrl)            { this.jobUrl = jobUrl; }
    public void setCreatedAt(Timestamp createdAt)   { this.createdAt = createdAt; }
}
