package com.abasyn.mvvmarchitecture.presentation.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.abasyn.mvvmarchitecture.R
import com.abasyn.mvvmarchitecture.data.remote.RecipeDto
import com.abasyn.mvvmarchitecture.databinding.FragmentPreviewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PreviewFragment : Fragment() {

    private var _binding: FragmentPreviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PreviewViewModel by viewModel()
    private val args: PreviewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        observeViewModel()

        // Load basic info from args if available
        args.recipeName?.let { binding.tvRecipeName.text = it }
        args.recipeImage?.let { binding.ivRecipe.load(it) }

        // Fetch full details
        viewModel.getRecipeDetails(args.recipeId)

        binding.btnFavorite.setOnClickListener {
            viewModel.toggleFavorite()
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeViewModel() {
        viewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            recipe?.let { displayRecipe(it) }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            binding.btnFavorite.setImageResource(
                if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
            binding.btnFavorite.imageTintList = android.content.res.ColorStateList.valueOf(
                if (isFavorite) android.graphics.Color.WHITE else android.graphics.Color.WHITE
            )
            // FloatingActionButton usually has its own tinting, but let's ensure it looks right.
            // If it's ❤️ we might want a different background tint too?
            // The user's XML had app:backgroundTint="@color/orange"
        }
    }

    private fun displayRecipe(recipe: RecipeDto) {
        binding.apply {
            tvRecipeName.text = recipe.strMeal
            ivRecipe.load(recipe.strMealThumb) {
                crossfade(true)
            }
            tvCategory.text = "${recipe.strCategory} | ${recipe.strArea}"
            tvInstructions.text = recipe.strInstructions
            
            val ingredients = recipe.getIngredientsList()
            tvIngredients.text = ingredients.joinToString("\n") { "• $it" }

            // These fields might not be in the API, using placeholders or hiding if null
            tvTime.text = "30 min" 
            tvCalories.text = "250 kcal"
            tvLevel.text = "Easy"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
