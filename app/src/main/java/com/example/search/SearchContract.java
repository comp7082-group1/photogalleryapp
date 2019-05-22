package com.example.search;

import com.example.BasePresenter;
import com.example.BaseView;

public interface SearchContract {

    interface View extends BaseView<Presenter> {

        void setStartDate();

        void setEndDate();

        void setCaption();
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

        void search();
//
//        void populateTask();
//
//        boolean isDataMissing();
    }
}
