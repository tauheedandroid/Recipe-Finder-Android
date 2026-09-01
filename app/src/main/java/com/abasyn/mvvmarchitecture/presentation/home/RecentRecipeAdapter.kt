package com.abasyn.mvvmarchitecture.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.abasyn.mvvmarchitecture.R
import com.abasyn.mvvmarchitecture.data.remote.RecipeDto
import com.abasyn.mvvmarchitecture.databinding.ItemRecentRecipeBinding

class RecentRecipeAdapter(
    private val onItemClick: (RecipeDto) -> Unit,
    private val onFavoriteClick: (RecipeDto) -> Unit
) : ListAdapter<RecipeDto, RecentRecipeAdapter.ViewHolder>(DiffCallback) {

    private var favoriteIds: Set<String> = emptySet()

    fun setFavorites(ids: Set<String>) {
        favoriteIds = ids
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRecentRecipeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick,
            onFavoriteClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe, favoriteIds.contains(recipe.idMeal))
    }

    class ViewHolder(
        private val binding: ItemRecentRecipeBinding,
        private val onItemClick: (RecipeDto) -> Unit,
        private val onFavoriteClick: (RecipeDto) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: RecipeDto, isFavorite: Boolean) {
            binding.apply {
                root.setOnClickListener {
                    onItemClick(recipe)
                }
                tvRecipeName.text = recipe.strMeal
                tvCategory.text = recipe.strCategory
                tvArea.text = if (recipe.strArea != null) " • ${recipe.strArea}" else ""
                ivRecipe.load(recipe.strMealThumb) {
                    crossfade(true)
                }
                
                ivFavorite.setImageResource(
                    if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                )
                ivFavorite.setColorFilter(
                    if (isFavorite) android.graphics.Color.RED else android.graphics.Color.parseColor("#777777")
                )
                ivFavorite.setOnClickListener {
                    onFavoriteClick(recipe)
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<RecipeDto>() {
        override fun areItemsTheSame(oldItem: RecipeDto, newItem: RecipeDto): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: RecipeDto, newItem: RecipeDto): Boolean {
            return oldItem == newItem
        }
    }
}
