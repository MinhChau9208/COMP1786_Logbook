package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_task);

        // declares variables
        DatePicker dp = (DatePicker) findViewById(R.id.dpDeadline);
        Calendar c = Calendar.getInstance();
        dp.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), null);

        //Buttons
        Button savebtn = findViewById(R.id.button2);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    onClickAddTask();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onClickAddTask() throws ParseException {
        //database
        DatabaseHelper dbh = new DatabaseHelper(this);
        String n, des;
        Date dl;
        int d;
        n = ((EditText) findViewById(R.id.etTaskName)).getText().toString();
        des = ((EditText) findViewById(R.id.editTextTextMultiLine)).getText().toString();
        DatePicker dp = (DatePicker) findViewById(R.id.dpDeadline);
        try {
            d = Integer.parseInt(((EditText) findViewById(R.id.etDuration)).getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid duration", Toast.LENGTH_SHORT).show();
            return;
        }
        String dateText = String.valueOf(dp.getDayOfMonth()) + "/" +
                String.valueOf(dp.getMonth() + 1) + "/" +
                String.valueOf(dp.getYear());
        Task t = new Task(n, new SimpleDateFormat("dd/MM/yyyy").parse(dateText), d, des, false);
        long _id = dbh.insertDetails(n, new SimpleDateFormat("dd/MM/yyyy").parse(dateText), d, des, false);
        Toast.makeText(getApplicationContext(), "A task is just created", Toast.LENGTH_LONG).show();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}