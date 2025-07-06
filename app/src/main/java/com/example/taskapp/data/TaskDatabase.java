package com.example.taskapp.data;

import android.content.Context;
import androidx.room.*;

// TaskDatabase.java
@Database(entities={Task.class}, version=2, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();

    private static volatile TaskDatabase INST;
    public static TaskDatabase getInstance(Context c) {
        if (INST == null) {
            INST = Room.databaseBuilder(
                            c.getApplicationContext(),
                            TaskDatabase.class,
                            "task_db"
                    )
                    // ** This will keep your existing data across schema changes **
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INST;
    }
}
