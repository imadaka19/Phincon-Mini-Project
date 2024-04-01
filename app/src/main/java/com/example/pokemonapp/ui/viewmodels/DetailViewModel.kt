package com.example.pokemonapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonapp.models.api_responses.PokemonDetailItem
import com.example.pokemonapp.models.customModels.CustomPokemonListItem
import com.example.pokemonapp.repository.DefaultRepository
import com.example.pokemonapp.util.Resource
import com.example.pokemonapp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: DefaultRepository) : ViewModel() {

    // single live event to stop stale items being stored in Live Data
    private val _pokemonDetails = SingleLiveEvent<Resource<PokemonDetailItem>>()
    val pokemonDetails: LiveData<Resource<PokemonDetailItem>>
        get() = _pokemonDetails


    private val _pokemonSaveIntent = MutableLiveData<Boolean>()
    val pokemonSaveIntent: LiveData<Boolean>
        get() = _pokemonSaveIntent

    // value for map plotting, held in view model to ensure the same location is plotted after rotation
    val plotLeft = (0..600).random()
    val plotTop = (0..600).random()


    fun getPokemonDetails(id: Int) {
        _pokemonDetails.postValue(Resource.Loading("loading"))
        viewModelScope.launch(Dispatchers.IO) {
            _pokemonDetails.postValue(repository.getPokemonDetail(id))
        }
    }


    fun savePokemon(customPokemonListItem: CustomPokemonListItem) {
        _pokemonSaveIntent.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            repository.savePokemon(customPokemonListItem)
        }
    }


}