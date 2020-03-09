package com.example.mytaskmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mytaskmanager.Database.DataBaseHandler;
import com.example.mytaskmanager.R;
import com.example.mytaskmanager.Models.Task;

import java.util.Date;
import java.util.Objects;


public class addTask extends AppCompatActivity {

    EditText title, details;
    DatePicker datePicker;
    TimePicker timePicker;
    Button add;
    Task t;
    DataBaseHandler dataBaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        dataBaseHandler = new DataBaseHandler(this);
        title = findViewById(R.id.taskTitle);
        details = findViewById(R.id.taskDetails);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        add = findViewById(R.id.insert);

            //setting views according to the task that will be edited
        if (getIntent().getExtras().getString("behaviour").equals("edit")) {
            t = dataBaseHandler.getTask(Integer.valueOf(getIntent().getExtras().getString("itemId")));
            title.setText(t.getTitle());
            details.setText(t.getDetails());


            // simple way to put the date from the task instance to the date picker
            int c = 0 ,indx =0 , i  ;
            String year="" , month ="", day="" ;

            for (i = 0  ; i < t.getTaskTime().length() ; i++){
                if (t.getTaskTime().charAt(i)=='-'){c++ ;
                    if (c==1) {
                        year = t.getTaskTime().substring(0,i );
                        indx = i;
                    }
                    else if (c==2){
                        month = t.getTaskTime().substring(indx+1 , i ) ;
                        indx= i ;
                    }
                }
                if(t.getTaskTime().charAt(i)==' ') {
                    day = t.getTaskTime().substring(indx + 1, i);
                    indx = i;
                    break;
                }
            }
            datePicker.updateDate(Integer.valueOf(year),Integer.valueOf(month)- 1, Integer.valueOf(day));

            // simple way to put the time from the task instance to the time
            for ( i = indx  ; i < t.getTaskTime().length() ; i++){
                if (t.getTaskTime().charAt(i)!=':'){}
                else {
                    timePicker.setHour(Integer.parseInt(t.getTaskTime().substring(indx+1, i)));
                    timePicker.setMinute(Integer.parseInt(t.getTaskTime().substring(i+1)));
                    break;
                }
            }
        }

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    {
                        // if the button will update case
                        if (getIntent().getExtras().getString("behaviour").equals("edit")&&
                           !title.getText().toString().equals("") && !details.getText().toString().equals("")) {
                            Task task = fillTask();
                            task.setId(t.getId());
                            dataBaseHandler.updateTask(task);

                            Intent intent = new Intent(getApplicationContext() , TaskDetails.class);
                            startActivity(intent);
                        }

                        //if the button will add case
                        // getting sure the Edit texts are not empty
                        else if (title.getText().toString().equals("") || details.getText().toString().equals(""))
                            Toast.makeText(getApplicationContext(), "Please Enter all Data ", Toast.LENGTH_LONG).show();

                        else {
                            //setting the values if the task into a task instance
                            Task task = fillTask();
                            //inserting the instance
                            dataBaseHandler.addTask(task);
                            Toast.makeText(getApplicationContext(), "Your task is added", Toast.LENGTH_LONG).show();
                        }
                        //resetting the views
                        title.setText("");
                        details.setText("");
                    }
                }
            });

        }


    // puts values from the activity views to a task object
    public Task fillTask () {
        Task task = new Task();
        task.setTitle(title.getText().toString());
        task.setDetails(details.getText().toString());
        task.setTaskTime(datePicker.getYear() + "-" + String.valueOf(Integer.valueOf(datePicker.getMonth()) + 1) + "-" + datePicker.getDayOfMonth()
                +" "+String.valueOf(String.format("%02d",timePicker.getHour())) + ":" + String.format("%02d",timePicker.getMinute()));

        return task;
    }
}
