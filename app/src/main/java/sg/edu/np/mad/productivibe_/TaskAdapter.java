package sg.edu.np.mad.productivibe_;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;

    private int selectedPosition = RecyclerView.NO_POSITION;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {     // called by recycler  view when new taskViewHolder instance is needed
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView); //returns new taskViewHolder instance
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) { //bind data to views within taskViewHolder
        Task task = taskList.get(position);
        holder.taskNameTextView.setText(task.getTaskName());
        holder.taskDescriptionTextView.setText(task.getTaskDesc());
        holder.taskStatusButton.setText(task.getStatus());

        holder.taskDurationTextView.setText(String.valueOf(task.getTaskDuration()));

        // format datetime nicely
        // no longer in date format
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
        //String taskDateTime = dateFormat.format(task.getTaskDateTime());
        //String taskDueDateTime = dateFormat.format(task.getTaskDueDateTime());

        String taskDateTime = task.getTaskDateTime();
        String taskDueDateTime = task.getTaskDueDateTime();


        holder.taskDateTimeTextView.setText(taskDateTime);
        holder.taskDueDateTimeTextView.setText(taskDueDateTime);


        //  setting default colours for the button depending on status message
        if(holder.taskStatusButton.getText().equals("Pending")){
            holder.taskStatusButton.setBackgroundColor(Color.parseColor("#FFF67777"));
        }
        else{
            holder.taskStatusButton.setBackgroundColor(Color.parseColor("#FF62D2FD"));
        }
        holder.taskStatusButton.setOnClickListener(new View.OnClickListener() {

            // Updates the status button colour and message after clicking
            @Override
            public void onClick(View v) {
                // get database
                String userName = FirebaseAuth.getInstance().getCurrentUser().getUid();
                // create list of today task based on the user
                FirebaseDatabase fdb = FirebaseDatabase.getInstance();
                DatabaseReference taskDBR = fdb.getReference("tasks/" + userName);


                if(holder.taskStatusButton.getText().equals("Pending")){
                    holder.taskStatusButton.setText("Done");
                    holder.taskStatusButton.setBackgroundColor(Color.parseColor("#FF62D2FD"));
                    task.setStatus("Done");
                    taskDBR.child(String.valueOf(task.getId())).child("status").setValue("Done");

                }
                else{
                    holder.taskStatusButton.setText("Pending");
                    holder.taskStatusButton.setBackgroundColor(Color.parseColor("#FFF67777"));
                    task.setStatus("Pending");
                    taskDBR.child(String.valueOf(task.getId())).child("status").setValue("Pending");
                }
                updateList(taskList);
            }
        });

        if (selectedPosition == position) {
            // Apply your desired styling or visual indication for the selected item
            int selectedColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.lighter_teal);
            holder.itemView.setBackgroundColor(selectedColor); //sets colour when clicked
        } else {
            // Reset the styling for non-selected items
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    } //returns no.of items in taskList


    // update the dataset used for adapter
    public void updateList(List<Task> updatedList) {
        // Separate pending and completed tasks
        List<Task> pendingTasks = new ArrayList<>();
        List<Task> completedTasks = new ArrayList<>();

        for (Task task : updatedList) {
            if (task.getStatus().equals("Pending")) {
                pendingTasks.add(task);
            } else {
                completedTasks.add(task);
            }
        }
        // Sort pending tasks by due date
        Collections.sort(pendingTasks, new Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                return task1.getTaskDueDateTime().compareTo(task2.getTaskDueDateTime());
            }
        });

        // Clear the task list and add pending tasks first, followed by completed tasks
        notifyItemRangeRemoved(0,taskList.size());
        taskList.clear();
        taskList.addAll(pendingTasks);
        taskList.addAll(completedTasks);
        notifyItemRangeInserted(0,taskList.size());

        //notifyDataSetChanged();
    }


    public void clearList(){
        int size = taskList.size();
        taskList.clear();
        notifyItemRangeRemoved(0,size);

    }

    public void setTasks(List<Task> tasks) {
        this.taskList = tasks;
    }

    //making it a static class, it can be accessed directly without the need for an instance of the
    // outer class (TaskAdapter)- improves performance and avoids unnecessary memory usage
    // holds references to the views within each item of the recyclerview
    public static class TaskViewHolder extends RecyclerView.ViewHolder { //view holder for recyclerview items

        public TextView taskNameTextView;
        public TextView taskDescriptionTextView;
        public TextView taskDurationTextView;
        public TextView taskDateTimeTextView;
        public TextView taskDueDateTimeTextView;

        public Button taskStatusButton;

        public TaskViewHolder(View view) {
            super(view);
            taskNameTextView = view.findViewById(R.id.taskNameTextView);
            taskDescriptionTextView = view.findViewById(R.id.taskDescriptionTextView);
            taskStatusButton = view.findViewById(R.id.taskStatusButton);
            taskDurationTextView = view.findViewById(R.id.taskDurationTextView);
            taskDateTimeTextView = view.findViewById(R.id.taskDateTimeTextView);
            taskDueDateTimeTextView = view.findViewById(R.id.taskDueDateTimeTextView);
        }

    }
}