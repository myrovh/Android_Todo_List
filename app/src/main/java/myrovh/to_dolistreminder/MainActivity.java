package myrovh.to_dolistreminder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Reminder> todoData = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup Toolbar
        Toolbar appToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(appToolbar);

        //Set test data
        todoData.add(new Reminder("Test", "First Test Entry", Calendar.getInstance()));
        todoData.add(new Reminder("Test2", "Second Test Entry", Calendar.getInstance()));
        todoData.add(new Reminder("Test3", "Third Test Entry", Calendar.getInstance()));
        todoData.add(new Reminder("Test4", "Forth Test Entry", Calendar.getInstance()));
        todoData.add(new Reminder("Test5", "Fifth Test Entry", Calendar.getInstance()));
        todoData.add(new Reminder("Test6", "Sixth Test Entry", Calendar.getInstance()));
        todoData.add(new Reminder("Test7", "Seventh Test Entry", Calendar.getInstance()));
        todoData.add(new Reminder("Test7", "Seventh Test Entry", Calendar.getInstance()));
        todoData.add(new Reminder("Test7", "Seventh Test Entry", Calendar.getInstance()));
        todoData.add(new Reminder("Test7", "Seventh Test Entry", Calendar.getInstance()));
        todoData.add(new Reminder("Test7", "Seventh Test Entry", Calendar.getInstance()));
        todoData.add(new Reminder("Test7", "Seventh Test Entry", Calendar.getInstance()));

        //Setup RecyclerView
        RecyclerView todoRecyclerView = (RecyclerView) findViewById(R.id.todoRecyclerView);
        RecyclerView.LayoutManager todoLayout = new LinearLayoutManager(this);
        todoRecyclerView.setLayoutManager(todoLayout);
        RecyclerView.Adapter adapter = new TodoAdapter(todoData);
        todoRecyclerView.setAdapter(adapter);
    }
}

