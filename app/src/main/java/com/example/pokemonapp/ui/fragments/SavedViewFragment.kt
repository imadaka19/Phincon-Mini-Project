package com.example.pokemonapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pokemonapp.R
import com.example.pokemonapp.databinding.FragmentSavedBinding
import com.example.pokemonapp.models.customModels.CustomPokemonListItem
import com.example.pokemonapp.ui.activities.MainActivity
import com.example.pokemonapp.ui.adapters.PokemonListAdapter
import com.example.pokemonapp.ui.adapters.PokemonSavedListAdapter
import com.example.pokemonapp.ui.viewmodels.SavedViewModel
import com.example.pokemonapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SavedViewFragment"

@AndroidEntryPoint
class SavedViewFragment : Fragment(R.layout.fragment_saved) {

    lateinit var binding: FragmentSavedBinding
    private val viewModel: SavedViewModel by viewModels()
    private lateinit var pokemonSavedListAdapter: PokemonSavedListAdapter

    private var count = 0 // used to keep track of saved pokemon
    private var savedList = mutableListOf<CustomPokemonListItem>() // used to keep track of saved pokemon

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavedBinding.bind(view)

        //setup backbutton
        binding.savedFragmentBack.setOnClickListener {
            findNavController().popBackStack()
        }


        lifecycleScope.launchWhenStarted {
            setupRv()
            initObservers()
            viewModel.getPokemonSavedPokemon()
        }
    }

    private fun setupRv() {
        pokemonSavedListAdapter = PokemonSavedListAdapter()

        // setup on click listener for RecyclerView Items

        pokemonSavedListAdapter.setOnClickListener(object : PokemonSavedListAdapter.OnClickListener {
            override fun onClick(item: CustomPokemonListItem) {
                // create bundle to pass to next fragment
                val bundle = Bundle()
                bundle.putParcelable("pokemon", item)
                findNavController().navigate(R.id.action_savedViewFragment_to_detailFragment, bundle)
            }

        })

        // setup on delete listener for RecyclerView Items

        pokemonSavedListAdapter.setOnDeleteListener(object : PokemonSavedListAdapter.OnDeleteListener {

            override fun onDelete(item: CustomPokemonListItem, pos: Int) {
                deletePokemon(item, pos)
            }

        })

        binding.savedFragmentRv.apply {
            adapter = pokemonSavedListAdapter
        }


    }

    // setup observers from viewmodel

    private fun initObservers() {
        viewModel.savedPokemon.observe(viewLifecycleOwner, Observer { savedPokemon ->
            when (savedPokemon) {
                is Resource.Success -> {
                    savedPokemon.data?.let {
                        if (savedPokemon.data.isNotEmpty()) {

                            count = savedPokemon.data.size
                            savedList = savedPokemon.data as MutableList<CustomPokemonListItem>
                            pokemonSavedListAdapter.setList(savedPokemon.data)
                            binding.savedFragmentRv.invalidate()
                            pokemonSavedListAdapter.notifyDataSetChanged()


                        } else {
                            binding.savedFragmentPlaceholder.isVisible = true
                        }
                    }
                }
                is Resource.Error -> {
                    Log.d(TAG, savedPokemon.message.toString())
                    binding.savedFragmentPlaceholder.isVisible = true
                }
                is Resource.Loading -> {
                    Log.d(TAG, "LOADING")
                }

                is Resource.Expired -> TODO()
            }

        })
    }

    // function to delete pokemon after bin png is clicked in Recyclerview

    private fun deletePokemon(customPokemonListItem: CustomPokemonListItem, pos: Int) {
        // Step 1: Generate a random number
        val randomNumber = (2..100).random()
        val isPrime = isPrime(randomNumber)

        // Check if the random number is prime before showing the dialog
        if (!isPrime) {
            Toast.makeText(context, "The number you got $randomNumber is not prime. Failed to release Pokemon. Try again.", Toast.LENGTH_LONG).show()
            return
        }

        // Proceed with the original delete dialog if the number is prime
        val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme) // using custom theme
        builder.setMessage("The number you got $randomNumber is prime. Are you sure you want to release this Pokemon? ")
            .setCancelable(true)
            .setPositiveButton("Yes") { dialog, id ->
                customPokemonListItem.isSaved = "false"
                pokemonSavedListAdapter.removeItemAtPosition(pos)
                pokemonSavedListAdapter.notifyDataSetChanged() // update RV
                count -= 1 // update count
                Log.d(TAG, count.toString())
                if (count == 0) {
                    binding.savedFragmentPlaceholder.isVisible = true
                }
                viewModel.deletePokemon(customPokemonListItem)
            }
            .setNegativeButton("No") { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun isPrime(number: Int): Boolean {
        if (number <= 1) return false
        for (i in 2..number / 2) {
            if (number % i == 0) return false
        }
        return true
    }
}