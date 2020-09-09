package de.pantrycook.pantryview;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.Locale;

import de.pantrycook.pantryview.MealPlanFragment.OnMealPlanActionListener;
import de.pantrycook.pantryview.PantryStorageContract.CookbookTable;
import de.pantrycook.pantryview.PantryStorageContract.IngredientTable;
import de.pantrycook.pantryview.PantryStorageContract.MealPlanTable;
import de.pantrycook.pantryview.PantryStorageContract.StorageTable;
import de.pantrycook.pantryview.RecipeAddFragment.OnRecipeSubmitListener;
import de.pantrycook.pantryview.RecipeListFragment.OnRecipeSelectedListener;
import de.pantrycook.pantryview.RecipeSelectedDialogFragment.OnRecipeOptionSelectedListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;

public class RecipeListActivity extends ActionBarActivity implements OnRecipeSelectedListener,OnRecipeSubmitListener,OnRecipeOptionSelectedListener, OnWeekdaySelectListener,OnMealPlanActionListener{

	public final static String RECIPE_TITLE = "de.pantrycook.pantryview.recipe_title";
	public final static String RECIPE_INGREDIENTS="de.pantrycook.pantryview.recipe_ingredients";
	public final static String RECIPE_METHOD= "de.pantrycook.pantryview.recipe_method";
	public final static String RECIPE_ID= "de.pantrycook.pantryview.recipe_id";
	PantryViewMainInterface mMainWindowListener = null;
	SQLiteDatabase PantryBase;
	private int currentSelectedRecipeId=0;
	private String currentDay="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_list);
		OpenDatabaseTask dbTask= new OpenDatabaseTask(getApplicationContext());
		PantryBase=dbTask.doInBackground((Integer)null);

		if (savedInstanceState == null) {

			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recipe_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id) {
		case R.id.action_shopping_list:

			return true;
		case R.id.action_pantry:
			return true;
		case R.id.action_settings:
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

//*****************************************************************************************************************
// Recipe display
//*****************************************************************************************************************

	public void openRecipeListView(View view){

		String[] projection_recipe_list= {
				CookbookTable._ID,
				CookbookTable.COOKBOOK_COLUMN_RECIPE_NAME
		};

		String orderByName = CookbookTable.COOKBOOK_COLUMN_RECIPE_NAME + " DESC";

		RecipeListFragment Recipes = new RecipeListFragment();
		Cursor c = PantryBase.query(CookbookTable.COOKBOOK_TABLE_NAME, projection_recipe_list,null, null, null, null, orderByName);
		SimpleCursorAdapter PantryBaseAdapter = new SimpleCursorAdapter(
				this,  
				android.R.layout.simple_selectable_list_item,
				c, 
				new String [] {CookbookTable.COOKBOOK_COLUMN_RECIPE_NAME}, 
				new int[] {android.R.id.text1}, 
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
				);
		Recipes.setListAdapter(PantryBaseAdapter);
		FragmentTransaction trans=getSupportFragmentManager().beginTransaction().replace(R.id.container, Recipes, "recipe_list");
		trans.addToBackStack(null);
		trans.commitAllowingStateLoss();

		//.add(R.id.container, Recipes,"recipe_list").commit()



	}
	



	@Override
	public void showRecipeDialog(View view, Bundle args) {
		RecipeSelectedDialogFragment selDialog=new RecipeSelectedDialogFragment();
		currentSelectedRecipeId=args.getInt(RecipeListActivity.RECIPE_ID);
		selDialog.show(getSupportFragmentManager(), "selectionDialog");

	}
	
	
	@Override
	public void showRecipe(View view, Bundle args) {
		
		RecipeDisplayFragment recipewindow= new RecipeDisplayFragment();
		
		// TODO setting things too early, fix problem with view not exstisting before onCreateview
		String[] projection_recipe_show= {
				CookbookTable._ID,
				CookbookTable.COOKBOOK_COLUMN_RECIPE_NAME,
				CookbookTable.COOKBOOK_COLUMN_RECIPE_METHOD
		};

		String where_recipe =  CookbookTable._ID +" = "+ currentSelectedRecipeId;
		String orderByName_recipe = CookbookTable.COOKBOOK_COLUMN_RECIPE_NAME + " DESC";

		Cursor c = PantryBase.query(CookbookTable.COOKBOOK_TABLE_NAME, projection_recipe_show, where_recipe , null, null, null, orderByName_recipe);
		c.moveToFirst();
		recipewindow.setTitle(c.getString(c.getColumnIndex(CookbookTable.COOKBOOK_COLUMN_RECIPE_NAME)));
		recipewindow.setMethod(c.getString(c.getColumnIndex(CookbookTable.COOKBOOK_COLUMN_RECIPE_METHOD)));
		
		String[] projection_ingredients_show={
				IngredientTable._ID,
				IngredientTable.INGREDIENT_COLUMN_RECIPE_ID,
				IngredientTable.INGREDIENT_COLUMN_INGREDIENT_NAME,
				IngredientTable.INGREDIENT_COLUMN_AMOUNT
		};
		
		String where_ingredients =IngredientTable.INGREDIENT_COLUMN_RECIPE_ID+" = "+currentSelectedRecipeId;
		
		String orderByName_ingredient = IngredientTable.INGREDIENT_COLUMN_INGREDIENT_NAME + " DESC";
		
		Cursor d = PantryBase.query(IngredientTable.INGREDIENT_TABLE_NAME, projection_ingredients_show, where_ingredients, null, null, null, orderByName_ingredient);
		d.moveToFirst();
		String[] ingredients = new String[d.getCount()*2];
		int i=0;
		while (!d.isAfterLast()){
			ingredients[i]=d.getString(d.getColumnIndex(IngredientTable.INGREDIENT_COLUMN_INGREDIENT_NAME));
			ingredients[i+1]=d.getString(d.getColumnIndex(IngredientTable.INGREDIENT_COLUMN_AMOUNT));
			i= i+2;
			d.moveToNext();
		}
		
		recipewindow.setIngredients(ingredients);
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction().replace(R.id.container, recipewindow, "recipe_show");
		trans.addToBackStack(null);
		trans.commit();
		
	}
//********************************************************************************************************************************************	
// Recipe entry
//*********************************************************************************************************************************************
	public void openAddView(View view){
		RecipeAddFragment AddDialog = new RecipeAddFragment();
		AddDialog.show(getSupportFragmentManager(),"add_recipe");
		
	}


	@SuppressLint("DefaultLocale")
	@Override
	/**
	 *  Submit a recipe, entered through the apps interfaces to the database
	 *  
	 *  args: a Bundle object that needs to contain at least fields with keys
	 * 		RECIPE_TITLE, RECIPE_INGREDIENTS and RECIPE_METHOD.
	 * 		content of theses fields has to be accessible as string
	 */
	public void submitRecipe(Bundle args) {
	
		//		if(!args.containsKey("RECIPE_TITLE") || !args.containsKey("RECIPE_INGREDIENTS")|| !args.containsKey("RECIPE_METHOD")){}

		// recipe table

		ContentValues newRecipeRow = new ContentValues();
		newRecipeRow.put(CookbookTable.COOKBOOK_COLUMN_RECIPE_NAME, (args.getString(RECIPE_TITLE)).toLowerCase());
		newRecipeRow.put(CookbookTable.COOKBOOK_COLUMN_RECIPE_METHOD,args.getString(RECIPE_METHOD));
		double recipe_id = PantryBase.insert(CookbookTable.COOKBOOK_TABLE_NAME, null, newRecipeRow);
		// ingredients table
		
		// parse ingredient string
		// current expected format: name of ingredient amount
		// ingredient names can not have digits 0-9 in them.
		String allIngredients=args.getString(RECIPE_INGREDIENTS);
		
		String[] splitIngredients=allIngredients.split("\\s",-1);
		// loop and enter into database
		int i = 0;
		int end= splitIngredients.length;
		String iName="";
		while (i< end){
			String curr = splitIngredients[i];
			if (curr.matches("[0-9]+")){
				// found an amount, thus must be finished with ingredient
				ContentValues newIngRow = new ContentValues();
				newIngRow.put(IngredientTable.INGREDIENT_COLUMN_INGREDIENT_NAME,iName.toLowerCase());
				newIngRow.put(IngredientTable.INGREDIENT_COLUMN_AMOUNT, curr);
				newIngRow.put(IngredientTable.INGREDIENT_COLUMN_RECIPE_ID, recipe_id);
				PantryBase.insert(IngredientTable.INGREDIENT_TABLE_NAME, null, newIngRow);
				// update typical amount of ingredients
				
				String sql_avg_query = "SELECT AVG("+
						IngredientTable.INGREDIENT_COLUMN_AMOUNT+") FROM (SELECT * FROM "+
						IngredientTable.INGREDIENT_TABLE_NAME+" WHERE "+
						IngredientTable.INGREDIENT_COLUMN_INGREDIENT_NAME+" = '"+ iName + "')";
				

				Cursor avg_cursor = PantryBase.rawQuery(sql_avg_query, null);
				double typical;
				if (avg_cursor.moveToFirst()){
					typical = avg_cursor.getDouble(0);
				}
				else{
					
					// TODO make sure this is the correct value for computation of metric
					typical = Double.POSITIVE_INFINITY;
				}
				avg_cursor.close();
				ContentValues updateToTypical= new ContentValues();
				updateToTypical.put(StorageTable.STORAGE_COLUMNS_TYPICAL_AMOUNT, typical);
				String whereClause = StorageTable.STORAGE_COLUMN_INGREDIENT+"=?";
				String [] whereArgs = {iName};
				PantryBase.update(StorageTable.STORAGE_TABLE_NAME, updateToTypical, whereClause, whereArgs);
					
				iName="";
				i++;
			}
			else{
				iName=iName+curr;
				i++;
			}
		}
		
		String notify = "Recipe added";
		Toast processing = Toast.makeText(getApplicationContext(), notify, Toast.LENGTH_LONG);
		processing.show();
		



	}



//************************************************************************************************************************************
// Meal plan
//*************************************************************************************************************************************



	@Override
	public void addToMealPlan(View view, Bundle args) {
		 // selection dialog for which day
		DayPickerDialog selector = new DayPickerDialog();
		selector.show(getSupportFragmentManager(), "day_selector");
	
		
	}
	
	
	@Override
	public void daySelected(String day) {
		currentDay=day;
		// this is ugly, but just to try whether it works
		if (currentDay==""){
			try {
				throw new IOException("current day was not set sucessfully");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		ContentValues addedToMealPlan = new ContentValues();
		addedToMealPlan.put(MealPlanTable.MEAL_PLAN_COLUMN_DAY, currentDay);
		addedToMealPlan.put(MealPlanTable.MEAL_PLAN_COLUMN_RECIPE_ID,currentSelectedRecipeId);		
		PantryBase.insert(MealPlanTable.MEAL_PLAN_TABLE_NAME, null, addedToMealPlan);
	
	}
	
	
	public void openMealPlanView(View view){
		MealPlanFragment Plan = new MealPlanFragment();
		
		// TODO research whether there is an option to do this via the query() method

		String sql_join_recipe_plan = "SELECT * FROM "+CookbookTable.COOKBOOK_TABLE_NAME+" INNER JOIN "+ MealPlanTable.MEAL_PLAN_TABLE_NAME+
				" ON "+ CookbookTable.COOKBOOK_TABLE_NAME+"."+CookbookTable._ID+" = "+MealPlanTable.MEAL_PLAN_COLUMN_RECIPE_ID;
		Cursor c = PantryBase.rawQuery(sql_join_recipe_plan, null);
	//	Cursor c = PantryBase.query(MealPlanTable.MEAL_PLAN_TABLE_NAME);
	
		SimpleCursorAdapter MealPlanAdapter = new SimpleCursorAdapter(
				this, 
				R.layout.two_item_row, 
				c, 
				//
				new String [] {MealPlanTable.MEAL_PLAN_COLUMN_DAY,CookbookTable.COOKBOOK_COLUMN_RECIPE_NAME}, 
				new int[] {R.id.two_item_row_item1,R.id.two_item_row_item2}, 
				//
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
				);
		Plan.setListAdapter(MealPlanAdapter);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
		.replace(R.id.container, Plan,"mealPlan");
		transaction.addToBackStack(null);
		transaction.commit();
		
	}

	
	
	
	public void removeMealfromMealPlan(String[] recipe_id){

		
		// remove recipes from the mealplan database table
		
		
	}
	
	
	public void cookMeal(String[]recipe_id){
		// get selected recipes
		
		// remove ingredients from database
		
		// remove from mealplan
		
		
	}
	

	@Override
	public void suggestRecipes() {
		// compute current ingredient scores
		String sql_compute_recipeScore = "SELECT "+CookbookTable.COOKBOOK_COLUMN_RECIPE_NAME+", "
													+ IngredientTable.INGREDIENT_COLUMN_RECIPE_ID +",  recipe_score FROM "+
				CookbookTable.COOKBOOK_TABLE_NAME+ " JOIN "+
				" (SELECT "+CookbookTable._ID+", "+IngredientTable.INGREDIENT_COLUMN_RECIPE_ID+" , SUM(urgency_score) AS recipe_score "+
				"FROM "+IngredientTable.INGREDIENT_TABLE_NAME+" JOIN ( SELECT " +IngredientTable.INGREDIENT_COLUMN_INGREDIENT_NAME+" AS ing_name, "+
				StorageTable.STORAGE_COLUMN_AMOUNT +", MAX("+StorageTable.STORAGE_COLUMN_AMOUNT+" /( "+
				StorageTable.STORAGE_COLUMNS_TYPICAL_AMOUNT+" *(strftime('%Y%j',"+StorageTable.STORAGE_COLUMN_USEBY+")-strftime('%Y%j','now')))) AS urgency_score "+
				"FROM "+ StorageTable.STORAGE_TABLE_NAME+" GROUP BY "+IngredientTable.INGREDIENT_COLUMN_INGREDIENT_NAME+" )"+
				"ON "+IngredientTable.INGREDIENT_TABLE_NAME+"."+IngredientTable.INGREDIENT_COLUMN_INGREDIENT_NAME+" ==  ing_name "+
				"GROUP BY "+IngredientTable.INGREDIENT_COLUMN_RECIPE_ID+")"+
				"ON "+CookbookTable.COOKBOOK_TABLE_NAME+"."+CookbookTable._ID+"=="+IngredientTable.INGREDIENT_COLUMN_RECIPE_ID;
		
		
		Cursor d = PantryBase.rawQuery(sql_compute_recipeScore, null);
//		SimpleCursorAdapter suggestions = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, d,
//		        new String[] { CookbookTable.COOKBOOK_COLUMN_RECIPE_NAME }, new int[] { android.R.id.text1 },CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.suggestListTitle);
//		builder.setAdapter(suggestions, new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		
		builder.setMultiChoiceItems(d, null, CookbookTable.COOKBOOK_COLUMN_RECIPE_NAME, new OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				// TODO Auto-generated method stub
				
			}
		});
		AlertDialog suggestDialog = builder.create();
		suggestDialog.show();
		
		// TODO this might be dangerous, check if there is a better way.
		//suggestions.swapCursor(null);
		// close cursor to avoid leaks
		//d.close();
		
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
			View rootView = inflater.inflate(R.layout.fragment_access_recipe_list,
					container, false);
			return rootView;
		}
	}



	





}



// code ausgemustert
//String orderBy = StorageTable.STORAGE_COLUMN_INGREDIENT+" ASCEND";
//
//Cursor c = PantryBase.query(StorageTable.STORAGE_TABLE_NAME, null, null, null, null, null, orderBy);
//ContentValues ingredientScores = new ContentValues();// TODO make sure keys can not be set twice with this type of object
//int i =0;
//SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy");
//c.moveToFirst();
//Date currdate= Calendar.getInstance().getTime();
//String lastingredient = "";
//double lastscore = 0;
//double tmpscore=0;
//while (c.moveToNext()){
//	String curringredient = c.getString(c.getColumnIndex(StorageTable.STORAGE_COLUMN_INGREDIENT));
//	double curramount = c.getDouble(c.getColumnIndex(StorageTable.STORAGE_COLUMN_AMOUNT));
//	double typamount = c.getDouble(c.getColumnIndex(StorageTable.STORAGE_COLUMNS_TYPICAL_AMOUNT));
//	String ubdatestring = c.getString(c.getColumnIndex(StorageTable.STORAGE_COLUMN_USEBY));
//	double timeuntilUB = 0;
//	try {
//		Date ubdate = sdf.parse(ubdatestring);
//		timeuntilUB = ubdate.getTime() - currdate.getTime();
//		
//	} catch (ParseException e) {
//		// TODO make sure use-by date format is correct when user enters it
//		timeuntilUB= Double.POSITIVE_INFINITY;
//		e.printStackTrace();
//	}
//	double currscore = curramount / (typamount*timeuntilUB);
//	if (curringredient!=lastingredient || currscore > lastscore){
//		lastscore=currscore;
//	}
//	ingredientScores.put(curringredient, lastscore);
//}
//
//
//// compute recipe scores
