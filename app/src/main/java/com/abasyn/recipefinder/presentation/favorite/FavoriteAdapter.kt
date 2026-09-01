package com.abasyn.recipefinder.presentation.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.abasyn.recipefinder.R
import com.abasyn.recipefinder.data.remote.RecipeDto
import com.abasyn.recipefinder.databinding.ItemRecipeBinding

class FavoriteAdapter(
    private val onItemClick: (RecipeDto) -> Unit,
    private val onFavoriteClick: (RecipeDto) -> Unit
) : ListAdapter<RecipeDto, FavoriteAdapter.FavoriteViewHolder>(FavoriteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding, onItemClick, onFavoriteClick)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FavoriteViewHolder(
        private val binding: ItemRecipeBinding,
        private val onItemClick: (RecipeDto) -> Unit,
        private val onFavoriteClick: (RecipeDto) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: RecipeDto) {
            binding.root.setOnClickListener {
                onItemClick(recipe)
            }
            binding.tvRecipeName.text = recipe.strMeal ?: ""
            
            // Category and Area binding
            val category = recipe.strCategory ?: "N/A"
            val area = recipe.strArea ?: "N/A"
            binding.tvCategory.text = "$category • $area"

            binding.ivRecipe.load(recipe.strMealThumb) {
                crossfade(true)
            }
            
            binding.ivFavorite.setImageResource(R.drawable.ic_favorite)
            binding.ivFavorite.setColorFilter(android.graphics.Color.RED)
            binding.ivFavorite.setOnClickListener {
                onFavoriteClick(recipe)
            }

            // Time handling: API does not provide time, showing N/A as requested
            binding.tvTime.text = "N/A"
            binding.ivTime.visibility = View.VISIBLE
        }
    }

    class FavoriteDiffCallback : DiffUtil.ItemCallback<RecipeDto>() {
        override fun areItemsTheSame(oldItem: RecipeDto, newItem: RecipeDto): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: RecipeDto, newItem: RecipeDto): Boolean {
            return oldItem == newItem
        }
    }
}
