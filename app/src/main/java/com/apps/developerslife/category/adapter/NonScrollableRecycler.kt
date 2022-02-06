package com.apps.developerslife.category.adapter

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView


class NonScrollableRecycler @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        //Ignore scroll events.
        return if (ev.action == MotionEvent.ACTION_MOVE) true else super.dispatchTouchEvent(ev)
    }
}