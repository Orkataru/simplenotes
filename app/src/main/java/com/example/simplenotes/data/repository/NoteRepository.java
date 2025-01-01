package com.example.simplenotes.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.simplenotes.data.NotesDatabase;
import com.example.simplenotes.data.dao.NoteDao;
import com.example.simplenotes.data.entity.Note;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;
    private ExecutorService executorService;

    public NoteRepository(Application application) {
        NotesDatabase database = NotesDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public void insert(Note note) {
        executorService.execute(() -> noteDao.insert(note));
    }

    public void update(Note note) {
        executorService.execute(() -> noteDao.update(note));
    }

    public void delete(Note note) {
        executorService.execute(() -> noteDao.delete(note));
    }

    public LiveData<Note> getNoteById(long id) {
        return noteDao.getNoteById(id);
    }
} 