package com.abasyn.mvvmarchitecture.data.remote

import java.io.Serializable

data class IngredientResponse(
    val meals: List<IngredientDto>?
)

data class IngredientDto(
    val idIngredient: String?,
    val strIngredient: String?,
    val strDescription: String?,
    val strType: String?
) : Serializable

data class RecipeResponse(
    val meals: List<RecipeDto>?
)

data class RecipeDto(
    val idMeal: String?,
    val strMeal: String?,
    val strMealThumb: String?,
    val strCategory: String?,
    val strArea: String?,
    val strInstructions: String?,
    val strTags: String?,
    val strYoutube: String?,
    val strIngredient1: String? = null,
    val strIngredient2: String? = null,
    val strIngredient3: String? = null,
    val strIngredient4: String? = null,
    val strIngredient5: String? = null,
    val strIngredient6: String? = null,
    val strIngredient7: String? = null,
    val strIngredient8: String? = null,
    val strIngredient9: String? = null,
    val strIngredient10: String? = null,
    val strIngredient11: String? = null,
    val strIngredient12: String? = null,
    val strIngredient13: String? = null,
    val strIngredient14: String? = null,
    val strIngredient15: String? = null,
    val strIngredient16: String? = null,
    val strIngredient17: String? = null,
    val strIngredient18: String? = null,
    val strIngredient19: String? = null,
    val strIngredient20: String? = null,
    val strMeasure1: String? = null,
    val strMeasure2: String? = null,
    val strMeasure3: String? = null,
    val strMeasure4: String? = null,
    val strMeasure5: String? = null,
    val strMeasure6: String? = null,
    val strMeasure7: String? = null,
    val strMeasure8: String? = null,
    val strMeasure9: String? = null,
    val strMeasure10: String? = null,
    val strMeasure11: String? = null,
    val strMeasure12: String? = null,
    val strMeasure13: String? = null,
    val strMeasure14: String? = null,
    val strMeasure15: String? = null,
    val strMeasure16: String? = null,
    val strMeasure17: String? = null,
    val strMeasure18: String? = null,
    val strMeasure19: String? = null,
    val strMeasure20: String? = null
) : Serializable {
    fun getIngredients(): List<String> {
        val ingredients = mutableListOf<String>()
        val fields = this::class.java.declaredFields
        for (i in 1..20) {
            val ingredient = fields.find { it.name == "strIngredient$i" }?.let {
                it.isAccessible = true
                it.get(this) as? String
            }
            val measure = fields.find { it.name == "strMeasure$i" }?.let {
                it.isAccessible = true
                it.get(this) as? String
            }
            if (!ingredient.isNullOrBlank()) {
                if (!measure.isNullOrBlank()) {
                    ingredients.add("$measure $ingredient")
                } else {
                    ingredients.add(ingredient)
                }
            }
        }
        return ingredients
    }

    // Manual helper since reflection is slow and might not work well with obfuscation
    fun getIngredientsList(): List<String> {
        val list = mutableListOf<String>()
        if (!strIngredient1.isNullOrBlank()) list.add("${strMeasure1 ?: ""} $strIngredient1".trim())
        if (!strIngredient2.isNullOrBlank()) list.add("${strMeasure2 ?: ""} $strIngredient2".trim())
        if (!strIngredient3.isNullOrBlank()) list.add("${strMeasure3 ?: ""} $strIngredient3".trim())
        if (!strIngredient4.isNullOrBlank()) list.add("${strMeasure4 ?: ""} $strIngredient4".trim())
        if (!strIngredient5.isNullOrBlank()) list.add("${strMeasure5 ?: ""} $strIngredient5".trim())
        if (!strIngredient6.isNullOrBlank()) list.add("${strMeasure6 ?: ""} $strIngredient6".trim())
        if (!strIngredient7.isNullOrBlank()) list.add("${strMeasure7 ?: ""} $strIngredient7".trim())
        if (!strIngredient8.isNullOrBlank()) list.add("${strMeasure8 ?: ""} $strIngredient8".trim())
        if (!strIngredient9.isNullOrBlank()) list.add("${strMeasure9 ?: ""} $strIngredient9".trim())
        if (!strIngredient10.isNullOrBlank()) list.add("${strMeasure10 ?: ""} $strIngredient10".trim())
        if (!strIngredient11.isNullOrBlank()) list.add("${strMeasure11 ?: ""} $strIngredient11".trim())
        if (!strIngredient12.isNullOrBlank()) list.add("${strMeasure12 ?: ""} $strIngredient12".trim())
        if (!strIngredient13.isNullOrBlank()) list.add("${strMeasure13 ?: ""} $strIngredient13".trim())
        if (!strIngredient14.isNullOrBlank()) list.add("${strMeasure14 ?: ""} $strIngredient14".trim())
        if (!strIngredient15.isNullOrBlank()) list.add("${strMeasure15 ?: ""} $strIngredient15".trim())
        if (!strIngredient16.isNullOrBlank()) list.add("${strMeasure16 ?: ""} $strIngredient16".trim())
        if (!strIngredient17.isNullOrBlank()) list.add("${strMeasure17 ?: ""} $strIngredient17".trim())
        if (!strIngredient18.isNullOrBlank()) list.add("${strMeasure18 ?: ""} $strIngredient18".trim())
        if (!strIngredient19.isNullOrBlank()) list.add("${strMeasure19 ?: ""} $strIngredient19".trim())
        if (!strIngredient20.isNullOrBlank()) list.add("${strMeasure20 ?: ""} $strIngredient20".trim())
        return list
    }
}

data class RecipeFilterResponse(
    val meals: List<RecipeFilterDto>?
)

data class RecipeFilterDto(
    val idMeal: String?,
    val strMeal: String?,
    val strMealThumb: String?
) : Serializable
