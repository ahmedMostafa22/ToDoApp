package com.example.mytaskmanager.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mytaskmanager.Models.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataBaseHandler extends SQLiteOpenHelper {


    private Context context ;


    private static final int DB_VERSION = 1 ;
    private static final String DB_NAME = "TODO List Database" ;
    private static final String TABLE_NAME = "Tasks" ;

    // Columns
    private static final String  KEY_ID="id" ;
    private static final String  TASK_TITLE = "taskTitle" ;
    private static final String  TASK_DETAILS = "taskDetails" ;
    public static final String  TIME = "time" ;
    public static final String  DONE = "done" ;


    public DataBaseHandler(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context ;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME +"(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                TASK_TITLE + " TEXT , " + TASK_DETAILS +" TEXT , "+TIME + " TEXT ,"+DONE + " INTEGER ); ";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    // ADD TASK
    public  void addTask(Task task){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //Setting the values to a database row
        contentValues.put(TASK_TITLE , task.getTitle());
        contentValues.put(TASK_DETAILS , task.getDetails());
        contentValues.put(TIME , task.getTaskTime());
        contentValues.put(DONE , 0 );

        // inserting the row
        sqLiteDatabase.insert(TABLE_NAME , null  , contentValues);
    }
        //returns a Single Task
    public Task getTask(int id ){

        Task task = new Task() ;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase() ;


        Cursor cursor = sqLiteDatabase.query(TABLE_NAME ,new String[] {KEY_ID , TASK_TITLE , TASK_DETAILS ,
                        TIME , DONE }, KEY_ID +"= ?", new String[] {String.valueOf(id)} , null , null , null);

        // setting values from data base to the Task object
        cursor.moveToFirst() ;
        task.setId(Integer.parseInt(cursor.getString(0)));
        task.setTitle(cursor.getString(1));
        task.setDetails(cursor.getString(2));
        task.setTaskTime(cursor.getString(3));
        task.setDone(cursor.getInt(4));
        return task ;
    }
    //DELETE TASK

    public void deleteTask (String id ){

        SQLiteDatabase sqLiteDatabase = getWritableDatabase() ;
        sqLiteDatabase.delete(TABLE_NAME ,KEY_ID + "=?",new String[]{id} );

    }

    // UPDATE TASK

    public void updateTask (Task task ){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //Setting the values to a database row
        contentValues.put(TASK_TITLE , task.getTitle());
        contentValues.put(TASK_DETAILS , task.getDetails());
        contentValues.put(TIME , task.getTaskTime());
        contentValues.put(DONE , task.isDone());

        // updating the row
        sqLiteDatabase.update(TABLE_NAME , contentValues,KEY_ID+"=?"  , new String[]{String.valueOf(task.getId())});

    }


    //GET array OF TASKS

    public Task[] getTasks(String listName){
        Cursor cursor = null ;

        SQLiteDatabase sqLiteDatabase = getReadableDatabase() ;

        // the sql read statement

        switch (listName) {
            case "All Tasks":
                cursor = sqLiteDatabase.query(TABLE_NAME, new String[]{KEY_ID, TASK_TITLE, TASK_DETAILS,
                                TIME, DONE}, DONE + "=?", new String[]{"0"}, null,
                        null, TIME + " ASC");
                break;

            case "Done":
                cursor = sqLiteDatabase.query(TABLE_NAME, new String[]{KEY_ID, TASK_TITLE, TASK_DETAILS,
                                TIME, DONE}, DONE + "=?", new String[]{"1"}, null,
                        null, TIME + " ASC");
                break;

            case "Overdue":
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm" , Locale.getDefault());
                String time = simpleDateFormat.format(new Date()) ;

                cursor = sqLiteDatabase.query(TABLE_NAME, new String[]{KEY_ID, TASK_TITLE, TASK_DETAILS,
                                TIME, DONE}, TIME + "<?", new String[]{time}, null,
                        null, TIME + " ASC");
                break;
        }

        Task[] tasksArray = new Task[cursor.getCount()] ;
        int i  = 0 ;

        while (cursor.moveToNext()){
                Task task = new Task();

                //setting the task object variables then adding it to the array

                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setTitle(cursor.getString(1));
                task.setDetails(cursor.getString(2));
                task.setTaskTime(cursor.getString(3));
                task.setDone(cursor.getInt(4));
                tasksArray[i] = task;
                i++;
        }

        return tasksArray;
    }
}
