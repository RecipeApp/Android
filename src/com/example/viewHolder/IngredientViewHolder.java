package com.example.viewHolder;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class IngredientViewHolder {

    private CheckBox checkBox ;  
    private TextView textView ;  
    private ImageView imageView;
    
    public IngredientViewHolder() {}  
    
    public IngredientViewHolder( TextView textView, CheckBox checkBox,ImageView imageView ) {  
      this.checkBox = checkBox ;  
      this.textView = textView ;  
      this.imageView = imageView;
    }  
    public CheckBox getCheckBox() {  
      return checkBox;  
    }  
    public void setCheckBox(CheckBox checkBox) {  
      this.checkBox = checkBox;  
    }  
    public TextView getTextView() {  
      return textView;  
    }  
    public void setTextView(TextView textView) {  
      this.textView = textView;  
    }

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}      
    
    
    
  } 