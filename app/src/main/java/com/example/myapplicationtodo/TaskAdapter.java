package com.example.myapplicationtodo;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private final List<Task> tasks;
    private final List<Task> historyTasks;

    public TaskAdapter(List<Task> tasks, List<Task> historyTasks) {
        this.tasks = tasks;
        this.historyTasks = historyTasks;
    }

    @NonNull
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.textTask.setText(task.getText());

        holder.checkDone.setOnCheckedChangeListener(null);
        holder.checkDone.setChecked(task.getDone());
        applyStrike(holder.textTask, task.getDone());

        holder.checkDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setDone(isChecked);
            applyStrike(holder.textTask, isChecked);

            if (isChecked) {
                task.setHistoryReason("done");
                if (!historyTasks.contains(task)) {
                    historyTasks.add(0, task);
                }
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    tasks.remove(pos);
                    notifyItemRemoved(pos);
                }
            } else {
                task.setHistoryReason(null);
                historyTasks.remove(task);
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    private void applyStrike(TextView textTask, Boolean done) {
        if (done) {
            textTask.setPaintFlags(textTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            textTask.setAlpha(0.5f);
        } else {
            textTask.setPaintFlags(textTask.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            textTask.setAlpha(1f);
        }
    }

    public void addTask(String text) {
        tasks.add(0, new Task(text, false));
        notifyItemInserted(0);
    }

    public void removeTask(int position) {
        Task task = tasks.get(position);
        task.setHistoryReason("deleted");
        if (!historyTasks.contains(task)) {
            historyTasks.add(0, task);
        }
        tasks.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreTask(Task task, int position) {
        task.setHistoryReason(null);
        historyTasks.remove(task);
        tasks.add(position, task);
        notifyItemInserted(position);
    }

    public Task getTask(int position) {
        return tasks.get(position);
    }


    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textTask;
        CheckBox checkDone;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textTask = itemView.findViewById(R.id.tvTasks);
            checkDone = itemView.findViewById(R.id.checkTask);
        }
    }
}