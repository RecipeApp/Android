package com.example.model;

import java.io.Serializable;

public class Ingredient  implements Serializable , Comparable<Ingredient>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String IngredientName;
	private Boolean checked = false;
	private boolean basicIngredient;
	private boolean changed = false;
	private Integer image;
	
	public Integer getImage() {
		return image;
	}

	public void setImage(Integer image) {
		this.image = image;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public int compareTo(Ingredient ingredient) {
		
		return checked.compareTo(ingredient.checked);
		
	} 
	
	public String getIngredientName() {
		return IngredientName;
	}
	public void setIngredientName(String ingredientName) {
		IngredientName = ingredientName;
	}
	public Boolean isChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	
	 public boolean isBasicIngredient() {
		return basicIngredient;
	}

	public void setBasicIngredient(boolean basicIngredient) {
		this.basicIngredient = basicIngredient;
	}

	public void toggleChecked() {  
	      checked = !checked ;  
	    }


}
