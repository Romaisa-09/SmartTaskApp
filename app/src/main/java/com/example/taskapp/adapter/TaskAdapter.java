package com.example.taskapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskapp.R;
import com.example.taskapp.data.Task;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.H> {
    // Backing list is now mutable so we can apply diffs
    private final List<Task> list = new ArrayList<>();
    private final Consumer<Task> onToggle;
    private Consumer<Task> onItemClick;

    public TaskAdapter(Consumer<Task> onToggle) {
        this.onToggle = onToggle;
    }

    /** Optional: call this to handle row‐clicks (edit) */
    public void setOnItemClickListener(Consumer<Task> listener) {
        this.onItemClick = listener;
    }

    @NonNull @Override
    public H onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new H(row);
    }

    @Override
    public void onBindViewHolder(@NonNull H h, int pos) {
        Task t = list.get(pos);

        // Title, description, date
        h.title.setText(t.title);
        h.desc .setText(t.description);
        h.category.setText(t.category);     // new
        h.date .setText(DateFormat.getDateInstance().format(new Date(t.dueDate)));

        // Only checkbox logic — no strikethrough, no alpha!
        h.cb.setOnCheckedChangeListener(null);
        h.cb.setChecked(t.isDone);
        h.cb.setOnCheckedChangeListener((cb, isChecked) -> {
            t.isDone = isChecked;
            onToggle.accept(t);   // will vm.update(t)
        });

        // Row click = edit
        h.itemView.setOnClickListener(v -> {
            if (onItemClick != null) onItemClick.accept(t);
        });
    }



    @Override public int getItemCount() {
        return list.size();
    }

    /** Replace the list with a new one, dispatching a diff so only changed rows update. */
    public void setList(List<Task> newList) {
        DiffUtil.DiffResult diff = DiffUtil.calculateDiff(
                new TaskDiffCallback(this.list, newList)
        );
        // Update backing list
        this.list.clear();
        this.list.addAll(newList);
        // Notify only the diffs
        diff.dispatchUpdatesTo(this);
    }

    /** Access a task for swipe/delete support */
    public Task getTaskAt(int pos) {
        return list.get(pos);
    }

    /** ViewHolder binds all three TextViews plus the CheckBox */
    static class H extends RecyclerView.ViewHolder {
        final TextView title, desc, date;
        final CheckBox cb;
        TextView category;

        H(View v) {
            super(v);
            title = v.findViewById(R.id.tvTitle);
            desc  = v.findViewById(R.id.tvDescription);
            date  = v.findViewById(R.id.tvDate);
            cb    = v.findViewById(R.id.cbDone);
            category = v.findViewById(R.id.tvCategory);

        }
    }
}
