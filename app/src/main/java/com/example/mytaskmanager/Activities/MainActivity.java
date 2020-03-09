package com.example.mytaskmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener ;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.example.mytaskmanager.Database.DataBaseHandler;
import com.example.mytaskmanager.R;
import com.example.mytaskmanager.Models.Task;
import com.example.mytaskmanager.Adapters.TasksListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity{

    FloatingActionButton addButn  ;
    Button delete ;
    ImageButton pickCategory ;
    public TextView itemClickId  , currentCategory ;
    Task[] tasks;
    ListView listView;
    TasksListAdapter tasksListAdapter ;
    DataBaseHandler dataBaseHandler ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting layouts with id
        delete = findViewById(R.id.delete) ;
        addButn= findViewById(R.id.addBtn);
        pickCategory = findViewById(R.id.chooseCateg);
        currentCategory = findViewById(R.id.catTV) ;
        listView = findViewById(R.id.tasksLV);

        //defining Database instance
        dataBaseHandler = new DataBaseHandler(getApplicationContext()) ;

        viewUnfinishedTasks();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 itemClickId = view.findViewById(R.id.taskId);
                Intent intent = new Intent(getApplicationContext() , TaskDetails.class);
                intent.putExtra("itemId" , itemClickId.getText().toString()) ;
                startActivity(intent);
            }
        });

        addButn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAddActivity = new Intent(getApplicationContext() , addTask.class);
                openAddActivity.putExtra("behaviour" , "add") ;
                startActivity(openAddActivity);
            }
        });

        pickCategory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //inflating menu items when button clicked
                PopupMenu popupMenu = new PopupMenu(getApplicationContext() , currentCategory) ;
                popupMenu.getMenuInflater().inflate(R.menu.pop_up  ,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        switch (item.getTitle().toString()){
                            case "All Tasks" :
                                settingActivityViews(item.getTitle().toString());
                                viewUnfinishedTasks();
                                break;

                            case "Done" :
                                settingActivityViews(item.getTitle().toString());
                                viewDoneTasks();
                                break;

                            case "Overdue":
                                settingActivityViews(item.getTitle().toString());
                                viewOverdueTasks();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();  // showing the menu
            }
        });

    }

    public void settingActivityViews(String category){
        currentCategory.setText(category);
        listView.setAdapter(null);
    }

    public void viewUnfinishedTasks ( ){

        // setting list view with the list view adapter
            tasks = dataBaseHandler.getTasks("All Tasks");
            tasksListAdapter = new TasksListAdapter(this, tasks);
            listView.setAdapter(tasksListAdapter);
    }

    public void viewDoneTasks(){

            tasks = dataBaseHandler.getTasks("Done");
            tasksListAdapter = new TasksListAdapter(this, tasks);
            listView.setAdapter(tasksListAdapter);
    }

    public void viewOverdueTasks(){
        tasks = dataBaseHandler.getTasks("Overdue");
        tasksListAdapter = new TasksListAdapter(this, tasks);
        listView.setAdapter(tasksListAdapter);    }

    // Refreshes the tasks after adding a new task
    protected void onResume() {
        super.onResume();
        switch (currentCategory.getText().toString()){
            case "All Tasks" :
                listView.setAdapter(null);
                viewUnfinishedTasks();
                break;

            case "Done" :
                listView.setAdapter(null);
                viewDoneTasks();
                break;

            case "Overdue":
                listView.setAdapter(null);
                viewOverdueTasks();
                break;
        }
    }
}

//notifications