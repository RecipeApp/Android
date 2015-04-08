package com.example.RecipeService;

import java.util.List;

import com.example.model.Recipe;

public interface IRecipeSearch {

	public List<Recipe> search(List IngredientList); 
	
}
