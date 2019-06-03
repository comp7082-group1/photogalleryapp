package com.example.photogal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.photogal.search.SearchActivity;

public class MainActivity extends AppCompatActivity implements LocationListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SEARCH_ACTIVITY_REQUEST_CODE = 0;

    private PhotoViewModel mPhotoViewModel;
    private int currentIndex = 0;
    private LinearLayoutManager layoutMan;

    private String locationText;
    Location currentLocation = null;
    protected LocationManager locationManager;
    private String currentPhotoPath;

    DateFormat format = new SimpleDateFormat("yyyyMMdd");
    Date filterMinDate = new Date(Long.MIN_VALUE);
    Date filterMaxDate = new Date(Long.MAX_VALUE);
    private String filterKeyword = "";
    private PhotoListAdapter adapter;
    LifecycleOwner mainOwner = this;
    FloatingActionButton fabCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        adapter = new PhotoListAdapter(this);

        recyclerView.setAdapter(adapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        snapHelper.attachToRecyclerView(recyclerView);
        layoutMan = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new ScrollListener(layoutMan) {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentIndex = layoutManager.findFirstCompletelyVisibleItemPosition();
                System.out.println(currentIndex);
                if (currentIndex >= 0) {
                    currentPhotoPath = adapter.getPhotoPath(currentIndex);
                    updateCard(currentPhotoPath);
                }

            }
        });


        // Get a new or existing ViewModel from the ViewModelProvider.
        mPhotoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mPhotoViewModel.getAllPhotos().observe(this, new Observer<List<PhotoEntity>>() {
            @Override
            public void onChanged(@Nullable final List<PhotoEntity> photos) {
                // Update the cached copy of the words in the adapter.
                adapter.setPhotos(photos);

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        FloatingActionButton fabS = findViewById(R.id.fabS);
        fabS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(i, SEARCH_ACTIVITY_REQUEST_CODE);
            }
        });

        fabCancel = findViewById(R.id.fabCancel);
        fabCancel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                mPhotoViewModel.getAllPhotos().observe(mainOwner, new Observer<List<PhotoEntity>>() {
                    @Override
                    public void onChanged(@Nullable final List<PhotoEntity> photos) {
                        // Update the cached copy of the words in the adapter.
                        adapter.setPhotos(photos);

                    }
                });
                fabCancel.setVisibility(View.INVISIBLE);
            }
        });

        Button mButton = findViewById(R.id.commentButton);
        mButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //update comment for image at current index
                EditText comment = findViewById(R.id.comment);
                String scomment = comment.getText().toString();
                saveAttribute(currentPhotoPath, ExifInterface.TAG_USER_COMMENT, scomment);
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // no location permissions
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public File createImageFile(Bitmap imageBitmap) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File dir = this.getFilesDir();
        File image = File.createTempFile(imageFileName, ".jpg", dir);
        Log.d("createImageFile", image.getAbsolutePath());
        try (FileOutputStream out = new FileOutputStream(image)) {
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //ImageView imageView = findViewById(R.id.main_imageView);
            File image = null;
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                // display image
                //                imageView.setImageBitmap(imageBitmap);
                try {
                    // save image to disk
                    image = createImageFile(imageBitmap);
                    //save metadata
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                    String currentPhotoPath = image.getAbsolutePath();
                    if (currentLocation != null) {
                        saveAttribute(currentPhotoPath, ExifInterface.TAG_GPS_LATITUDE, dec2DMS(currentLocation.getLatitude()));
                        saveAttribute(currentPhotoPath, ExifInterface.TAG_GPS_LONGITUDE, dec2DMS(currentLocation.getLongitude()));
                    }
                    saveAttribute(currentPhotoPath, ExifInterface.TAG_DATETIME, timeStamp);
                    saveAttribute(currentPhotoPath, ExifInterface.TAG_USER_COMMENT, "");
                    PhotoEntity photo = new PhotoEntity(currentPhotoPath);
                    mPhotoViewModel.insert(photo);
                    Toast.makeText(this, image.getAbsolutePath(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Log.d("Failed Creating Photo", "Unable to create a temporary photo.");
                    Log.d("Stack Trace", e.getStackTrace().toString());
                }
            }

            Toast.makeText(this, image.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }

        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d("createImageFile", data.getStringExtra("STARTDATE"));
                Log.d("createImageFile", data.getStringExtra("ENDDATE"));
                Log.d("createImageFile", data.getStringExtra("KEYWORD"));

                // TODO: 5/8/2019 Check minDate and maxDate is being populated correctly!!!! Completed = Confirmed
//                format = new SimpleDateFormat("yyyyMMdd");

                filterKeyword = data.getStringExtra("KEYWORD");

                try {
                    filterMinDate = format.parse(data.getStringExtra("STARTDATE"));
                    filterMaxDate = format.parse(data.getStringExtra("ENDDATE"));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                adapter.setFilter(filterMinDate, filterMaxDate, filterKeyword);
                //min and max date + keword are filter
                fabCancel.setVisibility(View.VISIBLE);
            }
        }
    }

    public void saveAttribute(String path, String tag, String value) {
        if (path != null) {
            ExifInterface exif = null;
            try {

                exif = new ExifInterface(path);
                exif.setAttribute(tag, value);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                exif.saveAttributes();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    String dec2DMS(double coord) {
        coord = coord > 0 ? coord : -coord;  // -105.9876543 -> 105.9876543
        String sOut = Integer.toString((int) coord) + "/1,";   // 105/1,
        coord = (coord % 1) * 60;         // .987654321 * 60 = 59.259258
        sOut = sOut + Integer.toString((int) coord) + "/1,";   // 105/1,59/1,
        coord = (coord % 1) * 60000;             // .259258 * 60000 = 15555
        sOut = sOut + Integer.toString((int) coord) + "/1000";   // 105/1,59/1,15555/1000
        return sOut;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        locationText = getLocationText();
    }

    private String getLocationText(){
        return "Lat:"  + currentLocation.getLatitude() + ",\r\n Long:"  + currentLocation.getLongitude();
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

    public void updateCard(String path) {

        EditText mComment= findViewById(R.id.comment);
        TextView mLocation = findViewById(R.id.location);
        TextView mDate = findViewById(R.id.date);

        ExifInterface exif = null;

        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }


        String locationLat;
        String locationLong;
        String location;
        String date;
        String comment = exif.getAttribute(ExifInterface.TAG_USER_COMMENT);
        System.out.println(exif.getAttribute(ExifInterface.TAG_USER_COMMENT));
        locationLat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        locationLong = exif.getAttribute(ExifInterface.TAG_GPS_DEST_LONGITUDE);
        location = locationLat + " " + locationLong;
        date = exif.getAttribute(ExifInterface.TAG_DATETIME);

        mComment.setText(comment);
        mLocation.setText(location);
        mDate.setText(date);


    }

}
