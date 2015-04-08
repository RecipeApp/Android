package com.recipeApp.fragments;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.Adapter.BasicIngredientArrayAdapter;
import com.example.helloworld.R;
import com.example.model.Ingredient;
import com.example.viewHolder.IngredientViewHolder;

public class ManageCommonIngredientFragment extends Fragment {

	XmlResourceParser myXml;
	int eventType;
	String nodeValue;

	ArrayList<Ingredient> basicIngredientsList = new ArrayList<Ingredient>();
	ListView basicIngredientsListView;
	private BasicIngredientArrayAdapter basicIngredientAdapter;
	Button saveIngredientsButton;

	HashMap<String, List<Ingredient>> ingredientsMap;

	private ProgressDialog progressDialog;

	public ManageCommonIngredientFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.common_ingredients,
				container, false);

		readXml();

		basicIngredientsListView = (ListView) rootView
				.findViewById(R.id.common_ingredient);
		basicIngredientAdapter = new BasicIngredientArrayAdapter(getActivity(),
				basicIngredientsList);
		basicIngredientsListView.setAdapter(basicIngredientAdapter);

		basicIngredientsListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View item,
							int position, long id) {
						Ingredient ingredient = basicIngredientAdapter
								.getItem(position);
						ingredient.toggleChecked();
						ingredient.setChanged(true);
						IngredientViewHolder viewHolder = (IngredientViewHolder) item
								.getTag();
						viewHolder.getCheckBox().setChecked(
								ingredient.isChecked());

						basicIngredientAdapter.filter("");
						basicIngredientAdapter.arrangeIngredientList();

					}
				});

		basicIngredientAdapter.arrangeIngredientList();

		addListenerOnButton(rootView);

		return rootView;
	}

	private class LoadViewTask extends AsyncTask<Void, Integer, Void> {

		protected void onPreExecute() {

			progressDialog = ProgressDialog.show(getActivity(), "Loading...",
					"Loading, please wait...");

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			saveChangedIngredients();
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

	public void addListenerOnButton(View rootView) {

		saveIngredientsButton = (Button) rootView
				.findViewById(R.id.saveIngredientsButton);

		saveIngredientsButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				new LoadViewTask().execute();
				basicIngredientAdapter.arrangeIngredientList();

			}
		});

	}

	private void saveChangedIngredients() {

		List<Ingredient> listOfChangedIngredient = new ArrayList<Ingredient>();

		for (Ingredient ingredient : basicIngredientsList) {

			if (ingredient.isChanged()) {

				System.out.println("Changed Ingredient "
						+ ingredient.getIngredientName());

				listOfChangedIngredient.add(ingredient);

			}

		}
		updateIngredientsXmlFile(listOfChangedIngredient);
		//basicIngredientAdapter.arrangeIngredientList();

	}

	private boolean isIngredientChanged(String ingredientName,
			List<Ingredient> changedIngredientList) {
		for (Ingredient ingredient : changedIngredientList) {

			if (ingredient.getIngredientName().equalsIgnoreCase(ingredientName))
				return true;
		}
		return false;

	}

	private void updateIngredientsXmlFile(List<Ingredient> changedIngredientList) {

		FileOutputStream fos = null;

		FileInputStream fis = null;
		String temp1 = null;
		try {
			fis = getActivity().getApplicationContext().openFileInput(
					"all_ingredients");

			if (fis == null) {
				loadXml();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			loadXml();
			try {
				fis = getActivity().getApplicationContext().openFileInput(
						"all_ingredients");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();

		try {

			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();

			Document document = documentBuilder.parse(fis);

			document.getDocumentElement().normalize();

			NodeList list = document.getElementsByTagName("Ingredients");

			for (int temp = 0; temp < list.getLength(); temp++) {

				Node nNode = list.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					for (String ingredientTagName : getIngredientTagNameList()) {

						NodeList recipelist = eElement
								.getElementsByTagName(ingredientTagName);

						for (int i = 0; i < recipelist.getLength(); i++) {
							Node recipeNode = recipelist.item(i);

							Element recipeElement = (Element) recipeNode;

							NodeList ingredientNamelist = recipeElement
									.getElementsByTagName("Ingredient-Name");

							boolean isChanged = false;

							for (int j = 0; j < ingredientNamelist.getLength(); j++) {

								Node ingredientNameNode = ingredientNamelist
										.item(j);

								Element ingredientNameElement = (Element) ingredientNameNode;

								if (isIngredientChanged(ingredientNameElement
										.getTextContent().trim(),
										changedIngredientList)) {
									isChanged = true;

								}

							}

							NodeList nodeList = recipeElement
									.getElementsByTagName("Is-Common-Ingredient");

							for (int j = 0; j < nodeList.getLength(); j++) {

								Node tokenNode = nodeList.item(j);
								Element element = (Element) tokenNode;

								if (isChanged) {

									if (element.getTextContent().trim()
											.toLowerCase()
											.equalsIgnoreCase("yes"))
										element.setTextContent("No");
									else if (element.getTextContent().trim()
											.toLowerCase()
											.equalsIgnoreCase("no"))
										element.setTextContent("Yes");
								}

							}

						}

					}

					NodeList recipelist = eElement
							.getElementsByTagName("Basic-Ingredient");

					for (int i = 0; i < recipelist.getLength(); i++) {
						Node recipeNode = recipelist.item(i);

						Element recipeElement = (Element) recipeNode;

						NodeList ingredientNamelist = recipeElement
								.getElementsByTagName("Ingredient-Name");

						boolean isChanged = false;

						for (int j = 0; j < ingredientNamelist.getLength(); j++) {

							Node ingredientNameNode = ingredientNamelist
									.item(j);

							Element ingredientNameElement = (Element) ingredientNameNode;

							if (isIngredientChanged(ingredientNameElement
									.getTextContent().trim(),
									changedIngredientList)) {
								isChanged = true;

							}

						}

						NodeList nodeList = recipeElement
								.getElementsByTagName("Is-Common-Ingredient");

						for (int j = 0; j < nodeList.getLength(); j++) {

							Node tokenNode = nodeList.item(j);
							Element element = (Element) tokenNode;

							if (isChanged) {

								if (element.getTextContent().trim()
										.toLowerCase().equalsIgnoreCase("yes"))
									element.setTextContent("No");
								else if (element.getTextContent().trim()
										.toLowerCase().equalsIgnoreCase("no"))
									element.setTextContent("Yes");
							}

						}

					}

				}

			}

			String xmlFile = "all_ingredients";

			try {

				fos = getActivity().openFileOutput(xmlFile,
						Context.MODE_PRIVATE);

			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			// Write the content into the xml

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			try {
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				DOMSource source = new DOMSource(document);

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void readXml() {

		FileInputStream fis = null;
		String temp1 = null;

		try {
			fis = getActivity().getApplicationContext().openFileInput(
					"all_ingredients");

			if (fis == null) {
				loadXml();

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			loadXml();

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

						for (String ingredientTagName : getIngredientTagNameList()) {

							NodeList recipelist = eElement
									.getElementsByTagName(ingredientTagName);
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

	private List<String> getIngredientTagNameList() {

		List<String> ingredientCategoryTagList = new ArrayList<String>();

		ingredientCategoryTagList.add("Ingredient-Vegetable");

		ingredientCategoryTagList.add("Ingredient-Fruit");

		ingredientCategoryTagList.add("Ingredient-Pulse");

		ingredientCategoryTagList.add("Ingredient-Meet");

		ingredientCategoryTagList.add("Ingredient-Grain");

		ingredientCategoryTagList.add("Ingredient-Other");

		return ingredientCategoryTagList;

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

	class StringOutputStream extends OutputStream {

		private StringBuilder m_string;

		StringOutputStream() {

			m_string = new StringBuilder();
		}

		@Override
		public void write(int b) throws IOException {
			m_string.append((char) b);

		}

		@Override
		public String toString() {

			return m_string.toString();

		}

	}

}
