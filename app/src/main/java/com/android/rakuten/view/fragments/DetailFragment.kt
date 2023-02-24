package com.android.rakuten.view.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.rakuten.R
import com.android.rakuten.data.model.Photo
import com.android.rakuten.databinding.DetailsFragmentBinding
import com.android.rakuten.viewmodel.ImagesViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
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
    }

    private fun initViews() {
        val photo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(ListFragment.photoObject, Photo::class.java)
        } else {
            arguments?.getSerializable(ListFragment.photoObject) as? Photo
        }

        photo?.let {
            Picasso.get()
                .load("https://live.staticflickr.com/${it.server}/${it.id}_${it.secret}_w.jpg")
                .placeholder(R.drawable.place_holder_detailed_page)
                .into(binding.ivPhotoImage)
            binding.tvPhotoTitle.text = resources.getString(R.string.title_des, it.title?:"")
            binding.tvPhotoId.text = resources.getString(R.string.id, it.id?:"")
            binding.tvPhotoOwner.text = resources.getString(R.string.owner, it.owner?:"")
            binding.tvPhotoSecret.text = resources.getString(R.string.secret, it.secret?:"")
            binding.tvPhotoServer.text = resources.getString(R.string.server, it.server?:"")
            binding.tvPhotoFarm.text = resources.getString(R.string.farm, it.farm)
            binding.tvIsPublic.text = resources.getString(R.string.is_public, it.isPublic?:0)
            binding.tvIsFriend.text = resources.getString(R.string.is_friend, it.isFriend?:0)
            binding.tvIsFamily.text = resources.getString(R.string.is_family, it.isFamily?:0)
            listViewModel.setTitle(it.title ?: resources.getString(R.string.title))
        }?: run {
            binding.viewGroup.visibility = View.GONE
            binding.errorText.visibility = View.VISIBLE
            binding.errorText.text = resources.getString(R.string.something_wrong)
        }
    }
}