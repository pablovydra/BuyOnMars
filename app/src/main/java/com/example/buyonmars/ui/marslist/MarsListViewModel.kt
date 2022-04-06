package com.example.buyonmars.ui.marslist

import androidx.lifecycle.*
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
class MarsListViewModel @Inject constructor(private val marsUseCase: MarsUseCase) : ViewModel() {

    private var _properties = MutableLiveData<List<MarsProperty>>()
    var properties: LiveData<List<MarsProperty>>? = null
        get() = _properties

    private fun setProperties(properties: List<MarsProperty>) {
        _properties.postValue(properties)
    }

    private val buyProperties = MutableLiveData<List<MarsProperty>>()
    private val rentProperties = MutableLiveData<List<MarsProperty>>()
    val setAdapterOnView = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO )

    init {
        coroutineScope.launch(Dispatchers.IO) {
            getMarsPropertiesByFlows()
                .onStart {
                    loading.postValue(true)
                }
                .cancellable()
                .onCompletion {
                    filterPropertiesByTypes()
                    setAdapterOnView.postValue(true)
                }
                .collect { setProperties(it) }
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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}