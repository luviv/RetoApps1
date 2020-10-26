package com.example.retoappsfinal;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.graphics.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.UUID;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private String user;
    private LocationManager manager;
    private Marker me;
    private ArrayList<Marker> holes;
    private Button addBtn;
    private Button posmeBtn;
    private LocationWorker locationWorker;
    private Position currentPos;
    private HoleWorker holeWorker;
    private UsersWorker usersWorker;
    private ArrayList<Marker> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        addBtn = findViewById(R.id.addBtn);
        posmeBtn = findViewById(R.id.posmeBtn);

        user = getIntent().getExtras().getString("user");
        holes = new ArrayList<>();
        users = new ArrayList<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 2, this);

        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);

        posmeBtn.setOnClickListener(
                (v) -> {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(me.getPosition(), 18));
                }
        );

        addBtn.setOnClickListener(
                (v) -> {
                    Marker m = mMap.addMarker(new MarkerOptions().position(me.getPosition()));
                    HTTPSWebUtilDomi holeMarker = new HTTPSWebUtilDomi();
                    Gson gson = new Gson();
                    new Thread(
                            () -> {
                                holeMarker.PUTrequest("https://retoappsfinal.firebaseio.com/holes/"+ UUID.randomUUID().toString()+"/location.json", gson.toJson(currentPos));
                            }
                    ).start();
                }
        );

        locationWorker = new LocationWorker(this);
        locationWorker.start();

        holeWorker = new HoleWorker(this);
        holeWorker.start();

        usersWorker = new UsersWorker(this);
        usersWorker.start();

    }

    @Override
    public void onDestroy() {
        locationWorker.finish();
        super.onDestroy();
    }

    @SuppressLint("MissingPermission")
    public void setInitialPos() {
        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            updateMyLocation(location);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        updateMyLocation(location);
    }

    public void updateMyLocation(Location location) {
        LatLng myPos = new LatLng(location.getLatitude(), location.getLongitude());
        if(me == null) {
            me = mMap.addMarker(new MarkerOptions().position(myPos).title("Yo"));
            me.setIcon(BitmapDescriptorFactory.defaultMarker(200));
        } else {
            me.setPosition(myPos);
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPos, 18));

        currentPos = new Position(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Toast.makeText(this, "Latitud: " + marker.getPosition().latitude + ", " + "Longitud: " + marker.getPosition().longitude, Toast.LENGTH_LONG).show();
        return true;
    }

    public Position getCurrentPos() {
        return currentPos;
    }

    public String getUser() {
        return user;
    }

    public void updateHoles(ArrayList<Position> positions) {
        runOnUiThread(
                () -> {
                    for (Marker marker: holes) {
                        marker.remove();
                    }
                    holes.clear();
                    for (Position pos: positions) {
                        LatLng latLng = new LatLng(pos.getLat(), pos.getLng());
                        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
                        holes.add(marker);
                    }
                }
        );
    }

    public void updateUsers(ArrayList<Position> positions) {
        runOnUiThread(
                () -> {
                    for (Marker marker: users) {
                        marker.remove();
                    }
                    users.clear();
                    for (Position pos: positions) {
                        LatLng latLng = new LatLng(pos.getLat(), pos.getLng());
                        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(230));
                        users.add(marker);
                    }
                }
        );
    }

}