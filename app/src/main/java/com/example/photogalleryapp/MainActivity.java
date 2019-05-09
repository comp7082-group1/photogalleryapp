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
import android.media.ExifInterface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.DateFormat;
import java.text.ParseException;

import java.io.InputStream;
import java.net.URI;

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
    protected Boolean resultFlag = Boolean.TRUE;
    Location currentLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView dateView = findViewById(R.id.main_TimeStamp);
        dateView.setText("test");
        
        Button btnLeft = findViewById(R.id.main_LeftButton);
        Button btnRight = findViewById(R.id.main_RightButton);
        Button btnSearch = findViewById(R.id.main_searchButton);
        Button submitComment = findViewById(R.id.main_CommentButton);

        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnSearch.setOnClickListener(filterListener);
        submitComment.setOnClickListener(this);

        Log.d("Before Loading Gallery", "Loading from");

        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date minDate = new Date(Long.MIN_VALUE);
        Date maxDate = new Date(Long.MAX_VALUE);
        loadGallery(minDate, maxDate, "");
        if (!photoGallery.isEmpty()) {
            currentPhotoIndex = photoGallery.size() - 1;
            displayPhoto(photoGallery.get(currentPhotoIndex));
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // no location permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    5);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    6);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }


    }

    private View.OnClickListener filterListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivityForResult(i, SEARCH_ACTIVITY_REQUEST_CODE);
        }
    };

    public void loadGallery(Date minDate, Date maxDate, String keyword) {
        File dir = MyApplication.getAppContext().getFilesDir();

        Log.d("Loading Gallery", "Loading from: " + dir.getPath());

        photoGallery = new ArrayList<>();
        File[] fList = dir.listFiles();
        if (fList != null) {
            for (File f : dir.listFiles()) {
                String extension = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("."));
                Log.d("File Extension", extension);

                int filePathLength = f.getAbsolutePath().length();

                DateFormat format = new SimpleDateFormat("yyyyMMdd");
                Date fileDate = null;
                String fileDateStr = f.getAbsolutePath().substring(filePathLength - 32, filePathLength - 24);
                System.out.println(f.getAbsolutePath());
                System.out.println(f.getAbsolutePath().substring(filePathLength - 33));
                System.out.println(f.getAbsolutePath().substring(filePathLength - 32, filePathLength - 24));
                try {
                    fileDate = format.parse(fileDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ExifInterface exifInterface = null;
                try {
                    exifInterface = new ExifInterface(f.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String comment;
                comment = exifInterface.getAttribute(ExifInterface.TAG_USER_COMMENT);


                if (fileDate == null) {
                    fileDate = new Date(System.currentTimeMillis());
                }
                // TODO: 5/8/2019 Compare fileDate with minDate and maxDate to see if it is within bounds 
                if (extension.equals(".jpg")) {
                    if (fileDate.compareTo(minDate) >= 0 && fileDate.compareTo(maxDate) <= 0) {
                        System.out.println("adding " + f.getPath() + " to gallery");
                        photoGallery.add(f.getPath());
                    }
                    if (keyword != null && comment != null) {
                        String commentLower = comment.toLowerCase();
                        String keywordLower = keyword.toLowerCase();
                        if (commentLower.contains(keywordLower)) {
                            photoGallery.add(f.getPath());
                        }
                    }
                }
            }
        }
        ImageView iv = findViewById(R.id.main_imageView);
        TextView noResult = findViewById(R.id.main_noResults);
        TextView dateView = findViewById(R.id.main_TimeStamp);
        Button btnLeft = findViewById(R.id.main_LeftButton);
        Button btnRight = findViewById(R.id.main_RightButton);
        Button comment = findViewById(R.id.main_CommentButton);
        EditText caption = findViewById(R.id.main_CaptionEditText);

        if (!photoGallery.isEmpty()) {

            iv.setVisibility(View.VISIBLE);
            dateView.setVisibility(View.VISIBLE);
            dateView.setVisibility(View.VISIBLE);
            noResult.setVisibility(View.INVISIBLE);
            btnLeft.setVisibility(View.VISIBLE);
            btnRight.setVisibility(View.VISIBLE);
            comment.setVisibility(View.VISIBLE);
            caption.setVisibility(View.VISIBLE);

//            System.out.println("displaying first image " + photoGallery.get(0));
//            currentPhotoPath = photoGallery.get(0);
//            displayPhoto(currentPhotoPath);
        } else {
            resultFlag = Boolean.FALSE;

            iv.setVisibility(View.INVISIBLE);
            dateView.setVisibility(View.INVISIBLE);
            noResult.setVisibility(View.VISIBLE);
            btnLeft.setVisibility(View.INVISIBLE);
            btnRight.setVisibility(View.INVISIBLE);
            comment.setVisibility(View.INVISIBLE);
            caption.setVisibility(View.INVISIBLE);
        }
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
            case R.id.main_CommentButton:
//                submitComment(v);
                submitLocation();
                submitComment();
                submitTimeStamp();
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

        if (path != null) {

            ImageView iv = findViewById(R.id.main_imageView);
            iv.setImageBitmap(BitmapFactory.decodeFile(path));

            try {
                ExifInterface exifInterface = new ExifInterface(path);
                String comment;
                comment = exifInterface.getAttribute(ExifInterface.TAG_USER_COMMENT);
                TextView commentView = findViewById(R.id.main_CaptionEditText);
                commentView.setText(comment);

                String lat = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                String lon = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                TextView locationView = findViewById(R.id.main_locationText);
                locationView.setText("Lat: " + lat + " Long: " + lon);

                String timestamp = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
                TextView timeStampView = findViewById(R.id.main_TimeStamp);
                timeStampView.setText(timestamp);

            } catch (IOException e) {
                e.printStackTrace();
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

    private String getLocationText() {
        return "Lat:" + currentLocation.getLatitude() + ",\r\n Long:" + currentLocation.getLongitude();
    }

    public void submitLocation() {
        if (currentPhotoPath != null && currentLocation != null) {
            saveAttribute(currentPhotoPath, ExifInterface.TAG_GPS_LATITUDE, dec2DMS(currentLocation.getLatitude()));
            saveAttribute(currentPhotoPath, ExifInterface.TAG_GPS_LONGITUDE, dec2DMS(currentLocation.getLongitude()));
        }
    }

    public void submitTimeStamp() {
        if (currentPhotoPath != null) {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            saveAttribute(currentPhotoPath, ExifInterface.TAG_DATETIME, dateTimeFormat.format(new Date()));
        }
    }

    public void submitComment() {
        if (currentPhotoPath != null) {
            TextView commentView = findViewById(R.id.main_CaptionEditText);
            String comment;
            comment = commentView.getText().toString();
            System.out.println(comment);
            saveAttribute(currentPhotoPath, ExifInterface.TAG_USER_COMMENT, comment);
        }
    }

    public void snapPhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public File createImageFile(Bitmap imageBitmap) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File dir = MyApplication.getAppContext().getFilesDir();
        File image = File.createTempFile(imageFileName, ".jpg", dir);
        Log.d("createImageFile", image.getAbsolutePath());
        try (FileOutputStream out = new FileOutputStream(image)) {
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d("createImageFile", data.getStringExtra("STARTDATE"));
                Log.d("createImageFile", data.getStringExtra("ENDDATE"));
                Log.d("createImageFile", data.getStringExtra("KEYWORD"));

                // TODO: 5/8/2019 Check minDate and maxDate is being populated correctly!!!! Completed = Confirmed
                DateFormat format = new SimpleDateFormat("yyyyMMdd");
                Date minDate = null;
                Date maxDate = null;
                String keyword = data.getStringExtra("KEYWORD");

                try {
                    minDate = format.parse(data.getStringExtra("STARTDATE"));
                    maxDate = format.parse(data.getStringExtra("ENDDATE"));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //System.out.println(format.format(minDate));
                //System.out.println(format.format(maxDate));

                loadGallery(minDate, maxDate, keyword);
                currentPhotoIndex = 0;
                if (resultFlag) {
                    currentPhotoPath = photoGallery.get(currentPhotoIndex);
                    displayPhoto(currentPhotoPath);
                }
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageView imageView = findViewById(R.id.main_imageView);
            File image = null;
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                // display image
                //                imageView.setImageBitmap(imageBitmap);
                try {
                    // save image to disk
                    image = createImageFile(imageBitmap);
                    currentPhotoPath = image.getAbsolutePath();
                    Toast.makeText(this, image.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    TextView textView = findViewById(R.id.main_TimeStamp);
                    textView.setText(new Date(System.currentTimeMillis()).toString());
                } catch (IOException e) {
                    Log.d("Failed Creating Photo", "Unable to create a temporary photo.");
                    Log.d("Stack Trace", e.getStackTrace().toString());
                }

                Date minDate = new Date(Long.MIN_VALUE);
                Date maxDate = new Date(Long.MAX_VALUE);
                loadGallery(minDate, maxDate, "");
                displayPhoto(currentPhotoPath);
                currentPhotoIndex = findPhotoIndex(currentPhotoPath);
            }

            Toast.makeText(this, image.getAbsolutePath(), Toast.LENGTH_LONG).show();
        }
    }

    private int findPhotoIndex(String path) {
        for (int i = 0; i < photoGallery.size(); ++i) {
            if (photoGallery.get(i).equals(path)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        locationText = getLocationText();
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
