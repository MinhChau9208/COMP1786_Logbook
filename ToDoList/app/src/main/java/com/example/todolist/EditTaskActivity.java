package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Date;

public class EditTaskActivity extends AppCompatActivity {

    private long taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_task);

        // Get task details from intent
        Intent intent = getIntent();
        taskId = intent.getLongExtra("TASK_ID", -1);
        String taskName = intent.getStringExtra("TASK_NAME");
        long deadlineMillis = intent.getLongExtra("TASK_DEADLINE", 0);
        int duration = intent.getIntExtra("TASK_DURATION", 0);
        String descriptions = intent.getStringExtra("TASK_DESCRIPTIONS");
        boolean completed = intent.getBooleanExtra("TASK_COMPLETED", false);

        // Initialize views
        EditText etTaskName = findViewById(R.id.etTaskName);
        DatePicker dpDeadline = findViewById(R.id.dpDeadline);
        EditText etDuration = findViewById(R.id.etDuration);
        EditText etDescriptions = findViewById(R.id.editTextTextMultiLine);
        CheckBox cbCompleted = findViewById(R.id.cbCompleted);

        // Set existing task details
        etTaskName.setText(taskName);
        etDuration.setText(String.valueOf(duration));
        etDescriptions.setText(descriptions);
        cbCompleted.setChecked(completed);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(deadlineMillis);
        dpDeadline.init(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null
        );

        // Save button
        Button saveBtn = findViewById(R.id.buttonSave);
        saveBtn.setOnClickListener(v -> {
            try {
                onClickSaveTask();
            } catch (Exception e) {
                Toast.makeText(this, "Error saving task", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onClickSaveTask() {
        DatabaseHelper dbh = new DatabaseHelper(this);
        String name = ((EditText) findViewById(R.id.etTaskName)).getText().toString();
        String descriptions = ((EditText) findViewById(R.id.editTextTextMultiLine)).getText().toString();
        DatePicker dp = findViewById(R.id.dpDeadline);
        CheckBox cbCompleted = findViewById(R.id.cbCompleted);
        int duration;
        try {
            duration = Integer.parseInt(((EditText) findViewById(R.id.etDuration)).getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid duration", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
        Date deadline = calendar.getTime();
        boolean completed = cbCompleted.isChecked();

        int rowsAffected = dbh.updateTask(taskId, name, deadline, duration, descriptions, completed);
        if (rowsAffected > 0) {
            Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to update task", Toast.LENGTH_SHORT).show();
        }
    }
}