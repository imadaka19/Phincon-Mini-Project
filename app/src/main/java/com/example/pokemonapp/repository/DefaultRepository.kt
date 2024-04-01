package com.example.pokemonapp.repository

import com.example.pokemonapp.models.api_responses.PokemonDetailItem
import com.example.pokemonapp.models.customModels.CustomPokemonListItem
import com.example.pokemonapp.util.Resource


// interface class to be overridden by main Repository

interface DefaultRepository {
    suspend fun getPokemonList(): Resource<List<CustomPokemonListItem>>
    suspend fun getPokemonListNextPage(): Resource<List<CustomPokemonListItem>>
    suspend fun getPokemonSavedPokemon(): Resource<List<CustomPokemonListItem>>
    suspend fun getPokemonDetail(id: Int): Resource<PokemonDetailItem>
    suspend fun getLastStoredPokemon(): CustomPokemonListItem
    suspend fun savePokemon(pokemonListItem: CustomPokemonListItem)

}