package com.example.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Service;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.IBinder;

import com.example.helloworld.R;
import com.example.model.Ingredient;
import com.example.model.Recipe;

public class LoadRecipes {

	XmlResourceParser myXml;
	int eventType;
	String nodeValue;
	private List<Recipe> allRecipes;

	public IBinder onBind(Intent arg0) {
		return null;
	}

	public List<Recipe> loadXml() {
		// TODO Auto-generated method stub

		allRecipes = new ArrayList<Recipe>();

		System.out.println("Inside LoadXml");
	
		try {
			//myXml = getBaseContext().getResources().getXml(R.xml.recipes);
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
			System.out.println("Inside While Loop Step 1");
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

		for (int i = 0; allRecipes.size() > i; i++) {
			System.out.println(allRecipes.get(i).getRecipe_Name());

		}
		return allRecipes;

	}

}
