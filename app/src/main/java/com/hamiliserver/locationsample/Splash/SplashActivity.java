package com.hamiliserver.locationsample.Splash;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.hamiliserver.locationsample.Dashboard.DashboardActivity;
import com.hamiliserver.locationsample.R;

public class SplashActivity extends AppCompatActivity {

    String[] requiredPermissions = {

            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();

        showRequestPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, requiredPermissions[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this, requiredPermissions[1]) || ActivityCompat.shouldShowRequestPermissionRationale(this, requiredPermissions[2])) {
                        showExplanation("Permission Needed", "Rationale", requiredPermissions, 1);
                    } else {


                    }
                } else {

                }
        }


        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);

                startActivity(intent);

            } else {
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                finish();

            }
            return;
        }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String[] permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String[] permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                permissionName, permissionRequestCode);
    }

    private void showRequestPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, requiredPermissions[0]) + ContextCompat.checkSelfPermission(this, requiredPermissions[1] + ContextCompat.checkSelfPermission(this, requiredPermissions[2]));
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    requiredPermissions[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this, requiredPermissions[1])) {
                showExplanation("Permission Needed", "Rationale", requiredPermissions, 1);
            } else {
                requestPermission(requiredPermissions, 1);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);

            startActivity(intent);
        }
    }
}