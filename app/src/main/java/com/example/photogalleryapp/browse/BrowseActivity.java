package com.example.photogalleryapp.browse;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.data.Photo;
import com.example.data.PhotoDataSource;
import com.example.photogalleryapp.R;

import java.util.ArrayList;
import java.util.List;

public class BrowseActivity extends AppCompatActivity implements BrowseActivityContract.View {

    BrowseActivityPresenter mPresenter;
    List<Photo> mPhotos = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        mPhotos = new ArrayList<>();
        mPresenter = new BrowseActivityPresenter(this);
        mPresenter.getPhotos(getPhotoCallback);
    }

    PhotoDataSource.GetPhotoCallback getPhotoCallback = new PhotoDataSource.GetPhotoCallback() {
        @Override
        public void onPhotosLoaded(List<Photo> photos) {
            mPhotos = photos;
            ListView listView = findViewById(R.id.browse_ListView);
            BrowseActivityPresenter.CustomAdapter adapter = mPresenter.new CustomAdapter();
            listView.setAdapter(adapter);
        }
    };

    @Override
    public View getListItemView(Photo photo) {
        View gridItem = getLayoutInflater().inflate(R.layout.griditem, null);

        ImageView imageView = gridItem.findViewById(R.id.imageView);
        TextView textViewComment = gridItem.findViewById(R.id.textView_comment);
        TextView textViewDate = gridItem.findViewById(R.id.textView_dateTime);
        TextView textViewLocation = gridItem.findViewById(R.id.textView_location);

        imageView.setImageBitmap(BitmapFactory.decodeFile(photo.getPath()));
        textViewComment.setText(photo.getComment());
        textViewDate.setText(photo.getDateTime());
        textViewLocation.setText(photo.getCoordinates());

        return gridItem;
    }
}
