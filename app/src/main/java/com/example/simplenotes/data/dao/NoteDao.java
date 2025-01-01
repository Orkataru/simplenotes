package com.example.simplenotes.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.simplenotes.data.entity.Note;
import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY modifiedAt DESC")
    LiveData<List<Note>> getAllNotes();

    @Insert
    long insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT * FROM notes WHERE id = :id")
    LiveData<Note> getNoteById(long id);
} 