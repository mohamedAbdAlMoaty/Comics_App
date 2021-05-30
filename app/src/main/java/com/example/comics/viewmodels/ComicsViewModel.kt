package com.example.comics.viewmodels

import com.example.comics.utils.NetworkHelper
import com.example.comics.utils.Resource
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comics.models.ComicsInfo
import com.example.comics.repository.ComicsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ComicsViewModel@ViewModelInject constructor(private val postRepository: ComicsRepository, private val networkHelper: NetworkHelper, @Assisted private val savedStateHandle: SavedStateHandle): ViewModel() {

    private val _posts = MutableStateFlow<Resource<List<ComicsInfo>>>(Resource.loading(null))
    val posts: StateFlow<Resource<List<ComicsInfo>>>
        get() = _posts


     fun getComics(id:Int) {
        viewModelScope.launch {
            _posts.value=(Resource.loading(null))
            // check if the network is Connected
            if (networkHelper.isNetworkConnected()) {
                //
                postRepository.getComicsFromNetwork(id).let {
                    if (it.isSuccessful) {
                        //  if the network is Connected first you have to insert the new rows from api and then simply update your UI by local Database
                        _posts.value=(Resource.loading(postRepository.getComicsFromDb(it.body())))

                    } else _posts.value=(Resource.error(it.errorBody().toString(), null))
                }
            } else{
                // if the network isn't connected show the error message
                _posts.value=(Resource.error("No internet connection", null))
            }
            // after checking the network update your UI by local Database directly
            _posts.value=(Resource.success(postRepository.getComicsFromDb()))

        }
    }

}