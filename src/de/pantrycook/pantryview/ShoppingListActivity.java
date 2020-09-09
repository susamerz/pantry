package de.pantrycook.pantryview;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.os.Build;

public class ShoppingListActivity extends ActionBarActivity {
	
	//public static final String SHOP_LIST_CONTENT = "de.pantrycook.pantryview.SHOP_LIST_CONTENT";
	//private static final String savefile = null;
	EditText mShoppingList;
	String SLISTsavefile = "shoppingListSave.txt";
	Context PantryViewContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PantryViewContext= getParent();
		mShoppingList = new EditText(this);
		mShoppingList.setText("test");
		setContentView(mShoppingList);
		// not used as long as no fragments
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shopping_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		// TODO if RecipeList test turn out fine, use the interface/ call from parent route here too
		switch(id) {
		case R.id.action_recipe_list:
			openRecipeList(findViewById(R.id.action_recipe_list));
			return true;
		case R.id.action_pantry:
			openPantryView(findViewById(R.id.action_pantry));
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	protected void onStart(){
		super.onStart();
		// TODO read current shopping list from persistent file
		
	
				
					try {
						File fileDirectory = getFilesDir();
						String fileloc=fileDirectory.getAbsolutePath();
						String fname= fileloc+"/"+SLISTsavefile;
						File f = new File(fname);
						if (f.exists()){
						FileInputStream fis= openFileInput(SLISTsavefile);
						BufferedInputStream bis = new BufferedInputStream(fis);
						int estSize=bis.available();
						byte [] buff = new byte[estSize];
						int index=0;
					//	long fileLength=f.length();
						Integer readBit=bis.read();
						while (readBit!= -1){
							buff[index]=readBit.byteValue();
							readBit=bis.read();
							index++;
						}
						String newSLIST = new String(buff);
						mShoppingList.setText(newSLIST);}
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				
	}
	
	
//	protected void onResume(){
//		super.onResume();
//		// TODO read current shopping list from cache
//	}
//	
	
	private void openPantryView(View findViewById) {
		Intent pantry_intent = new Intent (this, PantryViewActivity.class);
		startActivity(pantry_intent);
		
	}

	private void openRecipeList(View findViewById) {
		Intent recipe_intent = new Intent (this, RecipeListActivity.class);
		startActivity(recipe_intent);
		
	}
	
	
	
	protected void onPause(){
		super.onPause();
		// TODO write to cache
		try {
			FileOutputStream outStream = openFileOutput(SLISTsavefile, Context.MODE_PRIVATE);
			String text= mShoppingList.getText().toString();
			outStream.write(text.getBytes());
			outStream.close();
		
			}
			catch (Exception e){
				e.printStackTrace();
			}
		
		
	}

	protected void onStop(){
		super.onStop();
		
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
			View rootView = inflater.inflate(R.layout.fragment_shopping_list,
					container, false);
			return rootView;
		}
	}

}
