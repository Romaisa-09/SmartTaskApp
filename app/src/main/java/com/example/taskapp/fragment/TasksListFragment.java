package com.example.taskapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskapp.AddEditTaskActivity;
import com.example.taskapp.R;
import com.example.taskapp.adapter.TaskAdapter;
import com.example.taskapp.data.Task;
import com.example.taskapp.viewmodel.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

public class TasksListFragment extends Fragment {
    private TaskViewModel vm;
    private TaskAdapter adapter;
    private final List<Task> allTasks = new ArrayList<>();
    private int filterState = 0; // 0=All,1=Completed,2=Pending
    private int categoryFilter = 0; // 0 = All Categories, 1 = Work, 2 = Personal, ...

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        vm = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        adapter = new TaskAdapter(task -> vm.update(task));

        RecyclerView rv = root.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        // Observe and re-apply filter on data changes
        vm.getTasks().observe(getViewLifecycleOwner(), list -> {
            allTasks.clear();
            allTasks.addAll(list);
            applyFilter();
        });

        // Edit on item tap
        adapter.setOnItemClickListener(task -> {
            Intent edit = new Intent(getActivity(), AddEditTaskActivity.class);
            edit.putExtra("mode", "edit");
            edit.putExtra("taskId",   task.id);
            edit.putExtra("title",    task.title);
            edit.putExtra("category", task.category);
            edit.putExtra("description", task.description);
            edit.putExtra("dueDate",  task.dueDate);
            edit.putExtra("isDone",   task.isDone);
            startActivity(edit);
        });

        // Swipe to delete
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                // We don't support drag & drop
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Task toDelete = adapter.getTaskAt(pos);
                    vm.delete(toDelete);
                }
            }
        });
        helper.attachToRecyclerView(rv);

        // Filter button
        Button btnFilter = root.findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(v -> showFilterDialog());
    }

    private void showFilterDialog() {
        // 1) Status options
        String[] statusOpts = { "All", "Completed", "Pending" };

        // 2) Category options (dynamically from your string-array)
        String[] categories = getResources().getStringArray(R.array.categories);
        String[] categoryOpts = new String[categories.length + 1];
        categoryOpts[0] = "All Categories";
        System.arraycopy(categories, 0, categoryOpts, 1, categories.length);

        // Build a two-section AlertDialog
        new AlertDialog.Builder(requireContext())
                .setTitle("Filter by status")
                .setSingleChoiceItems(statusOpts, filterState, (dlg, which) -> {
                    filterState = which;
                    dlg.dismiss();
                    // After status chosen, show category dialog
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Filter by category")
                            .setSingleChoiceItems(categoryOpts, categoryFilter, (d2, catWhich) -> {
                                categoryFilter = catWhich;  // 0 = all, 1 = first category, etc
                                d2.dismiss();
                                applyFilter();
                            })
                            .show();
                })
                .show();
    }


    private void applyFilter() {
        List<Task> filtered = new ArrayList<>();
        for (Task t : allTasks) {
            // Status filter
            if (filterState == 1 && !t.isDone) continue;
            if (filterState == 2 &&  t.isDone) continue;

            // Category filter (skip index 0 = all)
            if (categoryFilter > 0) {
                String wanted = getResources()
                        .getStringArray(R.array.categories)[categoryFilter - 1];
                if (!t.category.equals(wanted)) continue;
            }

            filtered.add(t);
        }
        adapter.setList(filtered);
    }

}
