package com.example.taskapp.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true) public int id;
    public String title;
    public String category;
    public long dueDate;
    public boolean isDone;
    public String description;
}
