package com.example.simplenotes.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.simplenotes.data.dao.NoteDao;
import com.example.simplenotes.data.entity.Note;
import net.sqlcipher.database.SupportFactory;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import java.security.KeyStore;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import com.example.simplenotes.utils.SecurityUtils;

@Database(entities = {Note.class}, version = 1)
public abstract class NotesDatabase extends RoomDatabase {
    private static NotesDatabase instance;
    public abstract NoteDao noteDao();

    public static synchronized NotesDatabase getInstance(Context context) {
        if (instance == null) {
            // Generate a unique device-specific key
            byte[] passphrase = SecurityUtils.getDeviceSpecificKey(context);
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