package myrovh.to_dolistreminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private TextView tapTextView;
    private ArrayList<Reminder> todoData = new ArrayList<>();
    private Map<Marker, Integer> markerList = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Get Views
        tapTextView = (TextView) findViewById(R.id.tap_text);

        //Get intent bundles
        todoData = Parcels.unwrap(getIntent().getParcelableExtra(MainActivity.BUNDLE_REMINDERS));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set Camera position to default location
        LatLng defaultLocation = new LatLng(-34, 151);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));
        tapTextView.setText("Tap a markers title to open the edit screen");

        //Add markers for reminder locations markers are stored in a map so we can attach the reminder id to remember which reminder a marker belongs to
        for (Reminder i : todoData) {
            if (i.getLocation() != null) {
                markerList.put(mMap.addMarker(new MarkerOptions().position(i.getLocation()).title(i.getTitle())), i.getId());
            }
        }

        //Set Listeners
        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Integer selectedReminder = markerList.get(marker);
        Log.d("MAP", "Reminder id equals " + selectedReminder);
        Intent returnIntent = new Intent();
        returnIntent.putExtra(MainActivity.BUNDLE_ID, selectedReminder.intValue());
        setResult(1, returnIntent);
        this.finish();
    }
}
