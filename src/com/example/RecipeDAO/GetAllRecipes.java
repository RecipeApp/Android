package com.example.RecipeDAO;

import java.util.List;

import com.example.Service.LoadRecipes;
import com.example.model.Recipe;

public class GetAllRecipes implements IGetAllRecipes {

	private static List<Recipe> allRecipes;
	
	public List<Recipe> getAllRecipe() {
		
		return null;
	}

	public static List<Recipe> getAllRecipes() {
		LoadRecipes l = new LoadRecipes();
		return l.loadXml();
		
	}

	
	
}
