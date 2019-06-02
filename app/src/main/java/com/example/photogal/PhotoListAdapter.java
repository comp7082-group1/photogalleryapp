package com.example.photogal;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.BreakIterator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder> {


    private EditText mComment = null;
    private TextView mLocation = null;
    private TextView mDate = null;
    private Button mButton;
    private ExifInterface exif;
    private String filterKeyword;
    private DateFormat format = new SimpleDateFormat("yyyyMMdd");
    private Date filterMinDate = new Date(Long.MIN_VALUE);
    private Date filterMaxDate = new Date(Long.MAX_VALUE);

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        private final ImageView photoItemView;
        private EditText mComment;
        private Context context;
        public EditText viewComment;

        private PhotoViewHolder(View itemView) {
            super(itemView);
            photoItemView = itemView.findViewById(R.id.imageView);
            this.viewComment = (EditText) itemView.findViewById(R.id.comment);

            this.context = context;

        }


        //extract metadata here
    }


    private final LayoutInflater mInflater;
    private List<PhotoEntity> mPhotos; // Cached copy of words

    PhotoListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);

        return new PhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        if (mPhotos != null) {
            PhotoEntity current = mPhotos.get(position);
            holder.photoItemView.setImageBitmap(BitmapFactory.decodeFile(current.getPath()));

        } else {
            // Covers the case of data not being ready yet.
            //holder.photoItemView.setImageBitmap(BitmapFactory.decodeFile(("")));
        }
    }

    void setPhotos(List<PhotoEntity> photos){
        mPhotos = photos;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mPhotos != null)
            return mPhotos.size();
        else return 0;
    }


    public String getPhotoPath(int index) {
        PhotoEntity current = mPhotos.get(index);
        return current.getPath();
    }

    public void setFilter(Date minDate, Date maxDate, String keyword){

        if (minDate != null && maxDate != null) {
            filterMinDate = minDate;
            filterMaxDate = maxDate;
        }

        if (keyword != null) {
            filterKeyword = keyword;
        } else {
            filterKeyword = "";
        }

            setFilteredPhotos(filterMinDate, filterMaxDate, filterKeyword);

    }

    public void setFilteredPhotos(Date minDate, Date maxDate, String keyword) {

        List<PhotoEntity> filteredList = new ArrayList<PhotoEntity> ();

        DateFormat format = new SimpleDateFormat("yyyyMMdd");

        int filePathLength = 0;

        if (mPhotos != null) {
            filePathLength = mPhotos.get(0).getPath().length();


            for (int i = 0; i < mPhotos.size(); i++) {
                String extension = mPhotos.get(i).getPath().substring(mPhotos.get(i).getPath().lastIndexOf("."));
                Date fileDate = null;
                String fileDateStr = mPhotos.get(i).getPath().substring(filePathLength - 32, filePathLength - 24);

                try {
                    fileDate = format.parse(fileDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ExifInterface exifInterface = null;
                try {
                    exifInterface = new ExifInterface(mPhotos.get(i).getPath());
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
                        System.out.println("adding " + mPhotos.get(i).getPath() + " to gallery");
                        filteredList.add(mPhotos.get(i));
                        continue;
                    }
                    if (keyword != null && comment != null) {
                        String commentLower = comment.toLowerCase();
                        String keywordLower = keyword.toLowerCase();
                        if (commentLower.contains(keywordLower)) {
                            filteredList.add(mPhotos.get(i));
                        }
                    }
                }

            }
        }

        mPhotos = filteredList;
        this.setPhotos(filteredList);

    }


}
