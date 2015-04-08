package com.example.Adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.helloworld.R;
import com.example.model.Ingredient;
import com.example.viewHolder.IngredientViewHolder;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<Ingredient>> _listDataChild;
	
	private List<String> ingredientCategoryHeader;
	
	private HashMap<String, List<Ingredient>> ingredientMap = null;


	public ExpandableListAdapter(Context context, List<String> listDataHeader,
			HashMap<String, List<Ingredient>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
		
		ingredientMap = new HashMap<String, List<Ingredient>>();
		
		ingredientMap.putAll(_listDataChild);
		
		ingredientCategoryHeader = new ArrayList<String>();
		
		ingredientCategoryHeader.addAll(_listDataHeader);
		
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
		
	//	final String childText = (String) getChild(groupPosition, childPosition);
		    
        
    	Ingredient ingredient = (Ingredient) getChild(groupPosition, childPosition);
  	  
	      // The child views in each row.  
	      CheckBox checkBox ;   
	      TextView textView ;   
	      ImageView imageView ;
	        
	      // Create a new row view  
	      if ( convertView == null ) {  
	    	  LayoutInflater infalInflater = (LayoutInflater) this._context
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.simplerow, null);
	          
	        // Find the child views.  
	        textView = (TextView) convertView.findViewById( R.id.rowTextView );  
	        checkBox = (CheckBox) convertView.findViewById( R.id.CheckBox01 );  
	        imageView =(ImageView)convertView.findViewById( R.id.img ); 
	          
	        // Optimization: Tag the row with it's child views, so we don't have to   
	        // call findViewById() later when we reuse the row.  
	        convertView.setTag( new IngredientViewHolder(textView,checkBox , imageView) );  
	  
	        // If CheckBox is toggled, update the planet it is tagged with.  
	        checkBox.setOnClickListener( new View.OnClickListener() {  
	          public void onClick(View v) {  
	            CheckBox cb = (CheckBox) v ;  
	            Ingredient ingredient = (Ingredient) cb.getTag();  
	            ingredient.setChecked( cb.isChecked() ); 
	            
	           /* filter("");
	            
	            arrangeIngredientList();*/
	          }  
	        });          
	      }  
	      // Reuse existing row view  
	      else {  
	        // Because we use a ViewHolder, we avoid having to call findViewById().  
	    	  IngredientViewHolder viewHolder = (IngredientViewHolder) convertView.getTag();  
	        checkBox = viewHolder.getCheckBox() ;  
	        textView = viewHolder.getTextView() ;  
	        imageView = viewHolder.getImageView();
	      }  
	  
	      // Tag the CheckBox with the Planet it is displaying, so that we can  
	      // access the planet in onClick() when the CheckBox is toggled.  
	      checkBox.setTag( ingredient );   
	        
	      // Display planet data  
	      checkBox.setChecked( ingredient.isChecked() );  
	      checkBox.setTextColor(Color.BLACK);
	      checkBox.setBackgroundColor(Color.WHITE);
	     checkBox.setTextSize(20);
	      
	      textView.setTextColor(Color.BLACK);
	    //  textView.setBackgroundColor(Color.WHITE);
	      textView.setWidth(600);
	      textView.setText( ingredient.getIngredientName() );    
	      
	      imageView.setImageResource(ingredient.getImage());
	        
	      return convertView;  
        
        
	
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
		 String headerTitle = (String) getGroup(groupPosition);
	        if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater) this._context
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.list_group, null);
	        }
	 
	        TextView lblListHeader = (TextView) convertView
	                .findViewById(R.id.lblListHeader);
	        lblListHeader.setTypeface(null, Typeface.BOLD);
	        lblListHeader.setText(headerTitle);
	    //    lblListHeader.getLinkTextColors().getDefaultColor();
	        lblListHeader.setBackgroundColor(Color.TRANSPARENT);
	        
	        return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	
	public void arrangeIngredientList(){

		//Collections.sort(_listDataChild,Collections.reverseOrder());
		notifyDataSetChanged();
		
	}
	
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		_listDataChild.clear();
		_listDataHeader.clear();
		if (charText.length() == 0) {
			_listDataChild.putAll(ingredientMap);
			_listDataHeader.addAll(ingredientCategoryHeader);
		} else {
			
			  for(Map.Entry<String,List<Ingredient>>  entry : ingredientMap.entrySet()){
		    	   List<Ingredient> ingredientlistfromMap = entry.getValue();
		    	   
		    	   String mapKey = entry.getKey();
		    	   
		    	   List<Ingredient> listOfIngredients = null;
		    	   
		    	   for(Ingredient ingredient : ingredientlistfromMap)
		    	   {
		    		   
		    		   if(listOfIngredients == null) listOfIngredients = new ArrayList<Ingredient>();
		    		   
		    		   if (ingredient.getIngredientName().toLowerCase(Locale.getDefault())
								.contains(charText)) {
		    			   listOfIngredients.add(ingredient);
						}
		    	   }
		    	   
		    	   if(listOfIngredients.size() > 0){
		    	   _listDataChild.put(mapKey, listOfIngredients);
		    	   _listDataHeader.add(mapKey);
		    	   }
		    	   
		       }
	
		}
		
		
		notifyDataSetChanged();
				
	}



}
