package com.example.mytaskmanager.Adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytaskmanager.Database.DataBaseHandler;
import com.example.mytaskmanager.Models.Task;
import com.example.mytaskmanager.R;

public class TasksListAdapter extends BaseAdapter {
    private Context context ;
    private Task[] TasksArray ;
    private LayoutInflater layoutInflater ;

    public TasksListAdapter(Context context , Task [] TasksArray) {
        this.context = context;
        this.TasksArray = TasksArray ;
    }

    @Override
    public int getCount() {
        return TasksArray.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        //setting inflater and item view layout
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View taskItem = layoutInflater.inflate(R.layout.task_item, parent, false);

        //setting the item layout components
        final TextView Title , id;
        final CheckBox IsDone;

        Title = taskItem.findViewById(R.id.Title);
        IsDone = taskItem.findViewById(R.id.donee);
        id = taskItem.findViewById(R.id.taskId);

        IsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHandler dataBaseHandler = new DataBaseHandler(context) ;
                Task  task = dataBaseHandler.getTask(Integer.parseInt(id.getText().toString())) ;
                if (IsDone.isChecked())
                    task.setDone(1);
                else
                    task.setDone(0);
                dataBaseHandler.updateTask(task);
            }
        });
        //setting values for each item list component
            Title.setText(TasksArray[position].getTitle());
            id.setText(String.valueOf(TasksArray[position].getId()));
            if (TasksArray[position].isDone() == 1)
                IsDone.setChecked(true);
            else
                IsDone.setChecked(false);

        return taskItem;
    }


}
