package com.tekrevol.arrowrecovery.searchdialog.core;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.tekrevol.arrowrecovery.searchdialog.SimpleSearchFilter;

import java.util.ArrayList;

public abstract class BaseSearchDialogCompat<T extends Searchable> extends AppCompatDialog implements Filterable {
    protected boolean mFilterAutomatically = true;
    private Filter mFilter;
    private ArrayList<T> mItems;
    private RecyclerView.Adapter mAdapter;
    private FilterResultListener<T> mFilterResultListener;
    private OnPerformFilterListener mOnPerformFilterListener;

    public BaseSearchDialogCompat(
            Context context, ArrayList<T> items, Filter filter,
            RecyclerView.Adapter adapter,
            FilterResultListener filterResultListener
    ) {
        this(context);
        mItems = items;
        mFilter = filter;
        mAdapter = adapter;
        mFilterResultListener = filterResultListener;
    }

    public BaseSearchDialogCompat(
            Context context, ArrayList<T> items, Filter filter,
            RecyclerView.Adapter adapter,
            FilterResultListener filterResultListener, int theme
    ) {
        this(context, theme);
        mItems = items;
        mFilter = filter;
        mAdapter = adapter;
        mFilterResultListener = filterResultListener;
    }

    public BaseSearchDialogCompat(Context context) {
        super(context);
    }

    public BaseSearchDialogCompat(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(getContext()).inflate(getLayoutResId(), null);
        getView(view);
        EditText searchBox = view.findViewById(getSearchBoxId());
        RecyclerView recyclerView = view.findViewById(getRecyclerViewId());
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isFilterAutomatically()) {
                    getFilter().filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false
        ));
        recyclerView.setAdapter(mAdapter);
    }

    protected abstract void getView(View view);

    @LayoutRes
    protected abstract int getLayoutResId();

    @IdRes
    protected abstract int getSearchBoxId();

    @IdRes
    protected abstract int getRecyclerViewId();

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new SimpleSearchFilter<>(mItems, mFilterResultListener, true, 0.33f);
        }
        return mFilter;
    }

    public BaseSearchDialogCompat setFilter(Filter filter) {
        mFilter = filter;
        return this;
    }

    public ArrayList<T> getItems() {
        return mItems;
    }

    public BaseSearchDialogCompat setItems(ArrayList<T> items) {
        mItems = items;
        return this;
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public BaseSearchDialogCompat setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        return this;
    }

    public FilterResultListener<T> getFilterResultListener() {
        return mFilterResultListener;
    }

    public BaseSearchDialogCompat setFilterResultListener(FilterResultListener<T> filterResultListener) {
        mFilterResultListener = filterResultListener;
        return this;
    }

    public OnPerformFilterListener getOnPerformFilterListener() {
        return mOnPerformFilterListener;
    }

    public BaseSearchDialogCompat setOnPerformFilterListener(OnPerformFilterListener onPerformFilterListener) {
        mOnPerformFilterListener = onPerformFilterListener;
        return this;
    }

    public boolean isFilterAutomatically() {
        return mFilterAutomatically;
    }

    public BaseSearchDialogCompat setFilterAutomatically(boolean filterAutomatically) {
        mFilterAutomatically = filterAutomatically;
        return this;
    }
}