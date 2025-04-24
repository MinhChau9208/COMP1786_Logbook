package com.example.unitconverter;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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

    public void onConvertClick(View v){
        Spinner sp1 = (Spinner) findViewById(R.id.spinner);
            String choice1 = sp1.getSelectedItem().toString();
            Spinner sp2 = (Spinner) findViewById(R.id.spinner2);
            String choice2 = sp2.getSelectedItem().toString();

            EditText ed1 = (EditText) findViewById(R.id.editTextText);

        String checkValue = ed1.getText().toString();
        if (checkValue.isBlank()) {
            Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show();
            return;
        }
            double value1 = Double.parseDouble(ed1.getText().toString());
            double value2 = 0;

            switch (choice1) {
                case "Meter":
                    switch (choice2) {
                        case "Meter":
                            value2 = value1;
                            break;
                        case "Millimeter":
                            value2 = value1 * 1000;
                            break;
                        case "Mile":
                            value2 = value1 * 0.000621371192;
                            break;
                        case "Foot":
                            value2 = value1 * 3.2808399;
                            break;
                    }
                    break;
                case "Millimeter":
                    switch (choice2) {
                        case "Meter":
                            value2 = value1 / 1000;
                            break;
                        case "Millimeter":
                            value2 = value1;
                            break;
                        case "Mile":
                            value2 = value1 * 0.000000621371192;
                            break;
                        case "Foot":
                            value2 = value1 * 0.0032808399;
                            break;
                    }
                    break;
                case "Mile":
                    switch (choice2) {
                        case "Meter":
                            value2 = value1 / 0.000621371192;
                            break;
                        case "Millimeter":
                            value2 = value1 / 0.000000621371192;
                            break;
                        case "Mile":
                            value2 = value1;
                            break;
                        case "Foot":
                            value2 = value1 * 5280;
                            break;
                    }
                    break;
                case "Foot":
                    switch (choice2) {
                        case "Meter":
                            value2 = value1 / 3.2808399;
                            break;
                        case "Millimeter":
                            value2 = value1 / 0.0032808399;
                            break;
                        case "Mile":
                            value2 = value1 / 5280;
                            break;
                        case "Foot":
                            value2 = value1;
                            break;
                    }
                    break;
            }
            EditText ed2 = (EditText) findViewById(R.id.editTextText2);
            ed2.setText(String.valueOf(value2));
    }
}