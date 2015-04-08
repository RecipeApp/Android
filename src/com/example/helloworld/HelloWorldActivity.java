package com.example.helloworld;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.RecipeDAO.GetAllRecipes;
import com.example.RecipeDAO.IGetAllRecipes;
import com.example.RecipeService.IRecipeSearch;
import com.example.RecipeService.RecipeSearch;
import com.example.Service.LoadRecipes;
import com.example.model.Ingredient;
import com.recipeApp.fragments.ManageCommonIngredientFragment;
import com.recipeApp.fragments.SelectIngredientFragment;

public class HelloWorldActivity extends Activity {

	Spinner spinner;
	List<Ingredient> ingredientList = new ArrayList<Ingredient>();
	ArrayList<Ingredient> basicIngredientsList = new ArrayList<Ingredient>();
	ArrayList<Ingredient> ingrediientList = new ArrayList<Ingredient>();

	Button searchButton;

	XmlResourceParser myXml;
	int eventType;
	String nodeValue;
	PopupWindow popupMessage;
	Button popupButton, insidePopupButton;
	LinearLayout layoutOfPopup;
	TextView popupText;
	EditText editsearch;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] menuTitles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main2);

		mTitle = mDrawerTitle = getTitle();
		menuTitles = getResources().getStringArray(R.array.menu_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, menuTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_websearch:
			// create intent to perform web search for this planet
			Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
			intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
			// catch event that there's no activity to handle intent
			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(intent);
			} else {
				Toast.makeText(this, R.string.app_not_available,
						Toast.LENGTH_LONG).show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;

		if (position == 1)
			fragment = new ManageCommonIngredientFragment();
		else
			fragment = new SelectIngredientFragment();

		Bundle args = new Bundle();
		
		// args.put
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(menuTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	public void startService() {
		startService(new Intent(getBaseContext(), LoadRecipes.class));
	}

	public void addListenerOnButton() {

		searchButton = (Button) findViewById(R.id.searchButton);

		searchButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				IGetAllRecipes getAllRecipe = new GetAllRecipes();
				IRecipeSearch recipeSearch = new RecipeSearch(getAllRecipe);
				recipeSearch.search(ingrediientList);

				Intent intent = new Intent(HelloWorldActivity.this,
						RecipeSearchResultsActivity.class);
				intent.putExtra("ingredientList", ingrediientList);
				intent.putExtra("basicIngredientList", basicIngredientsList);

				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private void loadXml() {
		// TODO Auto-generated method stub
		System.out.println("Inside LoadXml");
		myXml = getBaseContext().getResources().getXml(R.xml.ingredients);
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
					if (nodeValue.equalsIgnoreCase("Ingredient")) {
						myXml.next();
						Ingredient ingredient = new Ingredient();
						ingredient.setIngredientName(myXml.getText().trim());

						ingredientList.add(ingredient); // Set the text of the
														// Effect1 tag to list
					}
					if (nodeValue.equalsIgnoreCase("Basic-Ingredient")) {
						myXml.next();
						Ingredient basicIngredient = new Ingredient();
						basicIngredient.setIngredientName(myXml.getText()
								.trim());
						basicIngredient.setChecked(true);

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

}
