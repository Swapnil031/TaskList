package com.example.swapnilaudichya.tasklist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TaskActivity extends AppCompatActivity {

    EditText taskTitle, taskDate, taskDesc;
    Button addTask;
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

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=taskTitle.getText().toString().trim();
                String date=taskDate.getText().toString().trim();
                String description=taskDesc.getText().toString().trim();

                if (taskId == -1) {
                    MainActivity.db.execSQL("INSERT INTO tasks (title,date,description) VALUES (?,?,?)",
                                                new String[]{title, date, description});

                    MainActivity.getTaskList();
                }
                else {
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
        });
    }
}
