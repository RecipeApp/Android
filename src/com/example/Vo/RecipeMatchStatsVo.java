package com.example.Vo;

import java.util.ArrayList;
import java.util.List;

import com.example.model.Ingredient;

public class RecipeMatchStatsVo implements Comparable<RecipeMatchStatsVo>{

	String Recipe_Id;
	String recipeName;
	int matchCount;
	int totalIngredientsInRecipe;
	Double matchPercentage;
	String url;
	List<Ingredient> missingIngredients = new ArrayList<Ingredient>();
	
	
	public int compareTo(RecipeMatchStatsVo recipeMatchStatsVo) {
		
		if (this.missingIngredients.size() < recipeMatchStatsVo.missingIngredients.size()) return -1;  
		if (this.missingIngredients.size() > recipeMatchStatsVo.missingIngredients.size()) return 1;
		if (this.missingIngredients.size() == recipeMatchStatsVo.missingIngredients.size()) return 0;
		
		return 0;
	}
	
	public String getRecipe_Id() {
		return Recipe_Id;
	}
	
	public void setRecipe_Id(String recipe_Id) {
		Recipe_Id = recipe_Id;
	}
	
	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public int getMatchCount() {
		return matchCount;
	}
	public void setMatchCount(int matchCount) {
		this.matchCount = matchCount;
	}
	public int getTotalIngredientsInRecipe() {
		return totalIngredientsInRecipe;
	}
	public void setTotalIngredientsInRecipe(int totalIngredientsInRecipe) {
		this.totalIngredientsInRecipe = totalIngredientsInRecipe;
	}
	public Double getMatchPercentage() {
		return matchPercentage;
	}
	public void setMatchPercentage(Double matchPercentage) {
		this.matchPercentage = matchPercentage;
	}

	public List<Ingredient> getMissingIngredients() {
		return missingIngredients;
	}

	public void setMissingIngredients(List<Ingredient> missingIngredients) {
		this.missingIngredients = missingIngredients;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	



	
}
