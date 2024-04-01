package com.example.pokemonapp.ui.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.pokemonapp.R
import com.example.pokemonapp.databinding.FragmentDetailBinding
import com.example.pokemonapp.models.api_responses.PokemonDetailItem
import com.example.pokemonapp.models.customModels.CustomPokemonListItem
import com.example.pokemonapp.ui.activities.MainActivity
import com.example.pokemonapp.ui.viewmodels.DetailViewModel
import com.example.pokemonapp.util.ImageUtils
import com.example.pokemonapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

private const val TAG = "DetailFragment"

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {
    lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    lateinit var mPokemon: CustomPokemonListItem

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)

//        binding.fragmentDetail_back.setOnClickListener {
//            findNavController().popBackStack()
//        }
        // checking for details passed from list fragment
        arguments?.let {
            it.getParcelable<CustomPokemonListItem>("pokemon")?.let { pokemon ->
                mPokemon = pokemon
                pokemon.type?.let { it1 -> setType(it1) }
                // setup name
                binding.detailFragmentTitleName.text = pokemon.name.capitalize()
                // query api for pokemon details
                getPokemonDetails(pokemon.apiId)
                subscribeObservers()
            }
        }

        // setup saveButton clickListener if pokemon is not saved

        if (this::mPokemon.isInitialized) {
            binding.detailFragmentSaveButton.setOnClickListener {
                // Check if the Pokemon is already saved
                if (mPokemon.isSaved == "false") {
                    // Generate a random number between 0 and 1
                    val chance = Math.random()
                    // 50% probability
                    if (chance < 0.5) {
                        // If the chance is less than 0.5, consider the Pokemon as caught
                        mPokemon.isSaved = "true"
                        viewModel.savePokemon(mPokemon)
                        binding.detailFragmentSaveButton.text = "Saved"
                        Toast.makeText(context, "Pokemon caught!", Toast.LENGTH_SHORT).show()
                    } else {
                        // If the chance is 0.5 or more, the attempt to catch the Pokemon fails
                        Toast.makeText(context, "Failed to catch Pokemon. Try again!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // If the Pokemon is already saved, no action is needed
                    binding.detailFragmentSaveButton.text = "Saved"
                }
            }
        }


    }

    private fun setType(type: String) {
        binding.detailFragmentType.text = "Type: ${type.capitalize(Locale.ROOT)}"
    }

    private fun subscribeObservers() {
        viewModel.pokemonDetails.observe(viewLifecycleOwner, Observer { pokemondetails ->
            when (pokemondetails) {
                is Resource.Success -> {
                    pokemondetails.data?.let { pokemon ->
                        setupView(pokemon)
                    }
                }
                is Resource.Error -> {
                    Log.d(TAG, pokemondetails.message.toString())
                    Toast.makeText(requireContext(), "Error retrieving results please check internet connection", Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    Log.d(TAG, pokemondetails.message.toString())
                }
                is Resource.Expired -> {
                    pokemondetails.data?.let { pokemon ->
                        setupView(pokemon)

                        //inform user that cache has expired and we cannot retrieve up to date details
                        Toast.makeText(
                            requireContext(),
                            "Unable to retrieve up to date details !, please check network connectivity",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    }
                }
            }

        })


        viewModel.pokemonSaveIntent.observe(viewLifecycleOwner, Observer { status ->

            if (status) {
                Toast.makeText(requireContext(), "Pokemon has been saved to your deck", Toast.LENGTH_SHORT).show()
            }

        })
    }

    //Setup Pokemon card info
    private fun setupView(pokemon: PokemonDetailItem) {
        // load image
        pokemon.sprites.otherSprites.artwork.front_default?.let { image ->
            ImageUtils.loadImage(requireContext(), binding.detailFragmentImage, image)
        }


        // load abilities

        for (i in pokemon.abilities) {
            val textView = TextView(requireContext())
            textView.text = i.ability.name.capitalize(Locale.ROOT)
            textView.textSize = 15f
            textView.setTextColor(Color.BLACK)
            textView.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            binding.abilitiesContainer.addView(textView)


        }

        val pokemonstats = mutableListOf<Int>()
        // load stats

        for (i in pokemon.stat) {
            val textView = TextView(requireContext())
            val progressBar = ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal)
            progressBar.progress = i.baseStat ?: 0
            progressBar.progressTintList = ColorStateList.valueOf(Color.BLACK)
            textView.text = i.stat.name.capitalize(Locale.ROOT) + " ${i.baseStat}"
            textView.textSize = 15f
            textView.setTextColor(Color.BLACK)
            textView.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            i.baseStat?.let { pokemonstats.add(it) }
            binding.statsContainer.addView(textView)
            binding.statsContainer.addView(progressBar)


        }

    }

    private fun getPokemonDetails(id: Int?) {
        if (id != null) {
            viewModel.getPokemonDetails(id)
        }
    }

}