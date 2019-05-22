package com.example.gallery;

import com.example.BasePresenter;
import com.example.BaseView;
import com.example.search.SearchContract;

import java.util.Date;

public interface GalleryContract {

    interface View extends BaseView<SearchContract.Presenter> {

//        void showEmptyTaskError();
//
//        void showTasksList();
//
//        void setTitle(String title);
//
//        void setDescription(String description);
//
//        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void search(Date startDate, Date endDate, String caption);
//
//        void populateGallery();
//
//        boolean isDataMissing();
    }
}
