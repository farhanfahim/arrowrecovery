package com.tekrevol.arrowrecovery.searchdialog.core;


public interface SearchResultListener<T> {
    void onSelected(BaseSearchDialogCompat dialog, T item, int position);
}