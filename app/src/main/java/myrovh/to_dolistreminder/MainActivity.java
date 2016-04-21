package myrovh.to_dolistreminder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private RecyclerView todoRecyclerView;
    private RecyclerView.Adapter Adapter;
    private RecyclerView.LayoutManager todoLayout;

    private Reminder[] data = {new Reminder("Test", "First Test Entry", new Date()),
            new Reminder("Second", "Second Test Entry", new Date())};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup RecyclerView
        todoRecyclerView = (RecyclerView) findViewById(R.id.todoRecyclerView);
        todoLayout = new LinearLayoutManager(this);
        todoRecyclerView.setLayoutManager(todoLayout);
        Adapter = new TodoAdapter(data);
        todoRecyclerView.setAdapter(Adapter);
    }
}

