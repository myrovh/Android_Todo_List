package myrovh.to_dolistreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    final static String REQUEST_INTENT = "intent";
    final static int REQUEST_NEW = 20;
    final static int REQUEST_EDIT = 30;
    final static int REQUEST_DELETE = 40;
    private final static String SETTING_FIRSTSTART = "firstStart";
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
                    database.addReminder(new Reminder(1, "Test", "First Test Entry", Calendar.getInstance(), false, null, null));
                    database.addReminder(new Reminder(2, "Test2", "Second Test Entry", Calendar.getInstance(), false, null, null));

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

        //Set Recycler View Listener (open edit EditReminderActivity activity on item click)
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
            case R.id.view_reminder_locations:
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
                return true;
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
        Intent i = new Intent(MainActivity.this, EditReminderActivity.class);
        i.putExtra("position", -1);
        i.putExtra(REQUEST_INTENT, REQUEST_NEW);
        startActivityForResult(i, REQUEST_NEW);
    }

    private void LaunchEditTodo(int position) {
        Reminder editTodo = todoData.get(position);
        Intent i = new Intent(MainActivity.this, EditReminderActivity.class);
        i.putExtra("todo", Parcels.wrap(editTodo));
        i.putExtra("position", position);
        i.putExtra(REQUEST_INTENT, REQUEST_EDIT);
        startActivityForResult(i, REQUEST_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnData) {
        //ReminderActivity is sending back a return intent value we can use to determine if an edit intent actually requires a delete order
        //TODO switch to using resultCode?
        int returnValue = -1;
        if (resultCode == 1) {
            returnValue = returnData.getIntExtra(EditReminderActivity.RETURN_INTENT, -1);
        }

        if (resultCode == 1 && returnValue == REQUEST_DELETE) {
            Reminder resultTodo = Parcels.unwrap(returnData.getParcelableExtra("todo"));
            Log.d("MAIN", "Reminder delete called on reminder id " + resultTodo.getId());
            database.deleteReminder(resultTodo);
            listRefresh();
        } else if (resultCode == 1 && requestCode == REQUEST_EDIT) {
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

