package myrovh.to_dolistreminder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Reminder> todoData = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set test data
        todoData.add(new Reminder("Test", "First Test Entry", new Date()));
        todoData.add(new Reminder("Test2", "Second Test Entry", new Date()));
        todoData.add(new Reminder("Test3", "Third Test Entry", new Date()));
        todoData.add(new Reminder("Test4", "Forth Test Entry", new Date()));
        todoData.add(new Reminder("Test5", "Fifth Test Entry", new Date()));
        todoData.add(new Reminder("Test6", "Sixth Test Entry", new Date()));
        todoData.add(new Reminder("Test7", "Seventh Test Entry", new Date()));

        //Setup RecyclerView
        RecyclerView todoRecyclerView = (RecyclerView) findViewById(R.id.todoRecyclerView);
        RecyclerView.LayoutManager todoLayout = new LinearLayoutManager(this);
        todoRecyclerView.setLayoutManager(todoLayout);
        RecyclerView.Adapter adapter = new TodoAdapter(todoData);
        todoRecyclerView.setAdapter(adapter);
    }
}

