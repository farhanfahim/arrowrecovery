package com.tekrevol.arrowrecovery.searchdialog;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.tekrevol.arrowrecovery.R;
import com.tekrevol.arrowrecovery.searchdialog.adapters.SearchDialogAdapter;
import com.tekrevol.arrowrecovery.searchdialog.core.BaseFilter;
import com.tekrevol.arrowrecovery.searchdialog.core.BaseSearchDialogCompat;
import com.tekrevol.arrowrecovery.searchdialog.core.FilterResultListener;
import com.tekrevol.arrowrecovery.searchdialog.core.OnPerformFilterListener;
import com.tekrevol.arrowrecovery.searchdialog.core.SearchResultListener;
import com.tekrevol.arrowrecovery.searchdialog.core.Searchable;

import java.util.ArrayList;

public class SimpleSearchDialogCompat<T extends Searchable> extends BaseSearchDialogCompat<T> {
    private String mTitle;
    private String mSearchHint;
    private SearchResultListener<T> mSearchResultListener;

    private TextView mTxtTitle;
    private EditText mSearchBox;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private boolean isSearchable;
    // In case you are doing process in another thread
    // and wanted to update the progress in that thread
    private Handler mHandler;
    private View mSearchContainer;

    public SimpleSearchDialogCompat(
            Context context, boolean isSearchable, String title, String searchHint,
            @Nullable Filter filter, ArrayList<T> items,
            SearchResultListener<T> searchResultListener
    ) {
        super(context, items, filter, null, null);
        this.isSearchable = isSearchable;
        init(title, searchHint, filter, items, searchResultListener);
    }

    public SimpleSearchDialogCompat(
            Context context, String title, String searchHint,
            @Nullable Filter filter, ArrayList<T> items,
            SearchResultListener<T> searchResultListener
    ) {
        super(context, items, filter, null, null);
        isSearchable = true;
        init(title, searchHint, filter, items, searchResultListener);

    }

    private void init(
            String title, String searchHint, @Nullable Filter filter, ArrayList<T> items,
            SearchResultListener<T> searchResultListener
    ) {
        mTitle = title;
        mSearchHint = searchHint;
        filter = filter;
        items = items;
        mSearchResultListener = searchResultListener;
        setFilterResultListener(new FilterResultListener<T>() {
            @Override
            public void onFilter(ArrayList<T> items) {
                ((SearchDialogAdapter) getAdapter())
                        .setSearchTag(mSearchBox.getText().toString())
                        .setItems(items);
            }
        });
        mHandler = new Handler();
    }

    @Override
    protected void getView(View view) {
        setContentView(view);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setCancelable(true);
        mTxtTitle = view.findViewById(R.id.txt_title);
        mSearchContainer = view.findViewById(R.id.search_container);
        mSearchBox = view.findViewById(getSearchBoxId());

        mRecyclerView = view.findViewById(getRecyclerViewId());
        mProgressBar = view.findViewById(R.id.progress);
        mTxtTitle.setText(mTitle);
        mSearchBox.setHint(mSearchHint);
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.GONE);
      /*  view.findViewById(R.id.dummy_background)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });*/
        final SearchDialogAdapter adapter = new SearchDialogAdapter<>(getContext(),
                R.layout.dialog_item, getItems()
        );
        adapter.setSearchResultListener(mSearchResultListener);
        adapter.setSearchDialog(this);
        setFilterResultListener(getFilterResultListener());
        setAdapter(adapter);
        if (isSearchable) {
            mSearchBox.requestFocus();
        } else
            mSearchContainer.setVisibility(View.GONE);

        ((BaseFilter<T>) getFilter()).setOnPerformFilterListener(new OnPerformFilterListener() {
            @Override
            public void doBeforeFiltering() {
                setLoading(true);
            }

            @Override
            public void doAfterFiltering() {
                setLoading(false);
            }
        });
    }

    public SimpleSearchDialogCompat setSearchHint(String searchHint) {
        mSearchHint = searchHint;
        if (mSearchBox != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mSearchBox.setHint(mSearchHint);
                }
            });
        }
        return this;
    }

    public void setLoading(final boolean isLoading) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mProgressBar != null) {
                    mRecyclerView.setVisibility(!isLoading ? View.VISIBLE : View.GONE);
                }
                if (mRecyclerView != null) {
                    mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                }
            }
        });
    }

    public SimpleSearchDialogCompat setSearchResultListener(
            SearchResultListener<T> searchResultListener
    ) {
        mSearchResultListener = searchResultListener;
        return this;
    }

    @LayoutRes
    @Override
    protected int getLayoutResId() {
        return R.layout.search_dialog_compat;
    }

    @IdRes
    @Override
    protected int getSearchBoxId() {
        return R.id.txt_search;
    }

    @IdRes
    @Override
    protected int getRecyclerViewId() {
        return R.id.rv_items;
    }

    public EditText getSearchBox() {
        return mSearchBox;
    }

    public String getTitle() {
        return mTitle;
    }

    public SimpleSearchDialogCompat setTitle(String title) {
        mTitle = title;
        if (mTxtTitle != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mTxtTitle.setText(mTitle);
                }
            });
        }
        return this;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public TextView getTitleTextView() {
        return mTxtTitle;
    }
}