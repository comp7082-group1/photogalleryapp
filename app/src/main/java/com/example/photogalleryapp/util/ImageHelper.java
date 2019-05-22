package com.example.photogalleryapp.util;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.photogalleryapp.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageHelper {

    // save image with timestamp in name
    public static File createImageFile(Bitmap imageBitmap) throws IOException {
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
}
