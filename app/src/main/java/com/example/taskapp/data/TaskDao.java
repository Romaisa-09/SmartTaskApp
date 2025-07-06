package com.example.taskapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import java.util.List;

@Dao
public interface TaskDao {
    @Insert void insert(Task t);
    @Update void update(Task t);
    @Delete void delete(Task t);

    @Query("SELECT * FROM tasks ORDER BY dueDate")
    LiveData<List<Task>> getAll();
}
