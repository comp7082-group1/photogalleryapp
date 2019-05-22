package com.example.photogalleryapp.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photogalleryapp.R;
import com.example.photogalleryapp.search.SearchActivity;
import com.example.photogalleryapp.browse.BrowseActivity;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

    MainActivityPresenter mPhotoPresenter;

    @Override
    public void initView() {
        TextView dateView = findViewById(R.id.main_TimeStamp);
        dateView.setText("test");

        Button btnLeft = findViewById(R.id.main_LeftButton);
        Button btnRight = findViewById(R.id.main_RightButton);
        Button btnSearch = findViewById(R.id.main_searchButton);
        Button submitComment = findViewById(R.id.main_CommentButton);
        Button btnBrowse = findViewById(R.id.main_BrowseButton);
        Button btnSnap = findViewById(R.id.main_SnapButton);

        btnLeft.setOnClickListener(onClickListener);
        btnRight.setOnClickListener(onClickListener);
        submitComment.setOnClickListener(onClickListener);
        btnSearch.setOnClickListener(searchClickListener);
        btnBrowse.setOnClickListener(browseClickListener);
        btnSnap.setOnClickListener(snapClickListener);
    }

    View.OnClickListener snapClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPhotoPresenter.snapOnClick(v);
        }
    };

    View.OnClickListener browseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPhotoPresenter.browseOnClick(v);
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPhotoPresenter.onClick(v);
        }
    };

    View.OnClickListener searchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPhotoPresenter.searchOnClick(v);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhotoPresenter = new MainActivityPresenter(this);
        mPhotoPresenter.initPresenter();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // no location permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    5);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    6);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mPhotoPresenter);
        }
    }

    @Override
    public void refreshVisibility(boolean isGalleryEmpty){
        ImageView iv = findViewById(R.id.main_imageView);
        TextView noResult = findViewById(R.id.main_noResults);
        TextView dateView = findViewById(R.id.main_TimeStamp);
        Button btnLeft = findViewById(R.id.main_LeftButton);
        Button btnRight = findViewById(R.id.main_RightButton);
        Button comment = findViewById(R.id.main_CommentButton);
        EditText caption = findViewById(R.id.main_CaptionEditText);

        if (isGalleryEmpty) {
            iv.setVisibility(View.INVISIBLE);
            dateView.setVisibility(View.INVISIBLE);
            noResult.setVisibility(View.VISIBLE);
            btnLeft.setVisibility(View.INVISIBLE);
            btnRight.setVisibility(View.INVISIBLE);
            comment.setVisibility(View.INVISIBLE);
            caption.setVisibility(View.INVISIBLE);
        } else {
            iv.setVisibility(View.VISIBLE);
            dateView.setVisibility(View.VISIBLE);
            dateView.setVisibility(View.VISIBLE);
            noResult.setVisibility(View.INVISIBLE);
            btnLeft.setVisibility(View.VISIBLE);
            btnRight.setVisibility(View.VISIBLE);
            comment.setVisibility(View.VISIBLE);
            caption.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPhotoPresenter.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public String getCommentText() {
        TextView commentView = findViewById (R.id.main_CaptionEditText);
        String comment;
        comment = commentView.getText().toString();
        System.out.println(comment);
        return comment;
    }

    @Override
    public void setCommentText(String comment) {
        TextView commentView = findViewById(R.id.main_CaptionEditText);
        commentView.setText(comment);
    }

    @Override
    public void setImageBitmap(Bitmap decodeFile) {
        ImageView iv = findViewById(R.id.main_imageView);
        iv.setImageBitmap(decodeFile);
    }

    @Override
    public void setLocation(String coordinates) {
        TextView locationView = findViewById(R.id.main_locationText);
        locationView.setText(coordinates);
    }

    @Override
    public void setTimeStamp(String timestamp) {
        TextView timeStampView = findViewById(R.id.main_TimeStamp);
        timeStampView.setText(timestamp);
    }

    @Override
    public void showLongText(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void searchOnClick(int requestCode) {
        Intent i = new Intent(this, SearchActivity.class);
        startActivityForResult(i, requestCode);
    }

    @Override
    public void browseOnClick(int browseActivityRequestCode) {
        Intent i = new Intent(this, BrowseActivity.class);
        startActivityForResult(i, browseActivityRequestCode);
    }

    @Override
    public void snapOnClick(int requestImageCapture) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, requestImageCapture);
        }
    }
}
