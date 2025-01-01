package com.example.simplenotes.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.simplenotes.R;
import com.example.simplenotes.data.entity.Note;
import java.util.HashSet;
import java.util.Set;

public class NotesAdapter extends ListAdapter<Note, NotesAdapter.NoteViewHolder> {
    private OnNoteClickListener onNoteClickListener;
    private OnNoteLongClickListener onNoteLongClickListener;
    private Set<Long> selectedNoteIds = new HashSet<>();
    private boolean selectionMode = false;
    private OnSelectionEmptyListener onSelectionEmptyListener;

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    public interface OnNoteLongClickListener {
        boolean onNoteLongClick(Note note);
    }

    public interface OnSelectionEmptyListener {
        void onSelectionEmpty();
    }

    public void setOnSelectionEmptyListener(OnSelectionEmptyListener listener) {
        this.onSelectionEmptyListener = listener;
    }

    public void setOnNoteClickListener(OnNoteClickListener listener) {
        this.onNoteClickListener = listener;
    }

    public void setOnNoteLongClickListener(OnNoteLongClickListener listener) {
        this.onNoteLongClickListener = listener;
    }

    public void toggleSelection(Note note) {
        if (selectedNoteIds.contains(note.getId())) {
            selectedNoteIds.remove(note.getId());
            if (selectedNoteIds.isEmpty() && onSelectionEmptyListener != null) {
                onSelectionEmptyListener.onSelectionEmpty();
            }
        } else {
            selectedNoteIds.add(note.getId());
        }
        notifyDataSetChanged();
    }

    public void setSelectionMode(boolean selectionMode) {
        this.selectionMode = selectionMode;
        if (!selectionMode) {
            selectedNoteIds.clear();
        }
        notifyDataSetChanged();
    }

    public boolean isInSelectionMode() {
        return selectionMode;
    }

    public Set<Note> getSelectedNotes() {
        Set<Note> notes = new HashSet<>();
        for (int i = 0; i < getCurrentList().size(); i++) {
            Note note = getItem(i);
            if (selectedNoteIds.contains(note.getId())) {
                notes.add(note);
            }
        }
        return notes;
    }

    public void clearSelections() {
        selectedNoteIds.clear();
        selectionMode = false;
        notifyDataSetChanged();
    }

    public NotesAdapter() {
        super(new DiffUtil.ItemCallback<Note>() {
            @Override
            public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                return oldItem.getContent().equals(newItem.getContent()) &&
                       oldItem.getModifiedAt() == newItem.getModifiedAt();
            }
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = getItem(position);
        
        holder.bind(note, selectedNoteIds.contains(note.getId()));

        holder.itemView.setOnClickListener(v -> {
            if (selectionMode) {
                toggleSelection(note);
            } else if (onNoteClickListener != null) {
                onNoteClickListener.onNoteClick(note);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (onNoteLongClickListener != null && !selectionMode) {
                boolean handled = onNoteLongClickListener.onNoteLongClick(note);
                if (handled) {
                    toggleSelection(note);
                }
                return handled;
            }
            return false;
        });
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewContent;
        private TextView textViewTimestamp;
        private View itemView;

        NoteViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewContent = itemView.findViewById(R.id.text_view_content);
            textViewTimestamp = itemView.findViewById(R.id.text_view_timestamp);
        }

        void bind(Note note, boolean isSelected) {
            if (note.getTitle() != null && !note.getTitle().isEmpty()) {
                textViewTitle.setVisibility(View.VISIBLE);
                textViewTitle.setText(note.getTitle());
            } else {
                textViewTitle.setVisibility(View.GONE);
            }
            textViewContent.setText(note.getContent());
            
            // Format and set the timestamp
            String timestamp = formatTimestamp(note.getModifiedAt());
            textViewTimestamp.setText(timestamp);

            itemView.setBackgroundResource(isSelected ? 
                android.R.color.darker_gray : android.R.color.transparent);
        }

        private String formatTimestamp(long timestamp) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM d, HH:mm", java.util.Locale.getDefault());
            return sdf.format(new java.util.Date(timestamp));
        }
    }
} 