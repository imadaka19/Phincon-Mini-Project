package com.example.pokemonapp.di

import android.content.Context
import com.example.pokemonapp.api.PokeApi
import com.example.pokemonapp.persistence.PokemonDao
import com.example.pokemonapp.persistence.PokemonDatabase
import com.example.pokemonapp.repository.DefaultRepository
import com.example.pokemonapp.repository.MainRepository
import com.example.pokemonapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//Hilt Module Class for providing dependencies to be used in repository, viewmodels and workmanager
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePokeApi(): PokeApi = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PokeApi::class.java)

    @Provides
    fun providePokemonDao(@ApplicationContext appContext: Context) : PokemonDao {
        return PokemonDatabase.getDatabase(appContext).pokemonDao()
    }


    @Singleton
    @Provides
    fun provideMainRepository(api: PokeApi, dao: PokemonDao): DefaultRepository = MainRepository(api, dao)

}