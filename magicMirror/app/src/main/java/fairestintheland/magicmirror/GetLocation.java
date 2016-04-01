/*
NOT USED RIGHT NOW. ALL OF THIS FUNCTIONALITY HADS BEEN MOVED TO MAIN
 */

package fairestintheland.magicmirror;

/**
 * Created by Keith on 3/27/2016.
 */

import android.Manifest;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.api.GoogleApiClient;


import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

public class GetLocation extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    android.location.Location mLastLocation;
    LocationRequest mLocationRequest;
    LocationSettingsRequest.Builder mLocationSettingsRequestBuilder;
    private String latitude;
    private String longitude;
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1001;
    Context mainContext;

    public GetLocation(Context context) {
        mainContext = context;
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mainContext)
                    //.enableAutoManage(this, this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mLocationSettingsRequestBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        //createLocationRequest();

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        mLocationSettingsRequestBuilder.build());
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100000); //every 100 seconds
        //mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    protected void onStart() {
        //mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        /*if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }*/
        super.onStop();
    }

    public void startLocationServices() {
        mGoogleApiClient.connect();
    }

    public void stopLocationServices() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(mainContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mainContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            return;
        }
        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
            latitude = (String.valueOf(mLastLocation.getLatitude()));
            longitude = (String.valueOf(mLastLocation.getLongitude()));
            System.out.println("Latitude: " + latitude);
            System.out.println("Longitude: " + longitude);
        } else {
            //Toast.makeText(this, "Location not allowed", Toast.LENGTH_LONG).show();
            System.out.println("Location is off.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    latitude = (String.valueOf(mLastLocation.getLatitude()));
                    longitude = (String.valueOf(mLastLocation.getLongitude()));
                    System.out.println("Latitude: " + latitude);
                    System.out.println("Longitude: " + longitude);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("LOC", "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("LOC", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

}
