package com.android.rakuten.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.rakuten.data.model.GetRecentImagesResponseDo
import com.android.rakuten.data.usecases.GetRecentImagesUseCase
import com.android.rakuten.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val getTrendingGifsUseCase: GetRecentImagesUseCase,
) : ViewModel() {

    private var _uiState = MutableStateFlow<UiState<GetRecentImagesResponseDo>>(UiState.Loading)
    val uiState: StateFlow<UiState<GetRecentImagesResponseDo>> = _uiState

    fun getRecentImages() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            getTrendingGifsUseCase.getAction()
                .flowOn(Dispatchers.IO)
                .catch {
                    _uiState.value = UiState.Error(it.toString())
                }
                .collect {
                    Log.e("getRecentImages()","called")
                    _uiState.value = UiState.Success(it)
                }
        }
    }
}