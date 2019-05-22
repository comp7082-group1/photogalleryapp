package com.example.search;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
//
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.support.v4.app.Fragment;

public class SearchFragment extends Fragment implements SearchContract.View {

    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";

    private SearchContract.Presenter mPresenter;

    private EditText startDate;

    private EditText endDate;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void setPresenter(@NonNull SearchContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//
//        FloatingActionButton fab =
//                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task_done);
//        fab.setImageResource(R.drawable.ic_done);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPresenter.saveTask(mTitle.getText().toString(), mDescription.getText().toString());
//            }
//        });
    }


    @Override
    public void setStartDate() {

    }

    @Override
    public void setEndDate() {

    }

    @Override
    public void setCaption() {

    }

}
