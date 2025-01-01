package com.example.simplenotes;

import android.app.Application;
import com.example.simplenotes.utils.SecurityUtils;
import com.example.simplenotes.data.NotesDatabase;

public class NotesApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Initialize security components
        SecurityUtils.init(this);
        
        // Initialize database
        NotesDatabase.getInstance(this);
    }
} 