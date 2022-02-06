package com.apps.developerslife.category

interface CategoryContract {

    interface View {
        fun renderState(gifPageState: GifPageState, payload: Any?)
        fun scrollToPosition(position: Int)
    }

    interface Presenter {
        fun subscribe(view: View?)
        fun unsubscribe()

        fun onBackButtonPressed()
        fun onForwardButtonPressed()
        fun onRetryButtonPressed()
    }
}