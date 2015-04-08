package com.example.Adapter;

import java.util.List;

import com.example.Vo.RecipeMatchStatsVo;
import com.example.helloworld.R;
import com.example.model.Ingredient;
import com.example.model.Recipe;
import com.example.viewHolder.IngredientViewHolder;
import com.example.viewHolder.RecipeViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class RecipeArrayAdapter extends ArrayAdapter<RecipeMatchStatsVo>{

	 private LayoutInflater inflater; 
	
	public RecipeArrayAdapter(Context context,
			List<RecipeMatchStatsVo> objects) {
		super(context, R.layout.reciperow, R.id.rowTextView, objects);
	     inflater = LayoutInflater.from(context) ;  
	}
	
	
	 @Override  
	    public View getView(int position, View convertView, ViewGroup parent) {  
	      // Ingredients to display  
		 RecipeMatchStatsVo recipeMatchStatsVo = (RecipeMatchStatsVo) this.getItem( position );   
	  
	      // The child views in each row.  
	      TextView recipeNameView , textView ;   
	        
	      // Create a new row view  
	      if ( convertView == null ) {  
	        convertView = inflater.inflate(R.layout.reciperow, null);  
	          
	        // Find the child views.  
	       textView = (TextView) convertView.findViewById( R.id.rowTextView );  
	        
	        recipeNameView = (TextView) convertView.findViewById( R.id.recipeNameTextView);
	        
	        convertView.setTag( new RecipeViewHolder(recipeNameView,textView) );  
	        // Optimization: Tag the row with it's child views, so we don't have to   
	        // call findViewById() later when we reuse the row.  
	              
	      }  
	      // Reuse existing row view  
	      else {  
	        // Because we use a ViewHolder, we avoid having to call findViewById().  
	    	  RecipeViewHolder viewHolder = (RecipeViewHolder) convertView.getTag();  
	          textView = viewHolder.getTextView() ; 
	          recipeNameView = viewHolder.getTextViewName() ; 
	      }  
	  
	      // Tag the CheckBox with the Planet it is displaying, so that we can  
	      // access the planet in onClick() when the CheckBox is toggled.  
	      StringBuffer text = new StringBuffer();
	      
	      recipeNameView.setText(Html.fromHtml("<a href=\"" +recipeMatchStatsVo.getUrl() + "\">"+ recipeMatchStatsVo.getRecipeName() + "</a>"));
	     
	     // recipeNameView.setTextColor(Color.WHITE);
	      recipeNameView.setTypeface(null, Typeface.BOLD);
	       recipeNameView.setTextSize(20);
	   //   recipeNameView.setBackgroundColor(Color.BLACK);
	      
	     
	    recipeNameView.setMovementMethod(LinkMovementMethod.getInstance());
	      
	      // Display planet data  

	      int i = 0;
	      for(Ingredient ingredient : recipeMatchStatsVo.getMissingIngredients())
	   {
	    	  if(i!=0)text.append(", ");  	  
	    	  text.append(ingredient.getIngredientName());
	    	  i++;
	 }
	      textView.setText( text.toString());
	      textView.setTextColor(Color.BLACK);
	      textView.setBackgroundColor(Color.WHITE);
	     
	    	  
	      return convertView;  
	
	    }   
	    
	

}
