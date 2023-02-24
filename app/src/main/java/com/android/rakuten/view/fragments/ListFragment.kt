package com.android.rakuten.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.android.rakuten.R
import com.android.rakuten.data.model.GetRecentImagesResponseDo
import com.android.rakuten.databinding.HeaderBinding
import com.android.rakuten.databinding.ListFragmentBinding
import com.android.rakuten.utils.UiState
import com.android.rakuten.view.adapter.ImageListAdapter
import com.android.rakuten.viewmodel.ImagesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment() {
    private val listViewModel: ImagesViewModel by activityViewModels()
    private lateinit var binding: ListFragmentBinding
    private lateinit var adapter: ImageListAdapter

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
                        binding.listImages.setHeaderView(
                            HeaderBinding.inflate(layoutInflater).root,
                            binding.quickReturnView
                        )
                    }
                    is UiState.Loading -> {}
                    is UiState.Error -> {
                        binding.errorText.text = when {
                            it.message.isEmpty() -> resources.getString(R.string.network_error)
                            else -> it.message
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

    companion object {
        var photoObject: String = "photoObject"
    }
}