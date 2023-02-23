package com.android.rakuten.view.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.rakuten.data.model.Photo
import com.android.rakuten.databinding.DetailsFragmentBinding
import com.android.rakuten.databinding.ListFragmentBinding
import com.android.rakuten.viewmodel.ImagesViewModel
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class DetailFragment : Fragment()  {
    private val listViewModel: ImagesViewModel by activityViewModels()
    private lateinit var binding: DetailsFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DetailsFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        //initObserver()
    }

    private fun initViews() {
        val photo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(ListFragment.photoObject,Photo::class.java)
        } else {
            arguments?.getSerializable(ListFragment.photoObject) as? Photo
        }
        Log.e("arguments","are: $photo")
    }
}