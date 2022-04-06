package com.example.buyonmars.ui.marslist

import android.util.Log
import androidx.lifecycle.*
import com.example.buyonmars.base.favorites.Favorite
import com.example.buyonmars.base.favorites.FavoriteRepository
import com.example.buyonmars.models.dto.ApiResource
import com.example.buyonmars.models.dto.MarsProperty
import com.example.buyonmars.models.usecase.MarsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarsListViewModel @Inject constructor(private val marsUseCase: MarsUseCase, private val repository: FavoriteRepository) : ViewModel() {

    private var _properties = MutableLiveData<List<MarsProperty>>()
    var properties: LiveData<List<MarsProperty>>? = null
        get() = _properties

    private fun setProperties(properties: List<MarsProperty>) {
        _properties.postValue(properties)
    }

    val favorites = MutableLiveData<List<Favorite>>(listOf())

    private val buyProperties = MutableLiveData<List<MarsProperty>>()
    private val rentProperties = MutableLiveData<List<MarsProperty>>()
    val setAdapterOnView = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    init {
        getFavoritesDatabase()

        coroutineScope.launch(Dispatchers.IO) {
            getMarsPropertiesByFlows()
                .onStart {
                    loading.postValue(true)
                }
                .cancellable()
                .onCompletion {
                    filterPropertiesByTypes()
                    setAdapterOnView.postValue(true)
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
                    Log.i("sky", "DATABASE SIZE: ${it.size}")
                }
            }
            ApiResource.Status.ERROR -> {
            }
        }
    }

    private fun getFavoritesDatabase() = viewModelScope.launch {
        favorites.value = repository.getAll()
        Log.i("sky", "FAVORITE ITEMS: ${(favorites.value as MutableList<Favorite>).size}")
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