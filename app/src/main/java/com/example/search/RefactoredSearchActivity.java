package com.example.search;

import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RefactoredSearchActivity {
    protected ArrayList<String> photoGallery = null;
    protected Date minDate = new Date(Long.MIN_VALUE);
    protected Date maxDate = new Date(Long.MAX_VALUE);

    protected ArrayList RefactoredSearchActivity(Date startDate, Date endDate, String keyword, ArrayList imageGallery){
        return filterList(imageGallery, keyword, startDate, endDate);
    }

    protected ArrayList filterList(ArrayList fileList, String keyword, Date startDate, Date endDate){
        ArrayList<String> filteredList = null;
        for (Object f : fileList) {
            String a = f.toString();
            DateFormat format = new SimpleDateFormat("yyyyMMdd");
            Date fileDate = null;
            String fileDateStr = a.substring(a.length()-32, a.length()-24);

            try {
                fileDate = format.parse(fileDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ExifInterface exifInterface = null;
            try {
                exifInterface = new ExifInterface(a);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (startDate != null && endDate != null) {
                minDate = startDate;
                maxDate = endDate;
            }

            String comment;
            comment = exifInterface.getAttribute(ExifInterface.TAG_USER_COMMENT);

            if (filterDate(minDate, maxDate, fileDate) || filterKeyword(keyword, comment)) {
                photoGallery.add(a);
            }

        }
        return filteredList;
    }

    protected Boolean filterDate(Date startDate, Date endDate, Date fileDate){
        if (fileDate.compareTo(minDate) >= 0 && fileDate.compareTo(maxDate) <= 0 ) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    protected Boolean filterKeyword(String keyword, String comment){
        if (keyword != null && comment != null) {
            String commentLower = comment.toLowerCase();
            String keywordLower = keyword.toLowerCase();
            if (commentLower.contains(keywordLower)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

}
