package myrovh.to_dolistreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    final static int REQUEST_NEW = 20;
    final static int REQUEST_EDIT = 30;
    final static String SETTING_FIRSTSTART = "firstStart";
    private ArrayList<Reminder> todoData = new ArrayList<>();
    private TodoAdapter globalAdapter = new TodoAdapter(todoData);
    private ReminderDatabase database;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup Toolbar
        Toolbar appToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(appToolbar);

        //Get database helper
        database = new ReminderDatabase(getApplicationContext());

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                boolean isFirstStart = getPrefs.getBoolean(SETTING_FIRSTSTART, true);
                if (isFirstStart) {
                    database.addReminder(new Reminder(1, "Test", "First Test Entry", Calendar.getInstance(), false));
                    database.addReminder(new Reminder(2, "Test2", "Second Test Entry", Calendar.getInstance(), false));

                    SharedPreferences.Editor e = getPrefs.edit();
                    e.putBoolean(SETTING_FIRSTSTART, false);
                    e.apply();
                }
            }
        });

        //If this is the first time running the app insert some test data into the database
        t.start();

        //Setup RecyclerView
        RecyclerView todoRecyclerView = (RecyclerView) findViewById(R.id.todoRecyclerView);
        RecyclerView.LayoutManager todoLayout = new LinearLayoutManager(this);
        if (todoRecyclerView != null) {
            todoRecyclerView.setLayoutManager(todoLayout);
            todoRecyclerView.hasFixedSize();
            todoRecyclerView.setAdapter(globalAdapter);
        }

        //Set Recycler View Listener (open edit EditTodoActivity activity on item click)
        globalAdapter.setOnItemClickListener(new TodoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                LaunchEditTodo(position);
            }
        });

        listRefresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_todo:
                //Call item creation intent here
                LaunchAddTodo();
                return true;
            default:
                // The user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    //Calls the edit todo activity but with a '-1' value. edit todo should populate default values in this case
    private void LaunchAddTodo() {
        Intent i = new Intent(MainActivity.this, EditTodoActivity.class);
        i.putExtra("position", -1);
        startActivityForResult(i, REQUEST_NEW);
    }

    private void LaunchEditTodo(int position) {
        Reminder editTodo = todoData.get(position);
        Intent i = new Intent(MainActivity.this, EditTodoActivity.class);
        i.putExtra("todo", Parcels.wrap(editTodo));
        i.putExtra("position", position);
        startActivityForResult(i, REQUEST_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnData) {
        if (resultCode == 1 && requestCode == REQUEST_EDIT) {
            Reminder resultTodo = Parcels.unwrap(returnData.getParcelableExtra("todo"));
            int insertPosition = returnData.getIntExtra("position", -1);
            if (insertPosition != -1) {
                database.editReminder(resultTodo);
                listRefresh();
            }
        } else if (resultCode == 1 && requestCode == REQUEST_NEW) {
            Reminder resultTodo = Parcels.unwrap(returnData.getParcelableExtra("todo"));
            database.addReminder(resultTodo);
            listRefresh();
        }
    }

    //Fetch all reminders from the database then sort
    private void listRefresh() {
        todoData.clear();
        todoData.addAll(database.getAllReminders());
        Collections.sort(todoData, new Comparator<Reminder>() {
            public int compare(Reminder r1, Reminder r2) {
                return r1.getDueDate().compareTo(r2.getDueDate());
            }
        });
        globalAdapter.notifyDataSetChanged();
    }
}

