package com.example.simplenotes.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.simplenotes.R;
import com.example.simplenotes.data.entity.Note;
import com.example.simplenotes.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteEditorFragment extends Fragment {
    private static final String TAG = "NoteEditorFragment";
    private NoteViewModel noteViewModel;
    private EditText titleEditText;
    private EditText noteEditText;
    private long noteId = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteId = NoteEditorFragmentArgs.fromBundle(getArguments()).getNoteId();
            Log.d(TAG, "Editing note with ID: " + noteId);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_editor, container, false);
        
        titleEditText = view.findViewById(R.id.edit_text_title);
        noteEditText = view.findViewById(R.id.edit_text_note);
        FloatingActionButton fabSave = view.findViewById(R.id.fab_save_note);
        
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        if (noteId != -1) {
            // Edit existing note
            noteViewModel.getNoteById(noteId).observe(getViewLifecycleOwner(), note -> {
                if (note != null) {
                    if (note.getTitle() != null) {
                        titleEditText.setText(note.getTitle());
                    }
                    noteEditText.setText(note.getContent());
                }
            });
        }

        fabSave.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            String content = noteEditText.getText().toString().trim();
            
            if (!content.isEmpty()) {
                if (noteId == -1) {
                    // Create new note
                    Note newNote = new Note(title.isEmpty() ? null : title, content);
                    noteViewModel.insert(newNote);
                    Log.d(TAG, "New note created");
                } else {
                    // Update existing note
                    Note updatedNote = new Note(title.isEmpty() ? null : title, content);
                    updatedNote.setId(noteId);
                    noteViewModel.update(updatedNote);
                    Log.d(TAG, "Note updated, ID: " + noteId);
                }
                Navigation.findNavController(v).navigateUp();
            }
        });

        return view;
    }
} 