package com.recipeApp.fragments;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.Adapter.BasicIngredientArrayAdapter;
import com.example.Adapter.ExpandableListAdapter;
import com.example.Adapter.InteractiveArrayAdapter;
import com.example.helloworld.R;
import com.example.helloworld.RecipeSearchResultsActivity;
import com.example.model.Ingredient;
import com.example.viewHolder.IngredientViewHolder;

public class SelectIngredientFragment extends Fragment {

	XmlResourceParser myXml;
	int eventType;
	String nodeValue;
	List<Ingredient> ingredientOtherList = new ArrayList<Ingredient>();

	List<Ingredient> ingredientGrainList = new ArrayList<Ingredient>();
	List<Ingredient> ingredientMeetList = new ArrayList<Ingredient>();
	List<Ingredient> ingredientPulseList = new ArrayList<Ingredient>();
	List<Ingredient> ingredientVegetableList = new ArrayList<Ingredient>();
	List<Ingredient> ingredientFruitList = new ArrayList<Ingredient>();

	ArrayList<Ingredient> basicIngredientsList = new ArrayList<Ingredient>();
	ArrayList<Ingredient> ingrediientList = new ArrayList<Ingredient>();

	PopupWindow popupMessage;
	Button popupButton, insidePopupButton;
	LinearLayout layoutOfPopup;
	TextView popupText;
	EditText editsearch;
	ListView mainListView;
	InteractiveArrayAdapter listAdapter;
	Button searchButton;
	CheckBox showOnlyVideoRecipes;

	ExpandableListAdapter elistAdapter;
	ExpandableListView expListView;
	List<String> ingredientCategory;
	HashMap<String, List<Ingredient>> ingredientsMap;
	private ProgressDialog progressDialog;

	public static final String ARG_PLANET_NUMBER = "planet_number";

	public SelectIngredientFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.main, container, false);

		readXml();

		editsearch = (EditText) rootView.findViewById(R.id.searchIngredient);

		// Capture Text in EditText
		editsearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String text = editsearch.getText().toString()
						.toLowerCase(Locale.getDefault());
				elistAdapter.filter(text);
				elistAdapter.arrangeIngredientList();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}
		});

		addListenerOnButton(rootView);

		expListView = (ExpandableListView) rootView.findViewById(R.id.ingredientListView);

		// preparing list data

		elistAdapter = new ExpandableListAdapter(getActivity()
				.getApplicationContext(), ingredientCategory, ingredientsMap);

		// setting list adapter
		expListView.setAdapter(elistAdapter);
		
/*		
		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
			
				 Ingredient ingredient = (Ingredient) elistAdapter.getChild(groupPosition, childPosition);  
	        	  ingredient.toggleChecked();  
	            IngredientViewHolder viewHolder = (IngredientViewHolder) v.getTag();  
	            viewHolder.getCheckBox().setChecked( ingredient.isChecked() ); 
	            
	            listAdapter.filter("");
	            listAdapter.arrangeIngredientList();

				
			return false;
			}
		});*/

		

		return rootView;
	}
	
	
	
	private class LoadViewTask extends AsyncTask<Void, Integer, Void> {

		protected void onPreExecute() {

			progressDialog = ProgressDialog.show(getActivity(), "Loading...",
					"Loading, please wait...");

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			
			showOnlyVideoRecipes = (CheckBox)getActivity().findViewById(R.id.showVideoRecipes);
					
			Intent intent = new Intent(getActivity(),
					RecipeSearchResultsActivity.class);
			intent.putExtra("ingredientList", ingredientsMap);
			intent.putExtra("basicIngredientList", basicIngredientsList);
			intent.putExtra("showVideoRecipes", showOnlyVideoRecipes.isChecked());
			
			startActivity(intent);
			
			return null;
		}

		protected void onPostExecute(Void result) {

			try {
				if (progressDialog.isShowing())
					progressDialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();

			}

		}

	}

	private void prepareListData() {
		ingredientCategory = new ArrayList<String>();
		ingredientsMap = new HashMap<String, List<Ingredient>>();

		// Adding child data
		ingredientCategory.add("Vegetables");
		ingredientCategory.add("Meat");
		ingredientCategory.add("Pulses");
		ingredientCategory.add("Fruits");
		ingredientCategory.add("Grains");
		ingredientCategory.add("Other");

		ingredientsMap.put(ingredientCategory.get(0), ingredientVegetableList); // Header,
																				// Child
																				// data
		ingredientsMap.put(ingredientCategory.get(1), ingredientMeetList);
		ingredientsMap.put(ingredientCategory.get(2), ingredientPulseList);
		ingredientsMap.put(ingredientCategory.get(3), ingredientFruitList);
		ingredientsMap.put(ingredientCategory.get(4), ingredientGrainList);
		ingredientsMap.put(ingredientCategory.get(5), ingredientOtherList);
	}

	public void addListenerOnButton(View rootView) {

		searchButton = (Button) rootView.findViewById(R.id.searchButton);
		

		searchButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				new LoadViewTask().execute();

			}
		});

	}

	private void readXml() {

		FileInputStream fis = null;
		String temp1 = null;
		prepareListData();
		try {
			fis = getActivity().getApplicationContext().openFileInput(
					"all_ingredients");

			if (fis == null) {
				loadXml();
				writeIngredientInfoToXml();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			loadXml();
			prepareListData();
			writeIngredientInfoToXml();
			try {
				fis = getActivity().getApplicationContext().openFileInput(
						"all_ingredients");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();

		try {
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

			Document doc;
			try {

				doc = docBuilder.parse(fis);
				doc.getDocumentElement().normalize();

				NodeList list = doc.getElementsByTagName("Ingredients");

				for (int temp = 0; temp < list.getLength(); temp++) {

					Node nNode = list.item(temp);

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {

						Element eElement = (Element) nNode;

						for (Map.Entry<String, List<Ingredient>> entry : ingredientsMap
								.entrySet()) {
							List<Ingredient> ingredientlistfromMap = entry
									.getValue();

							String mapKey = getIngredientTagName(entry.getKey());

							NodeList recipelist = eElement
									.getElementsByTagName(mapKey);
							Ingredient ingredient = null;
							for (int i = 0; i < recipelist.getLength(); i++) {
								Node recipeNode = recipelist.item(i);

								Element recipeElement = (Element) recipeNode;

								ingredient = new Ingredient();

								NodeList ingredientNamelist = recipeElement
										.getElementsByTagName("Ingredient-Name");

								for (int j = 0; j < ingredientNamelist
										.getLength(); j++) {

									Node ingredientNameNode = ingredientNamelist
											.item(j);

									Element ingredientNameElement = (Element) ingredientNameNode;

									ingredient
											.setIngredientName(ingredientNameElement
													.getTextContent().trim());
									
									ingredient.setImage(getIngredient());

								}

								NodeList isCommonIngredientlist = recipeElement
										.getElementsByTagName("Is-Common-Ingredient");

								for (int j = 0; j < isCommonIngredientlist
										.getLength(); j++) {

									Node isCommonIngredientListNode = isCommonIngredientlist
											.item(j);

									Element isCommonIngredientElement = (Element) isCommonIngredientListNode;

									String text = isCommonIngredientElement
											.getTextContent().trim()
											.toLowerCase();

									if (text.equalsIgnoreCase("Yes"))
										ingredient.setBasicIngredient(true);
									else
										ingredient.setBasicIngredient(false);

								}

								if (!ingredient.isBasicIngredient())
									ingredientlistfromMap.add(ingredient);

							}

						}

						NodeList recipelist = eElement
								.getElementsByTagName("Basic-Ingredient");
						Ingredient ingredient = null;
						for (int i = 0; i < recipelist.getLength(); i++) {
							Node recipeNode = recipelist.item(i);

							Element recipeElement = (Element) recipeNode;

							ingredient = new Ingredient();

							NodeList ingredientNamelist = recipeElement
									.getElementsByTagName("Ingredient-Name");

							for (int j = 0; j < ingredientNamelist.getLength(); j++) {

								Node ingredientNameNode = ingredientNamelist
										.item(j);

								Element ingredientNameElement = (Element) ingredientNameNode;

								ingredient
										.setIngredientName(ingredientNameElement
												.getTextContent().trim());
								ingredient.setImage(getIngredient());

							}

							NodeList isCommonIngredientlist = recipeElement
									.getElementsByTagName("Is-Common-Ingredient");

							for (int j = 0; j < isCommonIngredientlist
									.getLength(); j++) {

								Node isCommonIngredientListNode = isCommonIngredientlist
										.item(j);

								Element isCommonIngredientElement = (Element) isCommonIngredientListNode;

								String text = isCommonIngredientElement
										.getTextContent().trim().toLowerCase();

								if (text.equalsIgnoreCase("Yes")) {
									ingredient.setBasicIngredient(true);
									ingredient.setChecked(true);

								} else {
									ingredient.setBasicIngredient(false);
									ingredient.setChecked(false);
								}
							}

							basicIngredientsList.add(ingredient);

						}

					}

				}

			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		System.out.println("afdad" + temp1);

	}
	
	
	private Integer getIngredient(){
		
		Random random = new Random();
		
		int i = random.nextInt(3);
		
		if(i == 0) return R.drawable.tomato;
		if(i == 1) return R.drawable.carrot;
		if(i == 2) return R.drawable.broccoli;
		
		
		return R.drawable.tomato;
	}


	private void loadXml() {
		// TODO Auto-generated method stub
		System.out.println("Inside LoadXml");
		myXml = getResources().getXml(R.xml.ingredients);
		try {
			myXml.next();

			eventType = myXml.getEventType(); // Get current xml Event

		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_DOCUMENT) {
				// checks can be placed here to make sure file is read
			} else if (eventType == XmlPullParser.START_TAG) {
				nodeValue = myXml.getName();

				try {

					if (nodeValue.equalsIgnoreCase("Ingredients")) { // Finds
																		// Ingredient
																		// tag
						myXml.next(); // Since the ingredient tag does not hold
										// the text, we go to next element area
						// ingredient.add(myXml.getText().trim()); // Set the
						// text of the Ingredient tag to list
					}
					nodeValue = myXml.getName();

					// Since the effect tags are followed by ending tags, we can
					// just do nextText() to get tag text
					if (nodeValue.equalsIgnoreCase("Basic-Ingredient")) {
						myXml.next();
						nodeValue = myXml.getName();

						if (nodeValue.equalsIgnoreCase("Ingredient-Name"))
							myXml.next();

						Ingredient basicIngredient = new Ingredient();
						basicIngredient.setIngredientName(myXml.getText()
								.trim());

						myXml.next();
						myXml.next();
						nodeValue = myXml.getName();

						if (nodeValue.equalsIgnoreCase("Is-Common-Ingredient")) {
							myXml.next();

							if (myXml.getText().trim().equalsIgnoreCase("Yes"))
								basicIngredient.setChecked(true);
							else
								basicIngredient.setChecked(false);
						}

						basicIngredientsList.add(basicIngredient); // ditto
					}

					if (nodeValue.equalsIgnoreCase("Ingredient-Other")) {
						myXml.next();

						nodeValue = myXml.getName();

						if (nodeValue.equalsIgnoreCase("Ingredient-Name"))
							myXml.next();
						Ingredient otherIngredient = new Ingredient();
						otherIngredient.setIngredientName(myXml.getText()
								.trim());

						myXml.next();
						myXml.next();
						nodeValue = myXml.getName();

						if (nodeValue.equalsIgnoreCase("Is-Common-Ingredient")) {
							myXml.next();

							if (myXml.getText().trim().equalsIgnoreCase("Yes"))
								otherIngredient.setChecked(true);
							else
								otherIngredient.setChecked(false);
						}

						ingredientOtherList.add(otherIngredient); // ditto
					}

					if (nodeValue.equalsIgnoreCase("Ingredient-Grain")) {
						myXml.next();
						nodeValue = myXml.getName();

						if (nodeValue.equalsIgnoreCase("Ingredient-Name"))
							myXml.next();

						Ingredient grainIngredient = new Ingredient();
						grainIngredient.setIngredientName(myXml.getText()
								.trim());

						myXml.next();
						myXml.next();
						nodeValue = myXml.getName();

						if (nodeValue.equalsIgnoreCase("Is-Common-Ingredient")) {
							myXml.next();

							if (myXml.getText().trim().equalsIgnoreCase("Yes"))
								grainIngredient.setChecked(true);
							else
								grainIngredient.setChecked(false);
						}

						ingredientGrainList.add(grainIngredient); // ditto
					}

					if (nodeValue.equalsIgnoreCase("Ingredient-Meet")) {
						myXml.next();

						nodeValue = myXml.getName();

						if (nodeValue.equalsIgnoreCase("Ingredient-Name"))
							myXml.next();

						Ingredient meetIngredient = new Ingredient();
						meetIngredient
								.setIngredientName(myXml.getText().trim());

						myXml.next();
						myXml.next();
						nodeValue = myXml.getName();

						if (nodeValue.equalsIgnoreCase("Is-Common-Ingredient")) {
							myXml.next();

							if (myXml.getText().trim().equalsIgnoreCase("Yes"))
								meetIngredient.setChecked(true);
							else
								meetIngredient.setChecked(false);
						}

						ingredientMeetList.add(meetIngredient); // ditto
					}

					if (nodeValue.equalsIgnoreCase("Ingredient-Pulse")) {
						myXml.next();
						nodeValue = myXml.getName();

						if (nodeValue.equalsIgnoreCase("Ingredient-Name"))
							myXml.next();

						Ingredient pulseIngredient = new Ingredient();
						pulseIngredient.setIngredientName(myXml.getText()
								.trim());

						myXml.next();
						myXml.next();
						nodeValue = myXml.getName();

						if (nodeValue.equalsIgnoreCase("Is-Common-Ingredient")) {
							myXml.next();

							if (myXml.getText().trim().equalsIgnoreCase("Yes"))
								pulseIngredient.setChecked(true);
							else
								pulseIngredient.setChecked(false);
						}

						ingredientPulseList.add(pulseIngredient); // ditto
					}

					if (nodeValue.equalsIgnoreCase("Ingredient-Vegetable")) {
						myXml.next();

						nodeValue = myXml.getName();

						if (nodeValue.equalsIgnoreCase("Ingredient-Name"))
							myXml.next();

						Ingredient vegetableIngredient = new Ingredient();
						vegetableIngredient.setIngredientName(myXml.getText()
								.trim());

						myXml.next();
						myXml.next();
						nodeValue = myXml.getName();

						if (nodeValue.equalsIgnoreCase("Is-Common-Ingredient")) {
							myXml.next();

							if (myXml.getText().trim().equalsIgnoreCase("Yes"))
								vegetableIngredient.setChecked(true);
							else
								vegetableIngredient.setChecked(false);
						}

						ingredientVegetableList.add(vegetableIngredient); // ditto
					}

					if (nodeValue.equalsIgnoreCase("Ingredient-Fruit")) {
						myXml.next();

						nodeValue = myXml.getName();

						if (nodeValue.equalsIgnoreCase("Ingredient-Name"))
							myXml.next();

						Ingredient fruitIngredient = new Ingredient();
						fruitIngredient.setIngredientName(myXml.getText()
								.trim());

						myXml.next();
						myXml.next();
						nodeValue = myXml.getName();

						if (nodeValue.equalsIgnoreCase("Is-Common-Ingredient")) {
							myXml.next();

							if (myXml.getText().trim().equalsIgnoreCase("Yes"))
								fruitIngredient.setChecked(true);
							else
								fruitIngredient.setChecked(false);
						}

						ingredientFruitList.add(fruitIngredient); // ditto
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

	}

	private String getIngredientTagName(String ingredientCategory) {

		if (ingredientCategory.trim().equalsIgnoreCase("Vegetables"))
			return "Ingredient-Vegetable";

		if (ingredientCategory.trim().equalsIgnoreCase("Fruits"))
			return "Ingredient-Fruit";

		if (ingredientCategory.trim().equalsIgnoreCase("Pulses"))
			return "Ingredient-Pulse";

		if (ingredientCategory.trim().equalsIgnoreCase("Meat"))
			return "Ingredient-Meet";

		if (ingredientCategory.trim().equalsIgnoreCase("Grains"))
			return "Ingredient-Grain";

		if (ingredientCategory.trim().equalsIgnoreCase("Other"))
			return "Ingredient-Other";

		return null;

	}

	public void writeIngredientInfoToXml() {

		FileOutputStream fos = null;

		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();

		try {
			String xmlFile = "all_ingredients";

			try {
				// fos = new FileOutputStream("ingredients.xml");

				fos = getActivity().openFileOutput(xmlFile,
						Context.MODE_PRIVATE);

			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();

			Element rootElement = doc.createElement("Ingredients");
			doc.appendChild(rootElement);

			for (Map.Entry<String, List<Ingredient>> entry : ingredientsMap
					.entrySet()) {
				List<Ingredient> ingredientlistfromMap = entry.getValue();

				String mapKey = getIngredientTagName(entry.getKey());

				List<Ingredient> listOfIngredients = null;

				for (Ingredient ingredient : ingredientlistfromMap) {
					Element ingredientElement = doc.createElement(mapKey);
					Element ingredientNameElement = doc
							.createElement("Ingredient-Name");
					Element isBasicIngredientElement = doc
							.createElement("Is-Common-Ingredient");

					ingredientNameElement.appendChild(doc
							.createTextNode(ingredient.getIngredientName()));
					isBasicIngredientElement.appendChild(doc
							.createTextNode("No"));

					ingredientElement.appendChild(ingredientNameElement);
					ingredientElement.appendChild(isBasicIngredientElement);

					rootElement.appendChild(ingredientElement);
				}

			}

			for (Ingredient ingredient : basicIngredientsList) {

				Element ingredientElement = doc
						.createElement("Basic-Ingredient");
				Element ingredientNameElement = doc
						.createElement("Ingredient-Name");
				Element isBasicIngredientElement = doc
						.createElement("Is-Common-Ingredient");

				ingredientNameElement.appendChild(doc.createTextNode(ingredient
						.getIngredientName()));
				isBasicIngredientElement.appendChild(doc.createTextNode("Yes"));

				ingredientElement.appendChild(ingredientNameElement);
				ingredientElement.appendChild(isBasicIngredientElement);

				rootElement.appendChild(ingredientElement);

			}

			// Write the content into the xml

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			try {
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				DOMSource source = new DOMSource(doc);

				StreamResult result = new StreamResult(fos);

				try {
					transformer.transform(source, result);
				} catch (TransformerException e) {
					e.printStackTrace();
				}

			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

	}

}
