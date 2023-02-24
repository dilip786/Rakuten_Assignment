package com.android.rakuten.utils

import android.view.View
import android.view.View.MeasureSpec
import android.widget.ListView


private lateinit var mItemOffsetY: IntArray

private var scrollIsComputed: Boolean = false
private var listHeight: Int = 0

fun ListView.getListHeight(): Int {
    return listHeight
}

fun ListView.isScrollYComputed(): Boolean {
    return scrollIsComputed
}

fun ListView.computeScrollY() {
    listHeight = 0
    val itemCount = adapter.count
    mItemOffsetY = IntArray(itemCount)
    for (i in 0 until itemCount) {
        val view: View = adapter.getView(i, null, this)
        view.measure(
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        mItemOffsetY[i] = listHeight
        listHeight += view.measuredHeight
    }
    scrollIsComputed = true
}

fun ListView.getComputedScrollY(): Int {
    return mItemOffsetY[firstVisiblePosition] - (getChildAt(0)?.top ?: 0)
}