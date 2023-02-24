package com.android.rakuten.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AbsListView
import android.widget.ListView
import com.android.rakuten.R
import com.android.rakuten.utils.computeScrollY
import com.android.rakuten.utils.getComputedScrollY
import com.android.rakuten.utils.getListHeight
import com.android.rakuten.utils.isScrollYComputed
import kotlin.math.min

class CustomListView(context: Context, attrs: AttributeSet?) : ListView(context, attrs) {

    private var cachedVerticalScrollRange = 0
    private var quickReturnViewHeight = 0
    private var state = STATE_ONSCREEN
    private var scrollY = 0
    private var minRawY = 0

    fun setHeaderView(header: View, quickReturnView: View) {
        val params = LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
        )
        header.layoutParams = params
        addHeaderView(header)
        viewTreeObserver.addOnGlobalLayoutListener {
            quickReturnViewHeight = quickReturnView.height
            computeScrollY()
            cachedVerticalScrollRange = getListHeight()
        }

        setOnScrollListener(object : OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {}
            override fun onScroll(
                view: AbsListView,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int,
            ) {
                scrollY = 0
                var translationY = 0
                if (isScrollYComputed()) {
                    scrollY = getComputedScrollY()
                }
                val placeHolder = header.findViewById<View>(R.id.holder)
                val rawY: Int =
                    placeHolder.top - min(cachedVerticalScrollRange - height, scrollY)
                when (state) {
                    STATE_OFFSCREEN -> {
                        if (rawY <= minRawY) {
                            minRawY = rawY
                        } else {
                            state = STATE_RETURNING
                        }
                        translationY = rawY
                    }
                    STATE_ONSCREEN -> {
                        if (rawY < -quickReturnViewHeight) {
                            state = STATE_OFFSCREEN
                            minRawY = rawY
                        }
                        translationY = rawY
                    }
                    STATE_RETURNING -> {
                        translationY = rawY - minRawY - quickReturnViewHeight
                        if (translationY > 0) {
                            translationY = 0
                            minRawY = rawY - quickReturnViewHeight
                        }
                        if (rawY > 0) {
                            state = STATE_ONSCREEN
                            translationY = rawY
                        }
                        if (translationY < -quickReturnViewHeight) {
                            state = STATE_OFFSCREEN
                            minRawY = rawY
                        }
                    }
                }
                quickReturnView.translationY = translationY.toFloat()
            }
        })
    }


    companion object {
        private const val STATE_ONSCREEN = 0
        private const val STATE_OFFSCREEN = 1
        private const val STATE_RETURNING = 2
    }
}