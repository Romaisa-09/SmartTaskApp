package com.example.taskapp.data;

import android.content.Context;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.*;

public class TaskRepository {
    private final TaskDao dao;
    private final Executor ex = Executors.newSingleThreadExecutor();

    public TaskRepository(Context c) {
        dao = TaskDatabase.getInstance(c).taskDao();
    }
    public LiveData<List<Task>> getAll() { return dao.getAll(); }
    public void insert(Task t){ ex.execute(() -> dao.insert(t)); }
    public void update(Task t){ ex.execute(() -> dao.update(t)); }
    public void delete(Task t){ ex.execute(() -> dao.delete(t)); }
}
