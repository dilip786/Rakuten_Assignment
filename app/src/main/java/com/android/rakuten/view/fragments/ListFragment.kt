package com.android.rakuten.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ListView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.android.rakuten.R
import com.android.rakuten.data.model.GetRecentImagesResponseDo
import com.android.rakuten.databinding.HeaderBinding
import com.android.rakuten.databinding.ListFragmentBinding
import com.android.rakuten.utils.*
import com.android.rakuten.view.adapter.ImageListAdapter
import com.android.rakuten.viewmodel.ImagesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.min

@AndroidEntryPoint
class ListFragment : Fragment() {
    private val listViewModel: ImagesViewModel by activityViewModels()
    private lateinit var binding: ListFragmentBinding
    private lateinit var adapter: ImageListAdapter

    private var cachedVerticalScrollRange = 0
    private var quickReturnViewHeight = 0
    private var state = STATE_ONSCREEN
    private var scrollY = 0
    private var minRawY = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ListFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserver()
    }

    private fun initViews() {
        adapter = ImageListAdapter(requireContext(), arrayListOf()) {
            val bundle = bundleOf()
            bundle.putSerializable(photoObject, it)
            view?.findNavController()?.navigate(R.id.detailFragment, bundle)
        }
        binding.listImages.adapter = adapter
    }


    private fun initObserver() {
        lifecycleScope.launch(Dispatchers.Main) {
            listViewModel.uiState.collect {
                handleViews(it)
                when (it) {
                    is UiState.Success -> {
                        adapter.refreshList(it.data.photos?.photosList ?: arrayListOf())
                        setupListView(
                            binding.listImages,
                            HeaderBinding.inflate(layoutInflater).root,
                            binding.quickReturnView
                        )
                    }
                    is UiState.Loading -> {}
                    is UiState.Error -> {
                        binding.errorText.text  =  when{
                            it.message.isEmpty() ->  resources.getString(R.string.network_error)
                            else ->  it.message
                        }
                    }
                }
            }
        }
        lifecycleScope.launch(Dispatchers.Main) {
            listViewModel.uiTitle.collect { title ->
                title?.let { binding.tvTitle.text = it }
            }
        }
    }

    private fun handleViews(uiState: UiState<GetRecentImagesResponseDo>) {
        binding.progress.visibility = View.GONE
        binding.listImages.visibility = View.GONE
        binding.errorText.visibility = View.GONE
        when (uiState) {
            is UiState.Success -> {
                binding.quickReturnView.visibility = View.VISIBLE
                binding.listImages.visibility = View.VISIBLE
            }
            is UiState.Loading -> binding.progress.visibility = View.VISIBLE
            is UiState.Error -> binding.errorText.visibility = View.VISIBLE
        }
    }

    private fun setupListView(listView: ListView, header: View, quickReturnView: View) {
        val params = AbsListView.LayoutParams(
            AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT
        )
        header.layoutParams = params
        listView.addHeaderView(header)
        listView.viewTreeObserver.addOnGlobalLayoutListener {
            quickReturnViewHeight = quickReturnView.height
            listView.computeScrollY()
            cachedVerticalScrollRange = listView.getListHeight()
        }
        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(
                view: AbsListView, firstVisibleItem: Int,
                visibleItemCount: Int, totalItemCount: Int,
            ) {
                scrollY = 0
                var translationY = 0
                if (listView.isScrollYComputed()) {
                    scrollY = listView.getComputedScrollY()
                }
                val placeHolder = header.findViewById<View>(R.id.holder)
                val rawY: Int =
                    placeHolder.top - min(cachedVerticalScrollRange - listView.height, scrollY)
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

            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}
        })
    }

    companion object {
        private const val STATE_ONSCREEN = 0
        private const val STATE_OFFSCREEN = 1
        private const val STATE_RETURNING = 2
        var photoObject: String = "photoObject"
    }
}