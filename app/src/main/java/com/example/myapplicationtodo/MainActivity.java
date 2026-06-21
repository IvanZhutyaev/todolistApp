package com.example.myapplicationtodo;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationtodo.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private TaskAdapter adapter;
    private final List<Task> historyTasks = new ArrayList<>();

    private final List<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adapter = new TaskAdapter(tasks, historyTasks);
        binding.rvTasks.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTasks.setAdapter(adapter);
        binding.addTask.setOnClickListener(v->showAddDialog());
        binding.historyTask.setOnClickListener(v -> showHistory());

        initSwipeDelete();

    }

    private void showHistory() {
        if (historyTasks.isEmpty()){
            new AlertDialog.Builder(this)
                    .setTitle("History")
                    .setMessage("EMPTY")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Task task:historyTasks){
            sb.append("Task ").append(task.getText()).append(" complete").append("\n");
        }

        TextView textView = new TextView(this);
        textView.setText(sb.toString().trim());
        textView.setPadding(40,20,40,20);
        textView.setTextSize(16);
        new AlertDialog.Builder(this)
                .setTitle("History")
                .setView(textView)
                .setPositiveButton("Clear History",(dialog, which)->{
                    historyTasks.clear();
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton("Close", null).show();
    }



    private void showAddDialog() {
        EditText input = new EditText(this);
        input.setHint("Input task");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        new AlertDialog.Builder(this)
                .setTitle("New Task")
                .setView(input)
                .setPositiveButton("Add", ((dialog, which) -> {
                    String text = input.getText().toString().trim();
                    if (!text.isEmpty()) {
                        adapter.addTask(text);
                        binding.rvTasks.scrollToPosition(0);
                    }
                }))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void initSwipeDelete() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();
                Task removedTask = adapter.getTask(position);
                adapter.removeTask(position);
                Snackbar.make(binding.getRoot(), "Task was deleted", Snackbar.LENGTH_LONG)
                        .setAction("Cancel", v -> {
                            adapter.restoreTask(removedTask, position);
                        }).show();

            }
        };
        new ItemTouchHelper(callback).attachToRecyclerView(binding.rvTasks);
    }
}