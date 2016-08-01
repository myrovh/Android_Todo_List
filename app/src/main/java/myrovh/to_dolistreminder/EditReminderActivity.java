package myrovh.to_dolistreminder;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class EditReminderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    static final String RETURN_INTENT = "returnIntent";
    private Reminder editTodo;
    private int todoPosition;
    private int activityIntent;
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
        todoPosition = getIntent().getIntExtra("position", -1);
        activityIntent = getIntent().getIntExtra(MainActivity.REQUEST_INTENT, -1);
        //Set existing values if an existing todo has been parceled
        if (activityIntent == MainActivity.REQUEST_EDIT) {
            editTodo = Parcels.unwrap(getIntent().getParcelableExtra("todo"));
            UpdateView();
        } else {
            editTodo = new Reminder();
            editTodo.setDueDate(Calendar.getInstance());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent returnData = new Intent();
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpTo(this, getIntent());
                return true;
            case R.id.action_delete_reminder:
                Log.d("REMINDER", "delete reminder button pressed");
                if (activityIntent == MainActivity.REQUEST_EDIT) {
                    returnData.putExtra("todo", Parcels.wrap(editTodo));
                    returnData.putExtra("position", todoPosition);
                    returnData.putExtra(RETURN_INTENT, MainActivity.REQUEST_DELETE);
                    setResult(1, returnData);
                } else {
                    setResult(0);
                }
                this.finish();
                return true;
            case R.id.action_apply_reminder:
                if (UpdateData()) {
                    returnData.putExtra("todo", Parcels.wrap(editTodo));
                    returnData.putExtra("position", todoPosition);
                    returnData.putExtra(RETURN_INTENT, MainActivity.REQUEST_NEW);
                    setResult(1, returnData);
                    this.finish();
                }
                return true;
            default:
                // The user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void SetDueDate(View v) {
        showDatePickerDialog(v);
    }

    //Takes the values stored inside the views and stores them inside the Reminder variable
    //Returns false if a title is not set (will not finish updating other variables)
    private boolean UpdateData() {
        if (titleText.getEditText().getText().length() > 0) {
            editTodo.setTitle(titleText.getEditText().getText().toString());
        } else {
            titleText.setError("Enter at least one character");
            return false;
        }
        editTodo.setDescription(descriptionText.getEditText().getText().toString());
        editTodo.setComplete(doneSwitch.isChecked());
        //Date is not updated here but in the SetDueDate function
        return true;
    }

    //Takes the values stored inside the Reminder variable and displays them in the views
    private void UpdateView() {
        titleText.getEditText().setText(editTodo.getTitle());
        descriptionText.getEditText().setText(editTodo.getDescription());
        if (editTodo.isComplete) {
            doneSwitch.setChecked(true);
        }
        GregorianCalendar cal = (GregorianCalendar) editTodo.getDueDate();
        String calString = cal.get(Calendar.DAY_OF_MONTH) + " " + cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
        dueDateView.setText(calString);
    }

    //Create a date picker dialog
    private void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //Apply returned date picker value when it reports onDateSet via listener
    @Override
    public void onDateSet(DatePicker v, int year, int month, int day) {
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, month, day);
        editTodo.setDueDate(newDate);
        UpdateView(); //TODO data sitting in text views that has not been applied is being destroyed here
    }
}

