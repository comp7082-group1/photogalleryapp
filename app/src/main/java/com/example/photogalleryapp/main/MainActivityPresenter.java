package com.example.photogalleryapp.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.photogalleryapp.R;
import com.example.data.Photo;
import com.example.data.PhotoDataSource;
import com.example.data.PhotoGalleryDataSource;
import com.example.data.PhotoGalleryDatabase;
import com.example.util.AppExecutors;
import com.example.util.ImageHelper;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MainActivityPresenter implements PhotoDataSource.GetPhotoCallback, MainActivityContract.Presenter, LocationListener {

    private PhotoGalleryDataSource mDataSource;
    private final MainActivityContract.View mMainActivityView;
    protected List<Photo> mPhotos = null;
    private Integer currentPhotoIndex = 0;
    private Location mCurrentLocation = null;

    private static final int SEARCH_ACTIVITY_REQUEST_CODE = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int BROWSE_ACTIVITY_REQUEST_CODE = 2;

    private Boolean resultFlag = Boolean.TRUE;

    public MainActivityPresenter(AppCompatActivity activity){
        mMainActivityView = (MainActivityContract.View)activity;
        mDataSource = PhotoGalleryDataSource.getInstance(
                        new AppExecutors(),
                        PhotoGalleryDatabase.getInstance(activity.getBaseContext()).photoDao());
    }

    @Override
    public void onPhotosLoaded(List<Photo> photos) {
        mPhotos = photos;
    }


    public void nextPhoto() {
        if (currentPhotoIndex < mPhotos.size() - 1) {
            currentPhotoIndex++;
        } else {
            currentPhotoIndex = 0;
        }
    }

    public void previousPhoto() {
        if (currentPhotoIndex > 0) {
            currentPhotoIndex--;
        } else {
            currentPhotoIndex = mPhotos.size() - 1;
        }
    }

    @Override
    public void initPresenter() {
        mPhotos = new ArrayList<>();
        mMainActivityView.initView();
        Log.d("Before Loading Gallery", "Loading from");

        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date minDate = new Date(Long.MIN_VALUE);
        Date maxDate = new Date(Long.MAX_VALUE);
        loadGallery(minDate, maxDate, "", new PhotoDataSource.GetPhotoCallback() {
            @Override
            public void onPhotosLoaded(List<Photo> photos) {
                mPhotos = photos;
                refreshVisibility();
                if (!mPhotos.isEmpty()) {
                    currentPhotoIndex = mPhotos.size() - 1;
                    displayPhoto(mPhotos.get(currentPhotoIndex));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_RightButton:
                Log.d("Right button pressed", "Right.");
                nextPhoto();
                break;
            case R.id.main_LeftButton:
                previousPhoto();
                break;
            case R.id.main_CommentButton:
                savePhoto(mPhotos.get(currentPhotoIndex));
                break;
        }
        Log.d("Current Index: ", "" + currentPhotoIndex);

        displayPhoto(mPhotos.get(currentPhotoIndex));
    }

    @Override
    public void searchOnClick(View view) {
        mMainActivityView.searchOnClick(SEARCH_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void browseOnClick(View v) {
        mMainActivityView.browseOnClick(BROWSE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void snapOnClick(View v) {
        mMainActivityView.snapOnClick(REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void setCurrentLocation(Location location) {
        mCurrentLocation = location;
    }

    private void savePhoto(Photo photo){
        if (photo.getPath() != null) {
            // path
            photo.setPath(photo.getPath());
            // comment
            String comment = mMainActivityView.getCommentText();
            photo.setComment(comment);
            // date
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            photo.setDateTime(dateTimeFormat.format(new Date()));
            // location
            if( mCurrentLocation != null) {
                String latitude = dec2DMS(mCurrentLocation.getLatitude());
                String longitude = dec2DMS(mCurrentLocation.getLongitude());
                photo.setCoordinates("Lat: " + latitude + " Long: " + longitude);
            }
            // save
            mDataSource.savePhoto(photo);
        }
    }

    // convert coordinates to exif-compatible format
    private String dec2DMS(double coord) {
        coord = coord > 0 ? coord : -coord;  // -105.9876543 -> 105.9876543
        String sOut = Integer.toString((int) coord) + "/1,";   // 105/1,
        coord = (coord % 1) * 60;         // .987654321 * 60 = 59.259258
        sOut = sOut + Integer.toString((int) coord) + "/1,";   // 105/1,59/1,
        coord = (coord % 1) * 60000;             // .259258 * 60000 = 15555
        sOut = sOut + Integer.toString((int) coord) + "/1000";   // 105/1,59/1,15555/1000
        return sOut;
    }

    public void displayPhoto(Photo photo) {
        if (photo.getPath() != null) {
            // display photo
            mMainActivityView.setImageBitmap(BitmapFactory.decodeFile(photo.getPath()));
            // display comment
            mMainActivityView.setCommentText(photo.getComment());
            // display lat/long
            mMainActivityView.setLocation(photo.getCoordinates());
            // display timestamp
            mMainActivityView.setTimeStamp(photo.getDateTime());
        }
    }

    // initialize a list of file paths named photoGallery
    public void loadGallery(Date minDate, Date maxDate, String keyword, PhotoDataSource.GetPhotoCallback callback) {
        mDataSource.getPhotos(/*minDate, maxDate, keyword,*/callback);
    }

    public void refreshVisibility(){
        resultFlag = !mPhotos.isEmpty();
        mMainActivityView.refreshVisibility(mPhotos.isEmpty());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

                loadGallery(minDate, maxDate, keyword, new PhotoDataSource.GetPhotoCallback() {
                    @Override
                    public void onPhotosLoaded(List<Photo> photos) {
                        mPhotos = photos;
                        refreshVisibility();
                        currentPhotoIndex = 0;
                        if (resultFlag) {
                            displayPhoto(mPhotos.get(currentPhotoIndex));
                        }
                    }
                });
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File image = null;
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                // display image
                //                imageView.setImageBitmap(imageBitmap);
                try {
                    // save image to disk
                    image = ImageHelper.createImageFile(imageBitmap);

                    // add it to the list of photos
                    Photo photo = new Photo();
                    photo.setPath(image.getAbsolutePath());
                    photo.setDateTime(new Date(System.currentTimeMillis()).toString());
                    mPhotos.add(photo);
                    currentPhotoIndex = mPhotos.indexOf(photo);

//                    Toast.makeText(mMainActivityView.getActivity(), image.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    // set date text
                    mMainActivityView.setTimeStamp(photo.getDateTime());
                } catch (IOException e) {
                    Log.d("Failed Creating Photo", "Unable to create a temporary photo.");
                    Log.d("Stack Trace", e.getStackTrace().toString());
                }

                refreshVisibility();
                displayPhoto(mPhotos.get(currentPhotoIndex));
            }

            mMainActivityView.showLongText(image.getAbsolutePath());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        setCurrentLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}
