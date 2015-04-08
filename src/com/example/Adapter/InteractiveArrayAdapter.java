package com.example.Adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.helloworld.R;
import com.example.model.Ingredient;
import com.example.viewHolder.IngredientViewHolder;

public class InteractiveArrayAdapter extends ArrayAdapter<Ingredient> {

	 private LayoutInflater inflater;  
	 
	 private ArrayList<Ingredient> arraylist;
	 
	 private List<Ingredient> ingredientList = null;
     
	    public InteractiveArrayAdapter( Context context, List<Ingredient> ingredientList ) {  
	      super( context, R.layout.simplerow, R.id.rowTextView, ingredientList );  
	      // Cache the LayoutInflate to avoid asking for a new one each time.  
	      inflater = LayoutInflater.from(context) ;  
	      this.ingredientList = ingredientList;
	      
	      this.arraylist = new ArrayList<Ingredient>();
	      this.arraylist.addAll(ingredientList);
	      
	    }  
	  
	    @Override  
	    public View getView(int position, View convertView, ViewGroup parent) {  
	      // Ingredients to display  
	    	Ingredient ingredient = (Ingredient) this.getItem( position );   
	  
	      // The child views in each row.  
	      CheckBox checkBox ;   
	      TextView textView ;   
	        
	      // Create a new row view  
	      if ( convertView == null ) {  
	        convertView = inflater.inflate(R.layout.simplerow, null);  
	          
	        // Find the child views.  
	        textView = (TextView) convertView.findViewById( R.id.rowTextView );  
	        checkBox = (CheckBox) convertView.findViewById( R.id.CheckBox01 );  
	          
	        // Optimization: Tag the row with it's child views, so we don't have to   
	        // call findViewById() later when we reuse the row.  
	       // convertView.setTag( new IngredientViewHolder(textView,checkBox) );  
	  
	        // If CheckBox is toggled, update the planet it is tagged with.  
	        checkBox.setOnClickListener( new View.OnClickListener() {  
	          public void onClick(View v) {  
	            CheckBox cb = (CheckBox) v ;  
	            Ingredient ingredient = (Ingredient) cb.getTag();  
	            ingredient.setChecked( cb.isChecked() ); 
	            
	            filter("");
	            
	            arrangeIngredientList();
	          }  
	        });          
	      }  
	      // Reuse existing row view  
	      else {  
	        // Because we use a ViewHolder, we avoid having to call findViewById().  
	    	  IngredientViewHolder viewHolder = (IngredientViewHolder) convertView.getTag();  
	        checkBox = viewHolder.getCheckBox() ;  
	        textView = viewHolder.getTextView() ;  
	      }  
	  
	      // Tag the CheckBox with the Planet it is displaying, so that we can  
	      // access the planet in onClick() when the CheckBox is toggled.  
	      checkBox.setTag( ingredient );   
	        
	      // Display planet data  
	      checkBox.setChecked( ingredient.isChecked() );  
	      checkBox.setTextColor(Color.WHITE);
	      checkBox.setBackgroundColor(Color.WHITE);
	      
	  //    textView.setTextColor(Color.WHITE);
	      textView.setBackgroundColor(Color.WHITE);
	      textView.setWidth(600);
	      textView.setText( ingredient.getIngredientName() );        
	        
	      return convertView;  
	    }   
	    
	    
	 // Filter Class
		public void filter(String charText) {
			charText = charText.toLowerCase(Locale.getDefault());
			ingredientList.clear();
			if (charText.length() == 0) {
				ingredientList.addAll(arraylist);
			} else {
				for (Ingredient wp : arraylist) {
					if (wp.getIngredientName().toLowerCase(Locale.getDefault())
							.contains(charText)) {
						ingredientList.add(wp);
					}
				}
			}
			notifyDataSetChanged();
					
		}
		
		
		
		public void arrangeIngredientList(){
	
			Collections.sort(ingredientList,Collections.reverseOrder());
			notifyDataSetChanged();
			
		}
		
	    
	}  