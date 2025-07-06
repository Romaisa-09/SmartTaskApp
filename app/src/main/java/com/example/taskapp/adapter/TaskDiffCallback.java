package com.example.taskapp.adapter;

import androidx.recyclerview.widget.DiffUtil;
import com.example.taskapp.data.Task;
import java.util.List;
import java.util.Objects;

public class TaskDiffCallback extends DiffUtil.Callback {
    private final List<Task> oldList, newList;
    public TaskDiffCallback(List<Task> oldList, List<Task> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }
    @Override public int getOldListSize() { return oldList.size(); }
    @Override public int getNewListSize() { return newList.size(); }

    @Override
    public boolean areItemsTheSame(int oldPos, int newPos) {
        return oldList.get(oldPos).id == newList.get(newPos).id;
    }

    @Override
    public boolean areContentsTheSame(int oldPos, int newPos) {
        Task o = oldList.get(oldPos);
        Task n = newList.get(newPos);
        // Use Objects.equals(...) for null-safe string comparisons
        return Objects.equals(o.title,       n.title)
                && Objects.equals(o.category,    n.category)
                && Objects.equals(o.description, n.description)
                && o.dueDate == n.dueDate
                && o.isDone  == n.isDone;
    }
}
