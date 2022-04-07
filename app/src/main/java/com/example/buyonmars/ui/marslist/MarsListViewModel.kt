package com.example.buyonmars.ui.marslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buyonmars.base.favorites.Favorite
import com.example.buyonmars.base.favorites.FavoriteRepository
import com.example.buyonmars.models.dto.ApiResource
import com.example.buyonmars.models.dto.MarsProperty
import com.example.buyonmars.models.usecase.MarsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MarsListViewModel @Inject constructor(
    private val marsUseCase: MarsUseCase,
    private val repository: FavoriteRepository
) : ViewModel() {

    private var _properties = MutableLiveData<List<MarsProperty>>()
    var properties: LiveData<List<MarsProperty>>? = null
        get() = _properties

    private fun setProperties(properties: List<MarsProperty>) {
        _properties.postValue(properties)
    }

    val favorites = MutableLiveData<List<Favorite>>(mutableListOf())
    private val buyProperties = MutableLiveData<List<MarsProperty>>()
    private val rentProperties = MutableLiveData<List<MarsProperty>>()
    val setAdapterOnView = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>(true)

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    init {
        coroutineScope.launch(Dispatchers.IO) {

            getFavoritesByFlows()
                .onStart {
                    loading.postValue(true)
                }
                .collect {
                    favorites.postValue(it)
                }

            getMarsPropertiesByFlows()
                .onStart {
                    loading.postValue(true)
                }
                .cancellable()
                .onCompletion {
                    filterPropertiesByTypes()
                    setAdapterOnView.postValue(true)
                    loading.postValue(false)
                }.collect {
                    setProperties(it)
                }

        }
    }

    private fun filterPropertiesByTypes() {
        _properties.value?.let { list ->
            if (list.isNotEmpty()) {
                buyProperties.postValue(list.filter { it.type == "buy" })
                rentProperties.postValue(list.filter { it.type == "rent" })
            }
        }
    }

    fun getMarsPropertiesByFlows(): Flow<List<MarsProperty>> = flow {
        var response = marsUseCase.getProperties()
        when (response.status) {
            ApiResource.Status.SUCCESS -> {
                response.data?.let {
                    emit(it)
                }
            }
            ApiResource.Status.ERROR -> {
            }
        }
    }

    fun getFavoritesByFlows(): Flow<List<Favorite>> = flow {
        emit(repository.getAll())
    }

    private fun getFavoritesDatabase() = viewModelScope.launch {
        favorites.value = repository.getAll()
    }

    fun insert(marsProperty: MarsProperty) = viewModelScope.launch {
        repository.insert(Favorite(marsProperty.id, marsProperty.url, marsProperty.type, marsProperty.price))
    }

    fun delete(marsProperty: MarsProperty) = viewModelScope.launch {
        repository.deleteById(marsProperty.id.toInt())
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}