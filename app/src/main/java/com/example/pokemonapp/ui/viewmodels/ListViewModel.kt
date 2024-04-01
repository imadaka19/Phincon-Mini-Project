package com.example.pokemonapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonapp.models.customModels.CustomPokemonListItem
import com.example.pokemonapp.repository.DefaultRepository
import com.example.pokemonapp.util.Resource
import com.example.pokemonapp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val repository: DefaultRepository) : ViewModel() {
    private val _pokemonList = SingleLiveEvent<Resource<List<CustomPokemonListItem>>>()
    val pokemonList: LiveData<Resource<List<CustomPokemonListItem>>>
        get() = _pokemonList


    init {
        Log.d("oncreate", "oncreate viewmodel")
        getPokemonList()
    }


    fun getPokemonList() {
        _pokemonList.postValue(Resource.Loading("loading"))
        viewModelScope.launch(Dispatchers.IO) {
            _pokemonList.postValue(repository.getPokemonList())
        }
    }

    fun getNextPage(){
        viewModelScope.launch(Dispatchers.IO) {
            _pokemonList.postValue(repository.getPokemonListNextPage())
        }
    }
}