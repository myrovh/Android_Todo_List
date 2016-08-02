package myrovh.to_dolistreminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SelectLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private TextView tapTextView;
    private boolean wasLocationSet;
    private LatLng setLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Unpack
        wasLocationSet = getIntent().getBooleanExtra(EditReminderActivity.BUNDLE_LOCATIONSET, false);
        if (wasLocationSet) {
            setLocation = new LatLng(getIntent().getDoubleExtra(EditReminderActivity.BUNDLE_LATITUDE, 0), getIntent().getDoubleExtra(EditReminderActivity.BUNDLE_LONGITUDE, 0));
        }

        //Get Views
        tapTextView = (TextView) findViewById(R.id.tap_text);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Move the camera to default location
        LatLng defaultLocation = new LatLng(-37, 144); //Melbourne
        //mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Default starting camera position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));

        //Add marker for old location if it was set
        if (wasLocationSet) {
            mMap.addMarker(new MarkerOptions().position(setLocation).title("Current Reminder Location"));
        }

        // Set Listeners
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        setLocation = latLng;
        Intent returnIntent = new Intent();
        returnIntent.putExtra(EditReminderActivity.BUNDLE_LATITUDE, setLocation.latitude);
        returnIntent.putExtra(EditReminderActivity.BUNDLE_LONGITUDE, setLocation.longitude);
        setResult(1, returnIntent);
        this.finish();
    }
}
