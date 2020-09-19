package com.example.swapnilaudichya.tasklist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    static ArrayList<Task> tasks = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, tasks);
        listView.setAdapter(arrayAdapter);


        /***************** SQLite Database code *****************/
        try {
            db = this.openOrCreateDatabase("Tasks",MODE_PRIVATE,null);
            db.execSQL("CREATE TABLE IF NOT EXISTS tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, date VARCHAR, description TEXT)");

            getTaskList(); // Getting tasks from database in array
        }
        catch (Exception e){
            e.printStackTrace();
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),TaskActivity.class);
                int taskId = tasks.get(position).getId();
                intent.putExtra("taskId",taskId);
                intent.putExtra("taskPos",position);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are You Sure?")
                        .setMessage("Do you want to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int taskId = tasks.get(position).getId();
                                db.execSQL("DELETE FROM tasks WHERE id = ?", new String[] {String.valueOf(taskId)});
                                tasks.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Task Deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });
    }

    /***** Method for filling tasks array from DB *****/
    public static void getTaskList(){
        tasks.clear();

        Cursor c = db.rawQuery("SELECT * FROM tasks",null);

        int idIndex = c.getColumnIndex("id");
        int titleIndex = c.getColumnIndex("title");
        int dateIndex = c.getColumnIndex("date");
        int descIndex = c.getColumnIndex("description");

        c.moveToFirst();
        while (!c.isAfterLast()){
            Task task = new Task();
            task.setId(c.getInt(idIndex));
            task.setTitle(c.getString(titleIndex));
            task.setDate(c.getString(dateIndex));
            task.setDesc(c.getString(descIndex));
            tasks.add(task);
            c.moveToNext();
        }
        c.close();

        arrayAdapter.notifyDataSetChanged();
    }


    /***************** Code for Menu ******************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_task_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.add_task){
            Intent intent = new Intent(getApplicationContext(), TaskActivity.class);
            startActivity(intent);

            return true;
        }

        if(item.getItemId() == R.id.clear_tasks){
            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are You Sure?")
                    .setMessage("Do you want to delete all tasks?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.execSQL("DELETE FROM tasks");
                            tasks.clear();
                            arrayAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true;
        }

        return false;
    }

    /***************** Code for Menu END******************/

}
