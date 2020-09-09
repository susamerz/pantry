package de.pantrycook.pantryview;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;

import de.pantrycook.pantryview.PantryStorageContract.IngredientTable;
import de.pantrycook.pantryview.PantryStorageContract.StorageTable;
import de.pantrycook.pantryview.StorageViewFragment.OnIngredientSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Build;

public class PantryViewActivity extends ActionBarActivity implements OnIngredientSelectedListener{

	public static final String ING_KEY = "ingredient_key";
	SQLiteDatabase PantryBase=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pantry_view);
		OpenDatabaseTask dbTask= new OpenDatabaseTask(getApplicationContext());
		PantryBase=dbTask.doInBackground((Integer)null);
		// by default open the view listing all exsisting ingredients
		if (savedInstanceState == null) {

			String[] projection1= {
					StorageTable._ID,
					StorageTable.STORAGE_COLUMN_INGREDIENT,
					StorageTable.STORAGE_COLUMN_AMOUNT
			};

			String orderByName = StorageTable.STORAGE_COLUMN_INGREDIENT + " DESC";

			StorageViewFragment StorageContents = new StorageViewFragment();
			Cursor c = PantryBase.query(StorageTable.STORAGE_TABLE_NAME, projection1,null, null, null, null, orderByName);
			SimpleCursorAdapter PantryBaseAdapter = new SimpleCursorAdapter(
							this, 
							R.layout.two_item_row, 
							c, 
							new String [] {StorageTable.STORAGE_COLUMN_INGREDIENT,StorageTable.STORAGE_COLUMN_AMOUNT}, 
							new int[] {R.id.two_item_row_item1,R.id.two_item_row_item2}, 
							CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
							);
			StorageContents.setListAdapter(PantryBaseAdapter);
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, StorageContents,"list1").commit();
		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pantry_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		// TODO if RecipeList test turn out fine, use the interface/ call from parent route here too
		int id = item.getItemId();
		switch(id) {
		case R.id.action_shopping_list:
			openShoppingList(findViewById(R.id.action_shopping_list));
			return true;
		case R.id.action_recipe_list:
			openRecipeList(findViewById(R.id.action_recipe_list));
			return true;
		case R.id.action_settings: 
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void openShoppingList(View findViewById) {
		Intent shop_intent = new Intent (this, ShoppingListActivity.class);
		startActivity(shop_intent);
		
	}
	private void openRecipeList(View findViewById) {
		Intent recipe_intent = new Intent (this, RecipeListActivity.class);
		startActivity(recipe_intent);
		
	}
	/*
	 *  wrapper method to conform to button click requirements
	 */
	public void openManualAdd(View view){
		Bundle args = new Bundle();
		args.putString(ING_KEY, "Enter Ingredient");
		openManualAdd(view, args);
	}
	
	
	public void openManualAdd(View view, Bundle args){
		// create manual add fragment instance
		
		ManualEntryFragment addFrag = new ManualEntryFragment();
		addFrag.setArguments(args);
		// use fragment manager to show the new fragment
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		
		transaction.replace(R.id.container, addFrag);
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	public void removeAmount(View view){
		// figure out what ingredient and amount
		EditText name_field= (EditText) findViewById(R.id.storage_ingredient);
		String name = name_field.getText().toString();
		EditText amount_field= (EditText) findViewById(R.id.storage_amount);
		double amount = Double.parseDouble(amount_field.getText().toString());
		//query database for ingredient
		
		String[] projection= {
				StorageTable._ID,
				StorageTable.STORAGE_COLUMN_INGREDIENT,
				StorageTable.STORAGE_COLUMN_AMOUNT,
				StorageTable.STORAGE_COLUMN_USEBY
		};
		String selection = StorageTable.STORAGE_COLUMN_INGREDIENT+"=?";
		String[] selArgs = {name};
		String orderBy = StorageTable.STORAGE_COLUMN_USEBY + " ASC";
		Cursor workingCursor = PantryBase.query(StorageTable.STORAGE_TABLE_NAME, projection,selection, selArgs, null, null, orderBy);
		
		// do the removal, looped in case the amount removed is large that the amount of the first entry for that ingredient
		workingCursor.moveToFirst();
		int idx=workingCursor.getColumnIndex(StorageTable.STORAGE_COLUMN_AMOUNT);
		
		// loop, summing total amount, because for some reason getBlob does throw an exception
		int rowcount=workingCursor.getCount();
		double total=0;
		for (int i =0;i<rowcount;i++){
			double tmp = workingCursor.getDouble(idx);
			total=total+tmp;
			workingCursor.moveToNext();
		}
		
		workingCursor.moveToFirst();
		// check that the total amount in the database is not smaller than the amount to remove
	//	byte[] allAmounts=c.getBlob(idx);
		//double[] amounts_double = ByteBuffer.wrap(allAmounts).asDoubleBuffer().array();
		//double total=0;
		//for (int i=0;i<amounts_double.length;i++){
		//	total=total+amounts_double[i];
		//}
		int duration = Toast.LENGTH_LONG;
		if (total<amount) { 
			CharSequence errtext = "There was not enough "+name+" in the database, nothing was removed";
			Toast notProcessing = Toast.makeText(getApplicationContext(), errtext, duration);
			notProcessing.show();
			return;
		}
		
		// if sufficient amount of ingredient was in database, we can remove the requested amount
		double cd_amount=amount;
		while (cd_amount>0){
			double curr_entry_amount=workingCursor.getDouble(idx);
			String whereClause = StorageTable._ID+"="+workingCursor.getString(workingCursor.getColumnIndex(StorageTable._ID));
			if (curr_entry_amount>cd_amount){
				// sufficient amount of ingredient in this entry, update database with new amount
				double amount_new=curr_entry_amount-cd_amount;
				ContentValues values = new ContentValues();
				values.put(StorageTable.STORAGE_COLUMN_AMOUNT, amount_new);
				PantryBase.update(StorageTable.STORAGE_TABLE_NAME, values, whereClause, null);
				cd_amount=0;
				// since we're done removing we can leave this loop, this is not necessary, but avoids one check for the while condition
				break;
			}
			else{
				// not a sufficient amount in first ingredient, delete the whole thing
				// reducing the cd_amount accordingly and the iterate to next entry
				cd_amount=cd_amount-curr_entry_amount;
				PantryBase.delete(StorageTable.STORAGE_TABLE_NAME, whereClause,null);
				workingCursor.moveToNext();
				
			}
		}
		workingCursor.close();
		
		CharSequence successText= amount+"g of "+name+" were removed from the database";
		
		Toast success = Toast.makeText(getApplicationContext(), successText, duration);
		success.show();
		String[] projection1= {
				StorageTable._ID,
				StorageTable.STORAGE_COLUMN_INGREDIENT,
				StorageTable.STORAGE_COLUMN_AMOUNT
		};

		String orderByName = StorageTable.STORAGE_COLUMN_INGREDIENT + " DESC";

		Cursor listFragmentCursor = PantryBase.query(StorageTable.STORAGE_TABLE_NAME, projection1,null, null, null, null, orderByName);
		Fragment temp = this.getSupportFragmentManager().findFragmentByTag("list1");
		if (temp instanceof ListFragment){
			ListFragment notifyRecipient = (ListFragment) temp;
			((SimpleCursorAdapter)notifyRecipient.getListAdapter()).changeCursor(listFragmentCursor);
		}
		
		
			
			
	
		
		
		
	}

	
	/*
	 * method to add an ingredient to the database, called by add button in manual add fragment
	 * TODO needs to check whether ingredient with same name and same use-by date is already in database
	 */
	public void addStorageEntry(View view){
		// collect data to be entered from Text Fields
		EditText name_field= (EditText) findViewById(R.id.storage_ingredient);
		String name = name_field.getText().toString();
		EditText amount_field= (EditText) findViewById(R.id.storage_amount);
		double amount = Double.parseDouble(amount_field.getText().toString());
		EditText date_field = (EditText) findViewById(R.id.storage_useby);
		String uBDate = date_field.getText().toString();
//		String [] recipe_projection = {IngredientTable._ID,
//				IngredientTable.INGREDIENT_COLUMN_INGREDIENT_NAME,
//				IngredientTable.INGREDIENT_COLUMN_AMOUNT
//		};
	//	String recipe_where = IngredientTable.INGREDIENT_COLUMN_INGREDIENT_NAME+"="+name;
		String sql_avg_query = "SELECT AVG("+
				IngredientTable.INGREDIENT_COLUMN_AMOUNT+") FROM (SELECT * FROM "+
				IngredientTable.INGREDIENT_TABLE_NAME+" WHERE "+
				IngredientTable.INGREDIENT_COLUMN_INGREDIENT_NAME+" = '"+ name + "')";
		

		Cursor avg_cursor = PantryBase.rawQuery(sql_avg_query, null);
		//Cursor d = PantryBase.query(IngredientTable.INGREDIENT_TABLE_NAME, recipe_projection ,  recipe_where, null, null, null, null);
		double typical;
		if (avg_cursor.moveToFirst()){
			typical = avg_cursor.getDouble(0);
		}
		else{
			
			// TODO make sure this is the correct value for metric computation
			typical = Double.POSITIVE_INFINITY;
		}
		avg_cursor.close();
		ContentValues newRow = new ContentValues();
		newRow.put(StorageTable.STORAGE_COLUMN_INGREDIENT, name.toLowerCase());
		newRow.put(StorageTable.STORAGE_COLUMN_AMOUNT,amount);
		//TODO make sure ub date is in propper date format
		newRow.put(StorageTable.STORAGE_COLUMN_USEBY,uBDate);
		newRow.put(StorageTable.STORAGE_COLUMNS_TYPICAL_AMOUNT, typical);
		// insert into database
		PantryBase.insert(StorageTable.STORAGE_TABLE_NAME, null, newRow);
		int duration = Toast.LENGTH_LONG;
		
		CharSequence successText= amount+"g of "+name+" were added to the database";
		
		Toast success = Toast.makeText(getApplicationContext(), successText, duration);
		success.show();
		
		String[] projection1= {
				StorageTable._ID,
				StorageTable.STORAGE_COLUMN_INGREDIENT,
				StorageTable.STORAGE_COLUMN_AMOUNT
		};

		String orderByName = StorageTable.STORAGE_COLUMN_INGREDIENT + " DESC";

	
		Cursor listFragmentCursor = PantryBase.query(StorageTable.STORAGE_TABLE_NAME, projection1,null, null, null, null, orderByName);
		Fragment temp = this.getSupportFragmentManager().findFragmentByTag("list1");
		if (temp instanceof ListFragment){
			ListFragment notifyRecipient = (ListFragment) temp;
			((SimpleCursorAdapter)notifyRecipient.getListAdapter()).changeCursor(listFragmentCursor);
		}

	}
		
		public void onClose(){
		Fragment temp = this.getSupportFragmentManager().findFragmentByTag("list1");
		if (temp instanceof ListFragment){
			((SimpleCursorAdapter)((ListFragment) temp).getListAdapter()).getCursor().close();
		}

	}

/*
 *  method to react to no use-by date checkbox on maunal entry fragment
 */
	public void toggleUBdate(View view){
		boolean checked = ((CheckBox) view).isChecked();
		EditText ub_field = (EditText) findViewById(R.id.storage_useby);
		if (checked){
			ub_field.setText("inf");
			ub_field.setFocusable(false);
			
		}
		else{
			ub_field.setFocusable(true);
			ub_field.setFocusableInTouchMode(true);
			ub_field.setText(null);
			ub_field.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
			
		}
		
	}
	
// asynch task as private subclasses are discouraged due to possible leaks
//	private class OpenDatabaseTask extends AsyncTask< Integer, Integer, SQLiteDatabase>{
//
//		@Override
//		protected SQLiteDatabase doInBackground(Integer... params) {
//			// TODO Auto-generated method stub
//			PantryDbHelper pDbHelper = new PantryDbHelper(getApplicationContext(),PantryDbHelper.DATABASE_NAME,null,PantryDbHelper.DATABASE_VERSION);
//			SQLiteDatabase db = pDbHelper.getWritableDatabase();
//			return db;
//		}
//	}
	

}
