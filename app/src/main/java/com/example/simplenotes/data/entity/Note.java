package com.example.simplenotes.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String content;
    private long createdAt;
    private long modifiedAt;

    // Default constructor for Room
    public Note() {
        this.createdAt = System.currentTimeMillis();
        this.modifiedAt = System.currentTimeMillis();
    }

    @Ignore
    public Note(String content) {
        this.content = content;
        this.createdAt = System.currentTimeMillis();
        this.modifiedAt = System.currentTimeMillis();
    }

    @Ignore
    public Note(String title, String content) {
        this.title = title;
        this.content = content;
        this.createdAt = System.currentTimeMillis();
        this.modifiedAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { 
        this.title = title;
        this.modifiedAt = System.currentTimeMillis();
    }
    
    public String getContent() { return content; }
    public void setContent(String content) { 
        this.content = content;
        this.modifiedAt = System.currentTimeMillis();
    }
    
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    
    public long getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(long modifiedAt) { this.modifiedAt = modifiedAt; }
} 