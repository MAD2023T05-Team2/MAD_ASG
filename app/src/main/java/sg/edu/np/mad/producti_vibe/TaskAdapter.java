package sg.edu.np.mad.producti_vibe;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;
    private OnItemClickListener listener;
    private OnEditClickListener editListener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public interface OnEditClickListener {
        void onEditClick(int position);
    }
    public TaskAdapter(List<Task> taskList, OnItemClickListener listener, OnEditClickListener editListener) {
        this.taskList = taskList;
        this.listener = listener;
        this.editListener = editListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.editListener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskNameTextView.setText(task.getTaskName());
        holder.taskDescriptionTextView.setText(task.getTaskDesc());
        holder.taskStatusTextView.setText(task.getStatus());

        // format datetime nicely
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
        String taskDateTime = dateFormat.format(task.getTaskDateTime());
        String taskDueDateTime = dateFormat.format(task.getTaskDueDateTime());

        holder.taskDateTimeTextView.setText(taskDateTime);
        holder.taskDueDateTimeTextView.setText(taskDueDateTime);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onItemClick(adapterPosition);
                }
            }
        });

        if (selectedPosition == position) {
            // Apply your desired styling or visual indication for the selected item
            int selectedColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.lighter_teal);
            holder.itemView.setBackgroundColor(selectedColor);
        } else {
            // Reset the styling for other items
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setSelectedPosition(int position) {
        int previousPosition = selectedPosition;
        selectedPosition = position;

        // Notify the adapter about item changes to update the UI
        if (previousPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(previousPosition);
        }
        if (selectedPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(selectedPosition);
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        // CheckBox check_box;
        public TextView taskNameTextView;
        public TextView taskDescriptionTextView;
        public TextView taskStatusTextView;
        public TextView taskDurationTextView;
        public TextView taskDateTimeTextView;
        public TextView taskDueDateTimeTextView;

        public Button editButton;

        public TaskViewHolder(View view) {
            super(view);
            taskNameTextView = view.findViewById(R.id.taskNameTextView);
            taskDescriptionTextView = view.findViewById(R.id.taskDescriptionTextView);
            taskStatusTextView = view.findViewById(R.id.taskStatusTextView);
            taskDurationTextView = view.findViewById(R.id.taskDurationTextView);
            taskDateTimeTextView = view.findViewById(R.id.taskDateTimeTextView);
            taskDueDateTimeTextView = view.findViewById(R.id.taskDueDateTimeTextView);
            editButton = itemView.findViewById(R.id.editButton);
            // check_box =  view.findViewById(R.id.checkBox);
        }
    }
    public void setFilter(List<Task> filterTasks) {
        List<Task> filterLists = new ArrayList<>();
        filterLists.addAll(filterTasks);
        notifyDataSetChanged();
    }
}


