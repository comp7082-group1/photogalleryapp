package com.example.photogalleryapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import static com.google.common.base.Preconditions.checkNotNull;

public class GalleryFragment extends Fragment implements GalleryContract.View, LocationListener, View.OnClickListener {


    protected LocationManager locationManager;

    private GalleryContract.Presenter mPresenter;

    private TextView mCaption;

//    private


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }


    private View.OnClickListener filterListener = new View.OnClickListener() {
        public void onClick(View v) {
            mPresenter.onClick(v);
        }
    };

    @Override
    public void setPresenter(@NonNull GalleryContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
////        View root = inflater.inflate(R.layout.addtask_frag, container, false);
////        mTitle = (TextView) root.findViewById(R.id.add_task_title);
////        return root;
//        return null;
//    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        rootView = (ImageView) getView().findViewById(R.id.foo);
    }

//    @Override void onActivityResult(int requestCode, int resultCode, Intent data){
//        mPresenter.onActivityResult();
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        FloatingActionButton fab =
//                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task_done);
//        fab.setImageResource(R.drawable.ic_done);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPresenter.saveTask(mTitle.getText().toString(), mDescription.getText().toString());
//            }
//        });


        TextView dateView = getView().findViewById(R.id.main_TimeStamp);
        dateView.setText("test");

        Button btnLeft = getView().findViewById(R.id.main_LeftButton);
        Button btnRight = getView().findViewById(R.id.main_RightButton);
        Button btnSearch = getView().findViewById(R.id.main_searchButton);
        Button submitComment = getView().findViewById(R.id.main_CommentButton);

        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnSearch.setOnClickListener(filterListener);
        submitComment.setOnClickListener(this);


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // no location permissions
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    5);
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    6);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

        mPresenter.initializeGallery();
    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_RightButton:
                Log.d("Right button pressed", "Right.");
                mPresenter.nextPhoto(v);
                break;
            case R.id.main_LeftButton:
                mPresenter.previousPhoto(v);
                break;
            case R.id.main_CommentButton:
//                submitComment(v);
                mPresenter.submitLocation();
                mPresenter.submitComment();
                mPresenter.submitTimeStamp();
                break;
        }
        mPresenter.refreshPhoto();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       mPresenter.onActivityResult(getActivity(), requestCode, resultCode, data);
    }



    @Override
    public void onLocationChanged(Location location) {
        mPresenter.onLocationChanged(location);
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
