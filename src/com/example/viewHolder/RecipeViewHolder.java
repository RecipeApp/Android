package com.example.viewHolder;

import android.widget.TextView;

public class RecipeViewHolder {

	private TextView textViewName ; 
	private TextView textView ; 

	public RecipeViewHolder(TextView textViewName , TextView textView ){
		this.textViewName = textViewName;
		this.textView = textView;
		
	}

	public TextView getTextViewName() {
		return textViewName;
	}

	public void setTextViewName(TextView textViewName) {
		this.textViewName = textViewName;
	}

	public TextView getTextView() {
		return textView;
	}

	public void setTextView(TextView textView) {
		this.textView = textView;
	}
	
	
}
