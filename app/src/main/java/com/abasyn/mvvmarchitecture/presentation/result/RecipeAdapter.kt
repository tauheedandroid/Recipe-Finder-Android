package com.abasyn.mvvmarchitecture.presentation.result

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.abasyn.mvvmarchitecture.R
import com.abasyn.mvvmarchitecture.data.remote.RecipeDto
import com.abasyn.mvvmarchitecture.databinding.ItemRecipeBinding

class RecipeAdapter(
    private val onItemClick: (RecipeDto) -> Unit,
    private val onFavoriteClick: (RecipeDto) -> Unit
) : ListAdapter<RecipeDto, RecipeAdapter.RecipeViewHolder>(RecipeDiffCallback()) {

    private var favoriteIds: Set<String> = emptySet()

    fun setFavorites(ids: Set<String>) {
        favoriteIds = ids
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding, onItemClick, onFavoriteClick)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe, favoriteIds.contains(recipe.idMeal))
    }

    class RecipeViewHolder(
        private val binding: ItemRecipeBinding,
        private val onItemClick: (RecipeDto) -> Unit,
        private val onFavoriteClick: (RecipeDto) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: RecipeDto, isFavorite: Boolean) {
            binding.root.setOnClickListener {
                onItemClick(recipe)
            }
            binding.tvRecipeName.text = recipe.strMeal ?: ""
            binding.ivRecipe.load(recipe.strMealThumb) {
                crossfade(true)
            }
            
            binding.ivFavorite.setImageResource(
                if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
            binding.ivFavorite.setColorFilter(
                if (isFavorite) android.graphics.Color.RED else android.graphics.Color.parseColor("#777777")
            )
            binding.ivFavorite.setOnClickListener {
                onFavoriteClick(recipe)
            }

            // Category and Area binding
            val category = recipe.strCategory ?: "N/A"
            val area = recipe.strArea ?: "N/A"
            binding.tvCategory.text = "$category • $area"

            // Time handling: API does not provide time, showing N/A as requested
            binding.tvTime.text = "N/A"
            binding.ivTime.visibility = View.VISIBLE
        }
    }

    class RecipeDiffCallback : DiffUtil.ItemCallback<RecipeDto>() {
        override fun areItemsTheSame(oldItem: RecipeDto, newItem: RecipeDto): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: RecipeDto, newItem: RecipeDto): Boolean {
            return oldItem == newItem
        }
    }
}
