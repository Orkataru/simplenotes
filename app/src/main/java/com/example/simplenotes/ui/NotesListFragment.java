package com.example.simplenotes.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DividerItemDecoration;
import com.example.simplenotes.R;
import com.example.simplenotes.data.entity.Note;
import com.example.simplenotes.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class NotesListFragment extends Fragment {
    private static final String TAG = "NotesListFragment";
    private NoteViewModel noteViewModel;
    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private MenuItem deleteMenuItem;
    private List<Note> allNotes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotesAdapter();
        recyclerView.setAdapter(adapter);

        // Add divider between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.note_divider, null));
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Set click listener for notes
        adapter.setOnNoteClickListener(note -> {
            if (!adapter.isInSelectionMode()) {
                Log.d(TAG, "Note clicked, ID: " + note.getId());
                try {
                    Navigation.findNavController(view).navigate(
                        NotesListFragmentDirections.actionListToEditor().setNoteId(note.getId())
                    );
                } catch (Exception e) {
                    Log.e(TAG, "Navigation failed", e);
                }
            }
        });

        // Set long click listener for notes
        adapter.setOnNoteLongClickListener(note -> {
            if (!adapter.isInSelectionMode()) {
                adapter.setSelectionMode(true);
                updateUI();
                return true;
            }
            return false;
        });

        // Set selection empty listener
        adapter.setOnSelectionEmptyListener(() -> {
            exitSelectionMode();
        });

        FloatingActionButton fab = view.findViewById(R.id.fab_add_note);
        fab.setOnClickListener(v -> {
            Log.d(TAG, "FAB clicked - attempting to navigate to editor");
            try {
                Navigation.findNavController(v).navigate(
                    NotesListFragmentDirections.actionListToEditor()
                );
            } catch (Exception e) {
                Log.e(TAG, "Navigation failed", e);
            }
        });

        FloatingActionButton fabSearch = view.findViewById(R.id.fab_search);
        fabSearch.setOnClickListener(v -> showSearchDialog());

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(getViewLifecycleOwner(), notes -> {
            allNotes = notes;
            adapter.submitList(notes);
            if (adapter.isInSelectionMode() && adapter.getSelectedNotes().isEmpty()) {
                exitSelectionMode();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.notes_list_menu, menu);
        deleteMenuItem = menu.findItem(R.id.action_delete);
        updateUI();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            showDeleteConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        Set<Note> selectedNotes = adapter.getSelectedNotes();
        if (selectedNotes.isEmpty()) {
            exitSelectionMode();
            return;
        }

        new AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete_confirmation_title)
            .setMessage(getString(R.string.delete_confirmation_message))
            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                for (Note note : selectedNotes) {
                    noteViewModel.delete(note);
                }
                exitSelectionMode();
            })
            .setNegativeButton(android.R.string.no, (dialog, which) -> exitSelectionMode())
            .show();
    }

    private void exitSelectionMode() {
        adapter.clearSelections();
        updateUI();
    }

    private void updateUI() {
        if (deleteMenuItem != null) {
            deleteMenuItem.setVisible(adapter.isInSelectionMode());
        }
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (adapter.isInSelectionMode()) {
                activity.getSupportActionBar().setTitle(getString(R.string.select_notes));
            } else {
                activity.getSupportActionBar().setTitle(R.string.app_name);
            }
        }
    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        SearchView searchView = new SearchView(getContext());
        searchView.setIconified(false);
        searchView.setQueryHint("Search notes...");
        
        builder.setView(searchView);
        AlertDialog dialog = builder.create();
        
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterNotes(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNotes(newText);
                return true;
            }
        });

        searchView.setOnCloseListener(() -> {
            dialog.dismiss();
            return true;
        });
        
        dialog.show();
    }

    private void filterNotes(String query) {
        if (allNotes == null) {
            return;
        }

        List<Note> notesToShow;
        if (query == null || query.isEmpty()) {
            notesToShow = new ArrayList<>(allNotes);
        } else {
            String lowercaseQuery = query.toLowerCase();
            notesToShow = new ArrayList<>();
            
            for (Note note : allNotes) {
                String title = note.getTitle() != null ? note.getTitle().toLowerCase() : "";
                String content = note.getContent() != null ? note.getContent().toLowerCase() : "";
                
                if (title.contains(lowercaseQuery) || content.contains(lowercaseQuery)) {
                    notesToShow.add(note);
                }
            }
        }
        
        adapter.submitList(new ArrayList<>(notesToShow));
    }
} 