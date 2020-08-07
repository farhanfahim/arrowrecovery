package com.tekrevol.arrowrecovery.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginatedRecyclerOnScrollListener :
    RecyclerView.OnScrollListener() {

    private var previousTotal =
        0 // The total number of _allItems in the dataset after the last load
    private var loading = true // True if we are still waiting for the last set of data to load.

    var visibleThreshold =
        5 // The minimum amount of _allItems to have below your current scroll position before loading more.

    open var lastLoadedPage = 0

    private var currentPage = 0

    var pageSize: Int? = null
    var gridLayoutManager: GridLayoutManager? = null
    var linearLayoutManager: LinearLayoutManager? = null

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = recyclerView.childCount
        var totalItemCount: Int? = null
        var firstVisibleItem: Int? = null
        var lastVisibleItemPosition: Int? = null
        if (gridLayoutManager != null) {
            totalItemCount = gridLayoutManager!!.itemCount
            firstVisibleItem = gridLayoutManager!!.findFirstVisibleItemPosition()
            lastVisibleItemPosition = gridLayoutManager!!.findLastVisibleItemPosition()
        }
        if (linearLayoutManager != null) {
            totalItemCount = linearLayoutManager!!.itemCount
            firstVisibleItem = linearLayoutManager!!.findFirstVisibleItemPosition()
            lastVisibleItemPosition = linearLayoutManager!!.findLastVisibleItemPosition()
        }

        if (pageSize != null && lastVisibleItemPosition!! / pageSize!! != currentPage) {
            currentPage = lastVisibleItemPosition!! / pageSize!!
            onPageChanged(currentPage)
        }

        if (loading) {
            if (totalItemCount!! > previousTotal) {
                loading = false
                previousTotal = totalItemCount!!
            }
        }
        if (!loading && totalItemCount!! - visibleItemCount <= firstVisibleItem!! + visibleThreshold) {
            // End has been reached
            lastLoadedPage += 1
//            Log.d("ScrollListenerT", "$lastLoadedPage")
            onLoadMore(lastLoadedPage)
            loading = true
        }
    }

    open fun onPageChanged(currentPage: Int) {}

    abstract fun onLoadMore(page: Int)

    fun reset() {
        previousTotal = 0
        loading = true
        currentPage = 0
        lastLoadedPage = 0
    }
}