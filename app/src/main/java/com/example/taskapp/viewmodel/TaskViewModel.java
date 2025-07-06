package com.example.taskapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.*;
import com.example.taskapp.data.Task;
import com.example.taskapp.data.TaskRepository;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private final TaskRepository repo;
    private final LiveData<List<Task>> tasks;

    public TaskViewModel(@NonNull Application app) {
        super(app);
        repo = new TaskRepository(app);
        tasks = repo.getAll();
    }

    // ← Add this getter
    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public void insert(Task t){ repo.insert(t); }
    public void update(Task t){ repo.update(t); }
    public void delete(Task t){ repo.delete(t); }
}
