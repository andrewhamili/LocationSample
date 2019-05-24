package com.hamiliserver.locationsample.Dashboard;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.hamiliserver.locationsample.AsyncTask.Listener.AsyncTaskListener;
import com.hamiliserver.locationsample.AsyncTask.SendLocation;
import com.hamiliserver.locationsample.GeofencingIntentService;
import com.hamiliserver.locationsample.Helpers.GeofenceErrorMessages;
import com.hamiliserver.locationsample.Model.PlacesList;
import com.hamiliserver.locationsample.R;
import com.hamiliserver.locationsample.SharedPreferences.Places;
import com.hamiliserver.locationsample.Splash.SplashActivity;
import com.hamiliserver.locationsample.UploadImageFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status>, AsyncTaskListener {

    private static final String TAG = DashboardActivity.class.getSimpleName();
    protected ArrayList<Geofence> geofenceList;
    Handler hander = new Handler();
    TextView lblUserName;
    Runnable getPlaces;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //lblUserName = findViewById(R.id.lblUserName);

        getPlaces = new Runnable() {
            @Override
            public void run() {
                new com.hamiliserver.locationsample.AsyncTask.Places(DashboardActivity.this).execute();
                hander.postDelayed(getPlaces, 60000);
            }
        };

        getPlaces.run();

        geofenceList = new ArrayList<>();

        buildGoogleApiClient();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void addGeofences() {
        if (googleApiClient.isConnected()) {
            Toast.makeText(this, "Google API Client connected", Toast.LENGTH_SHORT).show();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()).setResultCallback(this);

            new SendLocation(this, location.getLatitude(), location.getLongitude()).execute();

        } else {
            Intent intent = new Intent(this, SplashActivity.class);

            startActivity(intent);

            finish();
        }
    }

    private void removeGeofences() {
        if (googleApiClient.isConnected()) {
            Toast.makeText(this, "Google API Client connected", Toast.LENGTH_SHORT).show();
        }

        LocationServices.GeofencingApi.removeGeofences(
                googleApiClient,
                getGeofencePendingIntent()
        ).setResultCallback(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            UploadImageFragment uploadImageFragment = new UploadImageFragment();

            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_dashboard, uploadImageFragment);

            fragmentTransaction.commit();



        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void populateGeofenceList() {

        geofenceList.clear();

        PlacesList[] placesList = new Gson().fromJson(Places.getPlacesJsonObject(this), PlacesList[].class);

        HashMap<String, LatLng> places = new HashMap<>();

        if (placesList.length > 0) {

            for (int i = 0; i < placesList.length; i++) {
                places.put(placesList[i].name, new LatLng(placesList[i].latitude, placesList[i].longitude));
            }

        }

        for (Map.Entry<String, LatLng> entry : places.entrySet()) {
            geofenceList.add(new Geofence.Builder()
                    .setRequestId(entry.getKey())
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            500)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build()
            );
        }
    }

    protected synchronized void buildGoogleApiClient() {

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {


        Location previousLocation = com.hamiliserver.locationsample.SharedPreferences.Location.getLastLocation(this);

        //Toast.makeText(this, "Distance: " + location.distanceTo(previousLocation), Toast.LENGTH_SHORT).show();

        if (location.distanceTo(previousLocation) > 500 || (location.distanceTo(previousLocation) + 500) == 0) {
            Toast.makeText(this, "Location changed!!", Toast.LENGTH_SHORT).show();

            new SendLocation(this, location.getLatitude(), location.getLongitude()).execute();

            //new com.hamiliserver.locationsample.AsyncTask.Places(this).execute();

        }

        Log.i(TAG, String.valueOf(location.distanceTo(previousLocation)));

        com.hamiliserver.locationsample.SharedPreferences.Location.setPreference(this, location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "GoogleApiClient onConnected");

        locationRequest = LocationRequest.create();

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationRequest.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                Log.i(TAG, "Last Location: " + location.toString());

            }

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection has failed." + connectionResult);
    }

    @Override
    public void onResult(@NonNull Status status) {
        if (status.isSuccess()) {
            Toast.makeText(this, "Geofence Added", Toast.LENGTH_SHORT).show();
        } else {
            String errorMessage = GeofenceErrorMessages.getErrorString(this, status.getStatusCode());

            Log.e(TAG, errorMessage);
        }
    }

    @Override
    public void updateUI(Boolean asyncStatus) {

        if (asyncStatus) {


            Toast.makeText(this, "updateUI()", Toast.LENGTH_SHORT).show();

            populateGeofenceList();

            addGeofences();

        }

    }

    @Override
    public void updateUser(String userName) {

        //lblUserName.setText("Hi. Your name is:\n" + userName);

    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        builder.addGeofences(geofenceList);

        return builder.build();

    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, GeofencingIntentService.class);

        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
