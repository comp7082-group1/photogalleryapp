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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                String fileDateStr = f.getAbsolutePath().substring(filePathLength-32, filePathLength-24);
                System.out.println(f.getAbsolutePath());
                System.out.println(f.getAbsolutePath().substring(filePathLength - 33));
                System.out.println(f.getAbsolutePath().substring(filePathLength-32, filePathLength-24));
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


                // TODO: 5/8/2019 Compare fileDate with minDate and maxDate to see if it is within bounds 
                if(extension.equals(".jpg")) {
                    if (fileDate.compareTo(minDate) >= 0 && fileDate.compareTo(maxDate) <= 0 ) {
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

        if(!photoGallery.isEmpty()) {

            iv.setVisibility(View.VISIBLE);
            dateView.setVisibility(View.VISIBLE);
            noResult.setVisibility(View.INVISIBLE);
            btnLeft.setVisibility(View.VISIBLE);
            btnRight.setVisibility(View.VISIBLE);
            comment.setVisibility(View.VISIBLE);
            caption.setVisibility(View.VISIBLE);

            System.out.println("displaying first image " + photoGallery.get(0));
            currentPhotoPath = photoGallery.get(0);
            displayPhoto(currentPhotoPath);
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
                submitComment(v);
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

        if (currentPhotoPath != null) {

            ImageView iv = findViewById(R.id.main_imageView);
            iv.setImageBitmap(BitmapFactory.decodeFile(path));

            try {
                ExifInterface exifInterface = new ExifInterface(path);
                String comment;
                comment = exifInterface.getAttribute(ExifInterface.TAG_USER_COMMENT);
                TextView commentView = findViewById(R.id.main_CaptionEditText);
                commentView.setText(comment);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int pathLength = path.length();
            DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            DateFormat formatPrint = new SimpleDateFormat("yyyy/MM/dd");
            Date fileDate = null;
            String fileDateStr = path.substring(pathLength - 32, pathLength - 24);
            try {
                fileDate = formatter.parse(fileDateStr);
                System.out.println(formatPrint.format(fileDate));
                TextView dateView = findViewById(R.id.main_TimeStamp);
                dateView.setText(formatPrint.format(fileDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }




    public void submitComment(View v) {
        if (currentPhotoPath != null) {
            TextView commentView = findViewById(R.id.main_CaptionEditText);
            String comment;
            comment = commentView.getText().toString();
            System.out.println(comment);
            ExifInterface exif = null;
            try {
                System.out.println(currentPhotoPath);
                exif = new ExifInterface(currentPhotoPath);
                exif.setAttribute(ExifInterface.TAG_USER_COMMENT, comment);
                System.out.println(exif.getAttribute(ExifInterface.TAG_USER_COMMENT));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                exif.saveAttributes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            displayPhoto(currentPhotoPath);
        }

    }

    public void snapPhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File dir = MyApplication.getAppContext().getFilesDir();
        File image = File.createTempFile(imageFileName, ".jpg", dir );
        currentPhotoPath = image.getAbsolutePath();
        Log.d("createImageFile", currentPhotoPath);
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView imageView = findViewById(R.id.main_imageView);

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

            File image = null;
            try {
                image = createImageFile();
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
