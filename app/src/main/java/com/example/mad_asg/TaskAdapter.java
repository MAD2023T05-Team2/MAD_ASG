package com.example.mad_asg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;
    private Context context;
    private TaskAdapterListener listener;

    public interface TaskAdapterListener {
        void onTaskEdit(Task task);
        void onTaskDelete(Task task);
    }

    public TaskAdapter(List<Task> taskList, Context context, TaskAdapterListener listener) {
        this.taskList = taskList;
        this.context = context;
        this.listener = listener;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        TaskViewHolder viewHolder = new TaskViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskNameTextView.setText("Task Name: " + task.getTaskName()+ " (" + task.getStatus() + ")");
        holder.taskDescTextView.setText("Description: " + task.getTaskDesc());
        holder.taskDateTextView.setText("Task Date: " + task.getTaskDate());
        String startTime = "Start Time: " + task.getTaskStartTime();
        String endTime = "End Time: " + task.getTaskEndTime();
        holder.taskTimeTextView.setText(startTime + " - " + endTime);
        holder.taskAlertTextView.setText("Alert: " + task.getAlert());
        holder.taskAlertDateTimeTextView.setText("Alert Date & Time: " + task.getAlertDateTime());
        holder.taskRepeatTextView.setText("Repeat: " + task.getRepeat());
        holder.taskRecurringDurationTextView.setText("Recurring Frequency: " + task.getRecurringDuration());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTaskEdit(task);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTaskDelete(task);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskNameTextView;
        TextView taskDescTextView;
        TextView taskDateTextView;
        TextView taskTimeTextView;
        TextView taskAlertTextView;
        TextView taskAlertDateTimeTextView;
        TextView taskRepeatTextView;

        TextView taskRecurringDurationTextView;
        Button editButton;
        Button deleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskNameTextView = itemView.findViewById(R.id.taskNameTextView);
            taskDescTextView = itemView.findViewById(R.id.taskDescTextView);
            taskDateTextView = itemView.findViewById(R.id.taskDateTextView);
            taskTimeTextView = itemView.findViewById(R.id.taskTimeTextView);
            taskAlertTextView = itemView.findViewById(R.id.taskAlertTextView);
            taskAlertDateTimeTextView = itemView.findViewById(R.id.taskAlertDateTimeTextView);
            taskRepeatTextView = itemView.findViewById(R.id.taskRepeatTextView);
            taskRecurringDurationTextView = itemView.findViewById(R.id.taskRecurringDurationTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }




    }
}
