package com.example.helloworld;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.Adapter.RecipeArrayAdapter;
import com.example.Vo.RecipeMatchStatsVo;
import com.example.model.Ingredient;
import com.example.model.Recipe;

public class RecipeSearchResultsActivity extends Activity{

	private ListView recipeListView ;
	private ArrayAdapter<RecipeMatchStatsVo> listAdapter ;  
	private List<Recipe> allRecipes;
	private List<Ingredient> selectedIngredients = new ArrayList<Ingredient>();
	private final double CUT_OFF= 0.25;
	
	XmlResourceParser myXml;
	int eventType;
	String nodeValue;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	       setContentView(R.layout.activity_hello_world); 
	        
	       recipeListView = (ListView) findViewById( R.id.recipeListView );  
	       
	       allRecipes =  loadXml();
	       
	       Intent i = getIntent();
	           
	       HashMap<String, List<Ingredient>> ingredientList = (HashMap<String, List<Ingredient>>)i.getSerializableExtra("ingredientList");
	       
	       ArrayList<Ingredient> basicIngredientList = (ArrayList<Ingredient>)i.getSerializableExtra("basicIngredientList");
	       
	       Boolean showVideoRecipes = (Boolean)i.getSerializableExtra("showVideoRecipes");
	       
	       for(Map.Entry<String,List<Ingredient>>  entry : ingredientList.entrySet()){
	    	   List<Ingredient> ingredientlistfromMap = entry.getValue();
	    	   
	    	   for(Ingredient ingredient : ingredientlistfromMap)
	    	   {
	    	   if(ingredient.isChecked()){	    		   
	    		   selectedIngredients.add(ingredient);	    		   
	    	   }
	    	   }
	       }
	       
	       
	       for(Ingredient ingredient :basicIngredientList ){
	    	   if(ingredient.isChecked()){
	    		   ingredient.setBasicIngredient(true);
	    		   selectedIngredients.add(ingredient);	    		   
	    	   }
	       }
	       
	       List<RecipeMatchStatsVo>  recipeResults =  search(selectedIngredients, showVideoRecipes);
	       
	    	       
	       Collections.sort(recipeResults);
	       
	       listAdapter = new RecipeArrayAdapter(this, recipeResults); 
	       recipeListView.setAdapter( listAdapter );   
	       
	                
	    }
	 
	 
	public List<RecipeMatchStatsVo> search(List<Ingredient> ingredientList, Boolean showOnlyVideoRecipes) {

		System.out.println("All Recipes" + allRecipes + "All Ingredients"
				+ ingredientList);

		int basicIngredientCount = 0;
		
		List<RecipeMatchStatsVo> recipeResultList = new ArrayList<RecipeMatchStatsVo>();

		for (Recipe recipe : allRecipes) {

			RecipeMatchStatsVo recipeMatchStatsVo = new RecipeMatchStatsVo();
			recipeMatchStatsVo.setRecipe_Id(recipe.getRecipe_Id());
			recipeMatchStatsVo.setRecipeName(recipe.getRecipe_Name());
			recipeMatchStatsVo.setTotalIngredientsInRecipe(recipe.getRecipe_Ingredients().size());
			
			for (Ingredient ingredient : recipe.getRecipe_Ingredients()) {
				if (searchIngredientInList(ingredientList, ingredient)) {
					if(ingredient.isBasicIngredient()) basicIngredientCount += 1;
					else
					recipeMatchStatsVo.setMatchCount(recipeMatchStatsVo.getMatchCount() + 1);
				}else{
					recipeMatchStatsVo.getMissingIngredients().add(ingredient);
				}
			}
			
			double matchPercentage = recipeMatchStatsVo.getMatchCount()/((double)recipeMatchStatsVo.getTotalIngredientsInRecipe() - basicIngredientCount);
			recipeMatchStatsVo.setMatchPercentage(matchPercentage);
			recipeMatchStatsVo.setUrl(recipe.getURL());
			
			if(recipeMatchStatsVo.getMatchCount() > 0)
			{
				if(!(showOnlyVideoRecipes && (!recipe.getURL().contains("youtube"))))
				recipeResultList.add(recipeMatchStatsVo);
		}
			}

		return recipeResultList;
	}
		
		private boolean searchIngredientInList(List<Ingredient> ingredientsInList ,  Ingredient ingredient){
			
			for(Ingredient recipeIngredient:ingredientsInList){
				if( recipeIngredient.getIngredientName().trim().toLowerCase().contains(ingredient.getIngredientName().trim().toLowerCase())){
					if(recipeIngredient.isBasicIngredient()){
						ingredient.setBasicIngredient(true);
					}
					return true;
				}				
			}		
			return false;
		}
		
		 
	 
	 public List<Recipe> loadXml() {
			// TODO Auto-generated method stub

			allRecipes = new ArrayList<Recipe>();
		
			try {
				myXml = getBaseContext().getResources().getXml(R.xml.recipes);
				myXml.next();

				eventType = myXml.getEventType(); // Get current xml Event

			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			List<Ingredient> ingredientList = null;
			Recipe recipe = null;

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					// checks can be placed here to make sure file is read
				} else if (eventType == XmlPullParser.START_TAG) {
					nodeValue = myXml.getName();

					try {

						if (nodeValue.equalsIgnoreCase("Recipe")) { // Finds
																	// Ingredient
																	// tag

							recipe = new Recipe();
							ingredientList = new ArrayList<Ingredient>();
							myXml.next();

						}

						nodeValue = myXml.getName();

						// Since the effect tags are followed by ending tags, we can
						// just do nextText() to get tag text
						if (nodeValue.equalsIgnoreCase("Recipe-Id")) {

							myXml.next();
							recipe.setRecipe_Id(myXml.getText().trim());
						}
						if (nodeValue.equalsIgnoreCase("Recipe-Name")) {

							myXml.next();
							recipe.setRecipe_Name(myXml.getText().trim());
						}

						if (nodeValue.equalsIgnoreCase("Ingredient")) {
							myXml.next();
							Ingredient ingredient = new Ingredient();
							ingredient.setIngredientName(myXml.getText().trim());

							ingredientList.add(ingredient);
						}

						if (nodeValue.equalsIgnoreCase("Basic-Ingredient")) {

							myXml.next();
							Ingredient ingredient = new Ingredient();
							ingredient.setIngredientName(myXml.getText().trim());

							ingredientList.add(ingredient);
						}

						if (nodeValue.equalsIgnoreCase("URL")) {

							myXml.next();
							recipe.setURL((myXml.getText().trim()));
							
							if(recipe.getURL().contains("youtube")){
								recipe.setIsVideoRecipe(true);
							}
							
							recipe.setRecipe_Ingredients(ingredientList);
							allRecipes.add(recipe);
							
						}

					} catch (Exception e) {
						e.getMessage();
						e.printStackTrace();
					}

				}
				try {
					eventType = myXml.next();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return allRecipes;

		}
	
}
