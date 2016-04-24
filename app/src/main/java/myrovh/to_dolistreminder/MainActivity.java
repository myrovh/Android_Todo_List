package myrovh.to_dolistreminder;

import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
    final static int REQUEST_NEW = 20;
    final static int REQUEST_EDIT = 30;
    private ArrayList<Reminder> todoData = new ArrayList<>();
    private TodoAdapter globalAdapter = new TodoAdapter(todoData);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup Toolbar
        Toolbar appToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(appToolbar);

        //Set test data
        todoData.add(new Reminder("Test", "First Test Entry", Calendar.getInstance(), false));
        todoData.add(new Reminder("Test2", "Second Test Entry", Calendar.getInstance(), false));
        todoData.add(new Reminder("Test3", "Third Test Entry", Calendar.getInstance(), false));
        globalAdapter.notifyDataSetChanged();

        //Setup RecyclerView
        RecyclerView todoRecyclerView = (RecyclerView) findViewById(R.id.todoRecyclerView);
        RecyclerView.LayoutManager todoLayout = new LinearLayoutManager(this);
        todoRecyclerView.setLayoutManager(todoLayout);
        todoRecyclerView.hasFixedSize();
        //RecyclerView.Adapter adapter = new TodoAdapter(todoData);
        todoRecyclerView.setAdapter(globalAdapter);

        //Set Recycler View Listener (open edit todo activity on item click)
        globalAdapter.setOnItemClickListener(new TodoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                LaunchEditTodo(position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_todo:
                //Call item creation intent here
                todoData.add(new Reminder("Test8", "Eighth Test Entry", Calendar.getInstance(), false));
                globalAdapter.notifyItemInserted(todoData.size() - 1);
                //LaunchAddTodo();
                return true;
            default:
                // The user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void LaunchAddTodo() {
        Intent i = new Intent(MainActivity.this, EditTodoActivity.class);
        startActivityForResult(i, REQUEST_NEW);
    }

    public void LaunchEditTodo(int position) {
        Reminder editTodo = todoData.get(position);
        Intent i = new Intent(MainActivity.this, EditTodoActivity.class);
        i.putExtra("todo", Parcels.wrap(editTodo));
        i.putExtra("position", position);
        startActivityForResult(i, REQUEST_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnData) {
        if (resultCode == 1 && requestCode == REQUEST_EDIT) {
            Reminder resultTodo = (Reminder) Parcels.unwrap(returnData.getParcelableExtra("todo"));
            int insertPosition = returnData.getIntExtra("position", -1);
            if (insertPosition != -1) {
                todoData.set(insertPosition, resultTodo);
                globalAdapter.notifyItemChanged(insertPosition);
            }
        }
    }
}

