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
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        holder.textTask.setText(task.getText());
        holder.checkDone.setOnCheckedChangeListener(null);
        holder.checkDone.setChecked(task.getDone());
        applyStrike(holder.textTask, task.getDone());

        holder.checkDone.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            task.setDone(isChecked);
            applyStrike(holder.textTask, isChecked);

            if(isChecked){
                historyTasks.add(0,task);
                int pos = holder.getAdapterPosition();
                tasks.remove(pos);
                notifyItemRemoved(pos);
            }
            else {
                historyTasks.remove(task);
                int pos = holder.getAdapterPosition();
                notifyItemChanged(pos);
            }
        }));
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
        if (task.getDone()){
            historyTasks.remove(task);
        }
        tasks.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreTask(Task task, int position) {
        tasks.add(position,task);

        if(task.getDone()){
            historyTasks.add(0,task);
        }
        notifyItemInserted(position);
    }

    public Task getTask(int position) {
        return tasks.get(position);
    }

    @Override
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
