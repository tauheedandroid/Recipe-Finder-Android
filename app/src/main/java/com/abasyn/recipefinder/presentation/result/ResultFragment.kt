package com.abasyn.recipefinder.presentation.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.abasyn.recipefinder.databinding.FragmentResultBinding
import com.abasyn.recipefinder.presentation.home.HomeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModel()
    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter(
            onItemClick = { recipe ->
                val action = ResultFragmentDirections.actionResultFragmentToPreviewFragment(
                    recipeId = recipe.idMeal ?: "",
                    recipeName = recipe.strMeal,
                    recipeImage = recipe.strMealThumb
                )
                findNavController().navigate(action)
            },
            onFavoriteClick = { recipe ->
                viewModel.toggleFavoriteFromFilter(recipe)
            }
        )
        binding.rvRecipes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecipes.adapter = recipeAdapter
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.searchResults.collect { recipes ->
                        recipeAdapter.submitList(recipes)
                        binding.tvResultCount.text = "${recipes.size} recipes found"
                        binding.tvNoRecipes.isVisible = recipes.isEmpty()
                    }
                }
                launch {
                    viewModel.favoriteIds.collect { ids ->
                        recipeAdapter.setFavorites(ids)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
