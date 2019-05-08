package com.example.photogalleryapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements LocationListener, View.OnClickListener {

    private String locationText;
    protected LocationManager locationManager;
    static final int SEARCH_ACTIVITY_REQUEST_CODE = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    protected ArrayList<String> photoGallery = null;
    protected Integer currentPhotoIndex = 0;
    protected String currentPhotoPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLeft = findViewById(R.id.main_LeftButton);
        Button btnRight = findViewById(R.id.main_RightButton);
        Button btnSearch = findViewById(R.id.main_searchButton);

        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnSearch.setOnClickListener(filterListener);

        Log.d("Before Loading Gallery", "Loading from");
        loadGallery();
    }

    private View.OnClickListener filterListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivityForResult(i, SEARCH_ACTIVITY_REQUEST_CODE);
        }
    };

    public void loadGallery() {
        File dir = MyApplication.getAppContext().getFilesDir();

        Log.d("Loading Gallery", "Loading from: " + dir.getPath());

        photoGallery = new ArrayList<>();
        File[] fList = dir.listFiles();
        if (fList != null) {
            for (File f : dir.listFiles()) {
                String extension = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("."));
                Log.d("File Extension", extension);
                if(extension.equals(".jpg")) {
                    photoGallery.add(f.getPath());
                }
            }
        }
        if(!photoGallery.isEmpty()) {
            displayPhoto(photoGallery.get(0));
        }

    }

    public void launchSearch(View v) {
        Intent i = new Intent(this, SearchActivity.class);
        startActivity(i);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_RightButton:
                Log.d("Right button pressed", "Right.");
                nextPhoto(v);
                break;
            case R.id.main_LeftButton:
                previousPhoto(v);
                break;
        }
        Log.d("Current Index: ", "" + currentPhotoIndex);

        currentPhotoPath = photoGallery.get(currentPhotoIndex);
        displayPhoto(currentPhotoPath);
    }

    public void nextPhoto(View v) {
        if (currentPhotoIndex < photoGallery.size() - 1) {
            currentPhotoIndex++;
        } else {
            currentPhotoIndex = 0;
        }
    }

    public void previousPhoto(View v) {
        if (currentPhotoIndex > 0) {
            currentPhotoIndex--;
        } else {
            currentPhotoIndex = photoGallery.size() - 1;
        }
    }

    public void displayPhoto(String path) {
        ImageView iv = findViewById(R.id.main_imageView);
        iv.setImageBitmap(BitmapFactory.decodeFile(path));
    }

    public void snapPhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", dir );
        currentPhotoPath = image.getAbsolutePath();
        Log.d("createImageFile", currentPhotoPath);
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView imageView = findViewById(R.id.main_imageView);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File dir = MyApplication.getAppContext().getFilesDir();

            File image = null;
            try {
                image = File.createTempFile(imageFileName, ".jpg", dir );
            } catch (IOException e) {
                Log.d("Failed Creating Photo", "Unable to create a temporary photo.");
                Log.d("Stack Trace", e.getStackTrace().toString());
            }

            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            try (FileOutputStream out = new FileOutputStream(image)) {
                Log.d("Writing to bit map", "Creating image.");
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Created Image", "Successfully created an image.");
            Log.d("Image Path", image.getAbsolutePath());

            Toast.makeText(this, image.getAbsolutePath(), Toast.LENGTH_LONG).show();

//            // display image
//            if (data != null && data.getExtras() != null) {
//                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
//                imageView.setImageBitmap(imageBitmap);
//            }
//            // display time
//            TextView txtTimeStamp = findViewById(R.id.main_TimeStamp);
//            txtTimeStamp.setText(new Date().toString());
//            // display location
//            TextView txtLocation = findViewById(R.id.main_LocationText);
//            txtLocation.setText(locationText);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        locationText = "Latitude:" + location.getLatitude() + ",\r\n Longitude:" + location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
