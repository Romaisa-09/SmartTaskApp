package com.example.taskapp;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.taskapp.data.Task;
import com.example.taskapp.viewmodel.TaskViewModel;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class AddEditTaskActivity extends AppCompatActivity {
    private TaskViewModel vm;
    private long dueDate;  // <--- instance field

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        vm = new ViewModelProvider(this).get(TaskViewModel.class);

        EditText etTitle    = findViewById(R.id.etTitle);
        Spinner spCategory  = findViewById(R.id.spCategory);
        Button btnPickDate  = findViewById(R.id.btnPickDate);
        Button btnSave      = findViewById(R.id.btnSave);
        Button btnDelete    = findViewById(R.id.btnDelete);

        // Set up category spinner
        ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(
                this, R.array.categories, android.R.layout.simple_spinner_item
        );
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(catAdapter);

        // Check if we're editing
        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");
        Task current;

        if ("edit".equals(mode)) {
            current = new Task();
            current.id       = intent.getIntExtra("taskId", 0);
            current.title    = intent.getStringExtra("title");
            current.category = intent.getStringExtra("category");
            current.dueDate  = intent.getLongExtra("dueDate", 0);
            current.isDone   = intent.getBooleanExtra("isDone", false);

            etTitle.setText(current.title);
            spCategory.setSelection(catAdapter.getPosition(current.category));

            dueDate = current.dueDate;  // initialize the field
            btnPickDate.setText(
                    android.text.format.DateFormat.getDateFormat(this)
                            .format(dueDate)
            );

            btnSave.setText("Update Task");
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(v -> {
                vm.delete(current);
                finish();
            });

        } else {
            current = null;
            btnDelete.setVisibility(View.GONE);
        }

        // Date picker
        btnPickDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        cal.set(year, month, dayOfMonth);
                        dueDate = cal.getTimeInMillis();  // update the field
                        btnPickDate.setText(
                                android.text.format.DateFormat.getDateFormat(this)
                                        .format(dueDate)
                        );
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // Save or update
        Task finalCurrent = current;
        btnSave.setOnClickListener(v -> {
            Task t = (finalCurrent != null) ? finalCurrent : new Task();
            t.title    = etTitle.getText().toString();
            t.category = spCategory.getSelectedItem().toString();
            t.dueDate  = dueDate;

            if ("edit".equals(mode)) {
                vm.update(t);
            } else {
                vm.insert(t);
            }

            scheduleNotification(t);
            finish();
        });
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleNotification(Task t) {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, DeadlineReceiver.class);
        i.putExtra("taskId",    t.id);
        i.putExtra("taskTitle", t.title);        // ← add this line

        PendingIntent pi = PendingIntent.getBroadcast(
                this, t.id, i, PendingIntent.FLAG_IMMUTABLE
        );
        long triggerAt = t.dueDate - TimeUnit.HOURS.toMillis(1);
        am.setExact(AlarmManager.RTC_WAKEUP, triggerAt, pi);
    }
}
