package myrovh.to_dolistreminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class EditTodoActivity extends AppCompatActivity {
    private Reminder editTodo;
    private int todoPositon;
    private TextInputLayout titleText;
    private TextInputLayout descriptionText;
    private Switch doneSwitch;
    private TextView dueDateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        //Setup Toolbar
        Toolbar appToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(appToolbar);

        //Setup Views
        titleText = (TextInputLayout) findViewById(R.id.titleView);
        descriptionText = (TextInputLayout) findViewById(R.id.descriptionView);
        doneSwitch = (Switch) findViewById(R.id.doneSwitch);
        dueDateView = (TextView) findViewById(R.id.dateView);

        //Get intent data
        editTodo = (Reminder) Parcels.unwrap(getIntent().getParcelableExtra("todo"));
        todoPositon = (int) getIntent().getIntExtra("position", -1);

        //Set existing values
        UpdateView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_apply_todo:
                Intent returnData = new Intent();
                UpdateData();
                returnData.putExtra("todo", Parcels.wrap(editTodo));
                returnData.putExtra("position", todoPositon);
                setResult(1, returnData);
                this.finish();
                return true;
            default:
                // The user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void SetDueDate(View v) {

    }

    //Takes the values stored inside the views and stores them inside the Reminder variable
    public void UpdateData() {
        editTodo.setTitle(titleText.getEditText().getText().toString());
        editTodo.setDescription(descriptionText.getEditText().getText().toString());
        editTodo.setComplete(doneSwitch.isChecked());
        //Date is not updated here but in the SetDueDate function
    }

    //Takes the values stored inside the Reminder variable and displays them in the views
    public void UpdateView() {
        titleText.getEditText().setText(editTodo.getTitle());
        descriptionText.getEditText().setText(editTodo.getDescription());
        if (editTodo.isComplete) {
            doneSwitch.setChecked(true);
        }
        GregorianCalendar cal = (GregorianCalendar) editTodo.getDueDate();
        String calString = cal.get(Calendar.DAY_OF_MONTH) + " " + cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
        dueDateView.setText(calString);
    }
}
