package de.pantrycook.pantryview;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class StartScreen extends ActionBarActivity implements PantryViewMainInterface {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_screen);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void openRecipeList(View view){
		// switch to the list of recipes 
		// atm this starts a new recipe list view every time it is called
		Intent recipe_intent = new Intent (this, RecipeListActivity.class);
		startActivity(recipe_intent);
	}
	
	public void openShoppingList(View view){
		// switch to the shopping list view
		// atm this starts a new shopping list every time it is called
		Intent shop_intent = new Intent (this, ShoppingListActivity.class);
		startActivity(shop_intent);
	}
	
	public void openPantryView(View view){
		// switch to the list of stored goods
		// atm this starts a new pantry view every time it is called
		Intent pantry_intent = new Intent (this, PantryViewActivity.class);
		startActivity(pantry_intent);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_start_screen,
					container, false);
			return rootView;
		}
	}
	

}
