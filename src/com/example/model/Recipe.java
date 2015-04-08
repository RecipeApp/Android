package com.example.model;

import java.util.List;

public class Recipe {

	String Recipe_Id;
	String Recipe_Name;
	List<Ingredient> Recipe_Ingredients;  // Or  Binary Search Tree for faster search
	String URL;
	Boolean videoRecipe;
	
	
	public Boolean isVideoRecipe() {
		return videoRecipe;
	}
	public void setIsVideoRecipe(Boolean videoRecipe) {
		this.videoRecipe = videoRecipe;
	}
	public String getRecipe_Id() {
		return Recipe_Id;
	}
	public void setRecipe_Id(String recipe_Id) {
		Recipe_Id = recipe_Id;
	}
	public String getRecipe_Name() {
		return Recipe_Name;
	}
	public void setRecipe_Name(String recipe_Name) {
		Recipe_Name = recipe_Name;
	}
	
	public List<Ingredient> getRecipe_Ingredients() {
		return Recipe_Ingredients;
	}
	public void setRecipe_Ingredients(List<Ingredient> recipe_Ingredients) {
		Recipe_Ingredients = recipe_Ingredients;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}

	
	
	
}
