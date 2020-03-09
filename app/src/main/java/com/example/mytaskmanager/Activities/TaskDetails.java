package com.example.mytaskmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytaskmanager.Database.DataBaseHandler;
import com.example.mytaskmanager.R;
import com.example.mytaskmanager.Models.Task;

public class TaskDetails extends AppCompatActivity implements View.OnClickListener {

DataBaseHandler dataBaseHandler ;
ImageButton delete , edit ;
Task activityTask ;
TextView title , details  , time ;
CheckBox done ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        dataBaseHandler = new DataBaseHandler(this) ;
        delete= findViewById(R.id.delete) ;
        title = findViewById(R.id.Title );
        details= findViewById(R.id.Details) ;
        time = findViewById(R.id.Time) ;
        done= findViewById(R.id.isdone) ;
        edit = findViewById(R.id.edit);

        delete.setOnClickListener(this) ;
        edit.setOnClickListener(this);
        done.setOnClickListener(this);

        fillActivity();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.delete:
                dataBaseHandler.deleteTask(getIntent().getExtras().getString("itemId"));
                Toast.makeText(getApplicationContext(), "Task deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;

            case R.id.edit :
                Intent toUpdate = new Intent (getApplicationContext() , addTask.class) ;
                toUpdate.putExtra("itemId",String.valueOf(activityTask.getId()));
                toUpdate.putExtra("behaviour","edit");
                startActivity(toUpdate);
                break;

            case R.id.isdone :
                if (done.isChecked())
                    activityTask.setDone(1);
                else
                    activityTask.setDone(0);
                dataBaseHandler.updateTask(activityTask);
                break;
            }
        }

    protected void onResume() {
        super.onResume();
        fillActivity();
    }

    void fillActivity (){

        //putting task properties in the activity
        activityTask= dataBaseHandler.getTask(Integer.parseInt(getIntent().getExtras().getString("itemId")));
        title.setText(activityTask.getTitle());
        details.setText(activityTask.getDetails());
        time.setText(activityTask.getTaskTime());
        if(activityTask.isDone()==1)
            done.setChecked(true);
    }
}
