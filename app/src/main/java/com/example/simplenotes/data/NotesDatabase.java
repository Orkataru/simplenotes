package com.example.simplenotes.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.simplenotes.data.dao.NoteDao;
import com.example.simplenotes.data.entity.Note;
import net.sqlcipher.database.SupportFactory;

@Database(entities = {Note.class}, version = 1)
public abstract class NotesDatabase extends RoomDatabase {
    private static NotesDatabase instance;
    public abstract NoteDao noteDao();

    public static synchronized NotesDatabase getInstance(Context context) {
        if (instance == null) {
            // Create encryption key
            byte[] passphrase = "your_secure_passphrase".getBytes();
            SupportFactory factory = new SupportFactory(passphrase);

            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    NotesDatabase.class,
                    "notes_database"
                )
                .openHelperFactory(factory)
                .build();
        }
        return instance;
    }
} 