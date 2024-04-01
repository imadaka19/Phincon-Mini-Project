package com.example.pokemonapp.api

import com.example.pokemonapp.models.api_responses.PokemonDetailItem
import com.example.pokemonapp.models.api_responses.PokemonListItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

// API Interface class to retrieve Pokemon Items and Pokemon Details

interface PokeApi {
    @GET("/api/v2/pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int,

        ): Response<PokemonDetailItem>

}

//Base Url
//"https://pokeapi.co/"

// GET PokemonDetail
//https://pokeapi.co/api/v2/pokemon/{number}/

//GET PokemonList
//https://pokeapi.co/api/v2/pokemon/
