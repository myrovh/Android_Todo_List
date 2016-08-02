package myrovh.to_dolistreminder;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.parceler.Parcels;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView tapTextView;
    private ArrayList<Reminder> todoData = new ArrayList<>();

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
        tapTextView.setText("Tap a reminder to open the edit screen");

        //Add markers for reminder locations
        for (Reminder i : todoData) {
            if (i.getLocation() != null) {
                mMap.addMarker(new MarkerOptions().position(i.getLocation()).title(i.getTitle()));
            }
        }
    }
}
