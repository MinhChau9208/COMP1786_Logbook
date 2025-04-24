package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public void onClickAdd(View v) {
        Intent i = new Intent(getApplicationContext(), AddTaskActivity.class);
        startActivity(i);
    }
    public static ArrayList<Task> taskList = new ArrayList<Task>();
    DatabaseHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    protected void onStart(){
        super.onStart();
        dbh = new DatabaseHelper(this);
        taskList.clear();
        taskList.addAll(dbh.getDetails());
        ListView lv = findViewById(R.id.listviewTask);
        TaskAdapter adapter = new TaskAdapter(this, taskList);
        lv.setAdapter(adapter);
    }
    public class TaskAdapter extends ArrayAdapter<Task> {

        public TaskAdapter(Context context, ArrayList<Task> tasks) {
            super(context, 0, tasks);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Task t = getItem(position);

            if (convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);

            TextView tvTaskName = convertView.findViewById(R.id.tvTaskName);
            TextView tvDeadline = convertView.findViewById(R.id.tvDeadline);
            TextView tvDuration = convertView.findViewById(R.id.tvDuration);
            TextView tvDescriptions = convertView.findViewById(R.id.tvDescriptions);
            TextView tvCompleted = convertView.findViewById(R.id.tvCompleted);
            Button btnEdit = convertView.findViewById(R.id.btnEdit);
            Button btnDelete = convertView.findViewById(R.id.btnDelete);

            tvTaskName.setText(t.name);
            tvDeadline.setText(t.deadline.toString().substring(0, 10));
            tvDuration.setText(String.valueOf(t.duration));
            tvDescriptions.setText(t.descriptions);
            tvCompleted.setText(t.completed ? "Completed" : "Not Completed");

            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), EditTaskActivity.class);
                intent.putExtra("TASK_ID", dbh.getTaskId(t));
                intent.putExtra("TASK_NAME", t.name);
                intent.putExtra("TASK_DEADLINE", t.deadline.getTime());
                intent.putExtra("TASK_DURATION", t.duration);
                intent.putExtra("TASK_DESCRIPTIONS", t.descriptions);
                intent.putExtra("TASK_COMPLETED", t.completed);
                getContext().startActivity(intent);
            });

            btnDelete.setOnClickListener(v -> {
                dbh.deleteTask(dbh.getTaskId(t));
                taskList.remove(t);
                notifyDataSetChanged();
                Toast.makeText(getContext(), "Task deleted", Toast.LENGTH_SHORT).show();
            });

            return convertView;
        }
    }
}