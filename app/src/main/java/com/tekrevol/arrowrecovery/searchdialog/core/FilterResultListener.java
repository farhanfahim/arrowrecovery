package com.tekrevol.arrowrecovery.searchdialog.core;

import java.util.ArrayList;

public interface FilterResultListener<T> {
    void onFilter(ArrayList<T> items);
}