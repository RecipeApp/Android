package com.example.RecipeService;

import java.util.List;

import com.example.RecipeDAO.IGetAllRecipes;
import com.example.Vo.RecipeMatchStatsVo;
import com.example.model.Recipe;

public class RecipeSearch implements IRecipeSearch {

	private List<Recipe> allRecipes;
	private List<RecipeMatchStatsVo> recipeSearchResultsList;
	
	public RecipeSearch(IGetAllRecipes getAllRecipes){
	allRecipes = getAllRecipes. getAllRecipe();
	}

	
	public List<Recipe> search(List IngredientList) {
	
		System.out.println("Reached Safely");
		
		return null;
	}
	
	
	

}
