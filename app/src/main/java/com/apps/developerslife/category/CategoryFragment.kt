package com.apps.developerslife.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.apps.developerslife.R
import com.apps.developerslife.category.adapter.GifAdapter
import com.apps.developerslife.domain.usecase.UseCaseHolder
import com.apps.developerslife.model.GifItem

class CategoryFragment : Fragment(), CategoryContract.View {

    private val presenter: CategoryContract.Presenter = CategoryPresenter(
        UseCaseHolder.loadGifUseCase,
        UseCaseHolder.loadGifsUseCase
    )
    private val gifAdapter: GifAdapter = GifAdapter()
    private val gifRecycler: RecyclerView?
        get() = view?.findViewById(R.id.recyclerView)
    private val snapHelper = PagerSnapHelper()

    private val buttonForward: ImageView?
        get() = view?.findViewById(R.id.forward_image_view)
    private val buttonBack: ImageView?
        get() = view?.findViewById(R.id.back_image_view)

    private val progressBar: ProgressBar?
        get() = view?.findViewById(R.id.progressBar)
    private val dataLoadErrorTextView: TextView?
        get() = view?.findViewById(R.id.dataLoadErrorTextView)
    private val retryButton: Button?
        get() = view?.findViewById(R.id.retryButton)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setGifRecycler()
        presenter.subscribe(view = this)

        buttonBack?.setOnClickListener {
            presenter.onBackButtonPressed()
        }

        buttonForward?.setOnClickListener {
            presenter.onForwardButtonPressed()
        }

        retryButton?.setOnClickListener {
            presenter.onRetryButtonPressed()
        }
    }

    override fun onDestroyView() {
        presenter.unsubscribe()
        super.onDestroyView()
    }

    override fun renderState(gifPageState: GifPageState, payload: Any?) {
        when (gifPageState) {
            GifPageState.LOADING -> {
                dataLoadErrorTextView?.visibility = View.GONE
                retryButton?.visibility = View.GONE
                progressBar?.visibility = View.VISIBLE
                buttonBack?.visibility = View.GONE
                buttonForward?.visibility = View.GONE
            }
            GifPageState.ERROR -> {
                dataLoadErrorTextView?.visibility = View.VISIBLE
                retryButton?.visibility = View.VISIBLE
                progressBar?.visibility = View.GONE
                buttonBack?.visibility = View.GONE
                buttonForward?.visibility = View.GONE
            }
            GifPageState.SHOW_DATA -> {
                dataLoadErrorTextView?.visibility = View.GONE
                retryButton?.visibility = View.GONE
                progressBar?.visibility = View.GONE
                buttonBack?.visibility = View.VISIBLE
                buttonForward?.visibility = View.VISIBLE

                val gifPageData: GifPageData = payload as? GifPageData ?: return
                val items: List<GifItem> = gifPageData.gifs
                if (gifPageData.areGifsUpdated) gifAdapter.update(items)
                buttonBack?.isEnabled = gifPageData.isBackButtonEnabled
                buttonForward?.isEnabled = gifPageData.isForwardButtonEnabled
            }
        }
    }

    override fun scrollToPosition(position: Int) {
        gifRecycler?.smoothScrollToPosition(position)
    }

    private fun setGifRecycler() {
        gifRecycler?.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        gifRecycler?.adapter = gifAdapter
        snapHelper.attachToRecyclerView(gifRecycler)
    }
}