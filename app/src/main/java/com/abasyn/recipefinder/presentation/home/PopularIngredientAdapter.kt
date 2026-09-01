package com.abasyn.recipefinder.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.abasyn.recipefinder.data.remote.IngredientDto
import com.abasyn.recipefinder.databinding.ItemPopularIngredientBinding
import com.abasyn.recipefinder.utils.Constants

class PopularIngredientAdapter : ListAdapter<IngredientDto, PopularIngredientAdapter.IngredientViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder(
            ItemPopularIngredientBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class IngredientViewHolder(
        private val binding: ItemPopularIngredientBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredient: IngredientDto) {
            binding.tvIngredientName.text = ingredient.strIngredient
            
            val imageUrl = "${Constants.INGREDIENT_IMAGE_BASE_URL}${ingredient.strIngredient}.png"
            
            binding.ivIngredient.load(imageUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<IngredientDto>() {
        override fun areItemsTheSame(oldItem: IngredientDto, newItem: IngredientDto): Boolean {
            return oldItem.idIngredient == newItem.idIngredient
        }

        override fun areContentsTheSame(oldItem: IngredientDto, newItem: IngredientDto): Boolean {
            return oldItem == newItem
        }
    }
}