package com.bangkit.tagme.activity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.VIBRATE;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bangkit.tagme.Fav;
import com.bangkit.tagme.MainActivity;
import com.bangkit.tagme.R;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.gson.Gson;
import com.google.zxing.Result;


public class QRscannerActivity extends AppCompatActivity {

    CodeScanner codeScanner;
    CodeScannerView scanView;
    TextView resultData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);
        scanView = findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(this,scanView);
        resultData = findViewById(R.id.resultsOfQr);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultData.setText(result.getText());

                    }
                });

                DataSaveToSharedPref(result.getText()); //hasil text dari qr code disimpan ke shared pref
            }


        });


        //permission & request permission dipanggil di bawah
        if (checkPermission()) {
            // if permission is already granted display a toast message
            Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        scanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });


    }

    private void DataSaveToSharedPref(String resultData) {


        // below line is use to add data to array list.
        MainActivity.courseModalArrayList.add(new Fav(resultData));
        // notifying adapter when new data added.
        MainActivity.adapter.notifyItemInserted(MainActivity.courseModalArrayList.size());


        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(MainActivity.courseModalArrayList);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("courses", json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply();

        Toast.makeText(this, "Data Saved Success", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        requestForCamera();
        codeScanner.startPreview();
    }
    @Override
    protected void onPause() {
        // on app pause the
        // camera will stop scanning.
        codeScanner.releaseResources();

        super.onPause();
    }

    private boolean checkPermission() {

        int camera_permission = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int vibrate_permission = ContextCompat.checkSelfPermission(getApplicationContext(), VIBRATE);
        return camera_permission == PackageManager.PERMISSION_GRANTED && vibrate_permission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // this method is to request
        // the runtime permission.
        int PERMISSION_REQUEST_CODE = 200;
        ActivityCompat.requestPermissions(this, new String[]{CAMERA, VIBRATE}, PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // this method is called when user
        // allows the permission to use camera.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            boolean cameraaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean vibrateaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            if (cameraaccepted && vibrateaccepted) {
                Toast.makeText(this, "Permission granted..", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denined \n You cannot use app without providing permission", Toast.LENGTH_SHORT).show();
            }
        }
    }


}