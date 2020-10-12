package com.example.swapnilaudichya.tasklist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class TaskActivity extends AppCompatActivity {

    EditText taskTitle, taskDate, taskDesc;
    Button addTask;
    DatePickerDialog datePicker;
    int taskId, taskPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        taskTitle=findViewById(R.id.taskTitle);
        taskDate=findViewById(R.id.taskDate);
        taskDesc=findViewById(R.id.taskDesc);
        addTask=findViewById(R.id.addTask);

        Intent intent = getIntent();
        taskId = intent.getIntExtra("taskId",-1);
        taskPos = intent.getIntExtra("taskPos",-1);

        if(taskPos != -1){
            taskTitle.setText(MainActivity.tasks.get(taskPos).getTitle());
            taskDate.setText(MainActivity.tasks.get(taskPos).getDate());
            taskDesc.setText(MainActivity.tasks.get(taskPos).getDesc());
        }

        taskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                /***** date picker dialog *****/
                datePicker = new DatePickerDialog(TaskActivity.this,
                        R.style.MyDatePickerDialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                taskDate.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                            }
                        },year,month,day);
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePicker.show();
            }
        });

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=taskTitle.getText().toString().trim();
                String date=taskDate.getText().toString().trim();
                String description=taskDesc.getText().toString().trim();

                if(title.length()<=0) {
                    taskTitle.setError("Cannot be empty");
                    //Toast.makeText(TaskActivity.this, "Enter Title", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (taskId == -1) {
                        MainActivity.db.execSQL("INSERT INTO tasks (title,date,description) VALUES (?,?,?)",
                                new String[]{title, date, description});

                        MainActivity.getTaskList();
                    } else {
                        MainActivity.db.execSQL("UPDATE tasks SET title = ?,date = ?,description = ? WHERE id = ?",
                                new String[]{title, date, description, String.valueOf(taskId)});

                        MainActivity.tasks.get(taskPos).setTitle(title);
                        MainActivity.tasks.get(taskPos).setDate(date);
                        MainActivity.tasks.get(taskPos).setDesc(description);
                    }

                    MainActivity.arrayAdapter.notifyDataSetChanged();

                    Toast.makeText(TaskActivity.this, "Task Saved", Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        });
    }
}
