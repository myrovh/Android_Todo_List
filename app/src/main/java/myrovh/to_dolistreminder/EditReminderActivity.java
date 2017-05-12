package myrovh.to_dolistreminder;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class EditReminderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, OnMapReadyCallback {
    static final String RETURN_INTENT = "returnIntent";
    static final String BUNDLE_LOCATIONSET = "location";
    static final String BUNDLE_LATITUDE = "latitude";
    static final String BUNDLE_LONGITUDE = "longitude";
    static final int PERMISSIONS_REQUEST_LOCATION = 20;
    private Reminder editTodo;
    private int todoPosition;
    private int activityIntent;
    private TextInputLayout titleText;
    private TextInputLayout descriptionText;
    private Switch doneSwitch;
    private TextView dueDateView;
    private TextView locationView;
    private SupportMapFragment mapFragment;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        //Setup Toolbar
        Toolbar appToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(appToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    private void setDefaultLocation() {
        //TODO application crashes if there is no last known location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            String locationProvider = LocationManager.NETWORK_PROVIDER;
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            if (lastKnownLocation != null) {
                editTodo.setLocation(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setDefaultLocation();
                }
            }
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

    public void setDueDate(View v) {
        showDatePickerDialog(v);
    }

    public void setAutomaticLocation(View v) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Need location to set default reminder location", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            setDefaultLocation();
        }
        mapRefresh();
    }

    public void setLocation(View v) {
        //start intent that passes a latlong if already set
        //on a valid return update the latlong variable with returned bundled value and call the update location text function
        Intent i = new Intent(EditReminderActivity.this, SelectLocationActivity.class);
        if (editTodo.getLocation() == null) {
            i.putExtra(BUNDLE_LOCATIONSET, false);
        } else {
            i.putExtra(BUNDLE_LOCATIONSET, true);
            i.putExtra(BUNDLE_LATITUDE, editTodo.getLocation().latitude);
            i.putExtra(BUNDLE_LONGITUDE, editTodo.getLocation().longitude);
        }
        startActivityForResult(i, MainActivity.REQUEST_LOCATION);
    }

    public void clearLocation(View v) {
        editTodo.setLocation(null);
        mapRefresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnData) {
        if (resultCode == 1 && requestCode == MainActivity.REQUEST_LOCATION) {
            LatLng newValue = new LatLng(returnData.getDoubleExtra(BUNDLE_LATITUDE, 0), returnData.getDoubleExtra(BUNDLE_LONGITUDE, 0));
            onLocationSet(newValue);
        }
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
        //Date is not updated here but in the setDueDate function
        //Location is not updated here but in the setLocation function
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
        mapFragment.getMapAsync(this);
    }

    //Create a date picker dialog
    private void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //Apply returned date picker value when it reports onDateSet via listener
    @Override
    public void onDateSet(DatePicker v, int year, int month, int day) {
        UpdateData();
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, month, day);
        editTodo.setDueDate(newDate);
        UpdateView(); //TODO data sitting in text views that has not been applied is being destroyed here
    }

    public void onLocationSet(LatLng location) {
        UpdateData();
        editTodo.setLocation(location);
        UpdateView();
        mapRefresh();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        mapRefresh();
    }

    //Clear the map and then apply new marker for set location will still clear old markers if there are no new locations to update from
    public void mapRefresh() {
        if (map != null) {
            map.clear();
            if (editTodo.getLocation() != null) {
                map.addMarker(new MarkerOptions().position(editTodo.getLocation()));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(editTodo.getLocation(), 15));
            }
        } else {
            Log.d("MAP", "refresh called before map exists");
        }
    }
}

