package com.abasyn.mvvmarchitecture.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.abasyn.mvvmarchitecture.R
import com.abasyn.mvvmarchitecture.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModel()
    private lateinit var popularIngredientAdapter: PopularIngredientAdapter
    private lateinit var recentRecipeAdapter: RecentRecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnFindRecipes.setOnClickListener {
            val query = binding.etIngredients.text.toString()
            if (query.isBlank()) {
                binding.etIngredients.error = "Please enter at least one ingredient"
            } else {
                viewModel.searchRecipes(query)
            }
        }
    }

    private fun setupRecyclerView() {
        popularIngredientAdapter = PopularIngredientAdapter()
        binding.rvPopularIngredients.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = popularIngredientAdapter
        }

        recentRecipeAdapter = RecentRecipeAdapter(
            onItemClick = { recipe ->
                val action = HomeFragmentDirections.actionHomeFragmentToPreviewFragment(
                    recipeId = recipe.idMeal ?: "",
                    recipeName = recipe.strMeal,
                    recipeImage = recipe.strMealThumb
                )
                findNavController().navigate(action)
            },
            onFavoriteClick = { recipe ->
                viewModel.toggleFavorite(recipe)
            }
        )
        binding.rvRecentRecipes.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = recentRecipeAdapter
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.ingredientsState.collect { state ->
                        handleIngredientsState(state)
                    }
                }
                launch {
                    viewModel.recentRecipesState.collect { state ->
                        handleRecentRecipesState(state)
                    }
                }
                launch {
                    viewModel.searchState.collect { state ->
                        handleSearchState(state)
                    }
                }
                launch {
                    viewModel.favoriteIds.collect { ids ->
                        recentRecipeAdapter.setFavorites(ids)
                    }
                }
            }
        }
    }

    private fun handleSearchState(state: HomeViewModel.RecipeSearchState) {
        when (state) {
            is HomeViewModel.RecipeSearchState.Loading -> {
                binding.btnFindRecipes.isEnabled = false
                binding.progressPopularIngredients.isVisible = true // Reusing progress for global search
            }
            is HomeViewModel.RecipeSearchState.Success -> {
                binding.btnFindRecipes.isEnabled = true
                binding.progressPopularIngredients.isVisible = false
                findNavController().navigate(R.id.action_homeFragment_to_resultFragment)
                viewModel.resetSearchState()
            }
            is HomeViewModel.RecipeSearchState.Error -> {
                binding.btnFindRecipes.isEnabled = true
                binding.progressPopularIngredients.isVisible = false
                binding.etIngredients.error = state.message
            }
            is HomeViewModel.RecipeSearchState.Empty -> {
                binding.btnFindRecipes.isEnabled = true
                binding.progressPopularIngredients.isVisible = false
                Toast.makeText(requireContext(), "No recipes found for these ingredients.", Toast.LENGTH_SHORT).show()
            }
            is HomeViewModel.RecipeSearchState.Idle -> {
                binding.btnFindRecipes.isEnabled = true
                binding.progressPopularIngredients.isVisible = false
            }
        }
    }

    private fun handleIngredientsState(state: HomeViewModel.IngredientsUiState) {
        when (state) {
            is HomeViewModel.IngredientsUiState.Loading -> {
                binding.progressPopularIngredients.isVisible = true
                binding.tvNoIngredients.isVisible = false
            }
            is HomeViewModel.IngredientsUiState.Success -> {
                binding.progressPopularIngredients.isVisible = false
                binding.tvNoIngredients.isVisible = false
                popularIngredientAdapter.submitList(state.data)
            }
            is HomeViewModel.IngredientsUiState.Error -> {
                binding.progressPopularIngredients.isVisible = false
                binding.tvNoIngredients.isVisible = false
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
            }
            is HomeViewModel.IngredientsUiState.Empty -> {
                binding.progressPopularIngredients.isVisible = false
                binding.tvNoIngredients.isVisible = true
                popularIngredientAdapter.submitList(emptyList())
            }
        }
    }

    private fun handleRecentRecipesState(state: HomeViewModel.RecentRecipesUiState) {
        when (state) {
            is HomeViewModel.RecentRecipesUiState.Loading -> {
                binding.progressRecentRecipes.isVisible = true
                binding.tvNoRecentRecipes.isVisible = false
            }
            is HomeViewModel.RecentRecipesUiState.Success -> {
                binding.progressRecentRecipes.isVisible = false
                binding.tvNoRecentRecipes.isVisible = false
                recentRecipeAdapter.submitList(state.data)
            }
            is HomeViewModel.RecentRecipesUiState.Error -> {
                binding.progressRecentRecipes.isVisible = false
                binding.tvNoRecentRecipes.isVisible = false
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
            }
            is HomeViewModel.RecentRecipesUiState.Empty -> {
                binding.progressRecentRecipes.isVisible = false
                binding.tvNoRecentRecipes.isVisible = true
                recentRecipeAdapter.submitList(emptyList())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
