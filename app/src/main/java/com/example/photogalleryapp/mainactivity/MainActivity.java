package com.example.photogalleryapp.mainactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.photogalleryapp.R;
import com.example.photogalleryapp.SearchActivity;

import static com.example.photogalleryapp.mainactivity.MainActivityPresenter.REQUEST_IMAGE_CAPTURE;

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

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoPresenter.onClick(v);
            }
        };
        btnLeft.setOnClickListener(listener);
        btnRight.setOnClickListener(listener);
        btnSearch.setOnClickListener(filterListener);
        submitComment.setOnClickListener(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhotoPresenter = new MainActivityPresenter(this);
        mPhotoPresenter.initPresenter();
    }

    private View.OnClickListener filterListener = new View.OnClickListener() {
        public void onClick(View v) {
            mPhotoPresenter.filteredOnClick(v);
        }
    };

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
            // what is resultFlag?
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


    public void snapPhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPhotoPresenter.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void startActivityForResult(int requestCode) {
        Intent i = new Intent(MainActivity.this, SearchActivity.class);
        startActivityForResult(i, requestCode);
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
