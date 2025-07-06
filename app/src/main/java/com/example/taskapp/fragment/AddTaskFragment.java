package com.example.taskapp.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.taskapp.R;
import com.example.taskapp.data.Task;
import com.example.taskapp.viewmodel.TaskViewModel;

import java.util.Calendar;

public class AddTaskFragment extends Fragment {
    private TaskViewModel vm;
    private long dueDate;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup parent,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_task, parent, false);
    }

    @Override public void onViewCreated(@NonNull View v, @Nullable Bundle b) {
        super.onViewCreated(v, b);

        vm = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        EditText etTitle       = v.findViewById(R.id.etTitle);
        EditText etDescription = v.findViewById(R.id.etDescription);
        Spinner spCat          = v.findViewById(R.id.spCat);
        Button btnDate         = v.findViewById(R.id.btnDate);
        Button btnSave         = v.findViewById(R.id.btnSave);

        // Initialize spinner (if not done via XML entries only)
        ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.categories, android.R.layout.simple_spinner_item
        );
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCat.setAdapter(catAdapter);

        btnDate.setOnClickListener(x -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(requireContext(),
                    (dp, year, month, day) -> {
                        c.set(year, month, day);
                        dueDate = c.getTimeInMillis();
                        btnDate.setText(
                                android.text.format.DateFormat
                                        .getDateFormat(requireContext())
                                        .format(c.getTime())
                        );
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        btnSave.setOnClickListener(x -> {
            Task t = new Task();
            t.title       = etTitle.getText().toString();
            t.description = etDescription.getText().toString();
            t.category    = spCat.getSelectedItem().toString();
            t.dueDate     = dueDate;
            t.isDone      = false;

            vm.insert(t);
            Toast.makeText(requireContext(), R.string.save_task, Toast.LENGTH_SHORT).show();

            // reset fields
            etTitle.setText("");
            etDescription.setText("");
            btnDate.setText(R.string.pick_date);
            spCat.setSelection(0);
        });
    }
}
