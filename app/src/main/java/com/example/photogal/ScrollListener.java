package com.example.photogal;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ScrollListener extends RecyclerView.OnScrollListener {

    LinearLayoutManager layoutManager;
    int firstVisibleItemPosition = 0;

    public ScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        firstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
    }

    public int getCurrent(){
        return firstVisibleItemPosition;
    }
}
