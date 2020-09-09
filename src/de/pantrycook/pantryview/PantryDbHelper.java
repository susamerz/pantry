package de.pantrycook.pantryview;

import de.pantrycook.pantryview.PantryStorageContract.CookbookTable;
import de.pantrycook.pantryview.PantryStorageContract.IngredientTable;
import de.pantrycook.pantryview.PantryStorageContract.MealPlanTable;
import de.pantrycook.pantryview.PantryStorageContract.StorageTable;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PantryDbHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 1; // increment if database schema changes
	public static final String DATABASE_NAME = "PantryStorage.db";
	
	private static final String TEXT_TYPE = " TEXT";
	private static final String NUM_TYPE = " REAL";
	private static final String COMMA_SEP = ",";
	
	
	// create commands 
	private static final String SQL_CREATE_STORAGE =
	    "CREATE TABLE " + StorageTable.STORAGE_TABLE_NAME + " (" +
	    		StorageTable._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
	    		StorageTable.STORAGE_COLUMN_INGREDIENT + TEXT_TYPE + COMMA_SEP +
	    StorageTable.STORAGE_COLUMN_AMOUNT + NUM_TYPE + COMMA_SEP +
	    StorageTable.STORAGE_COLUMN_USEBY + TEXT_TYPE + COMMA_SEP +
	    StorageTable.STORAGE_COLUMNS_TYPICAL_AMOUNT + NUM_TYPE +
	    " )";
	
	private static final String SQL_CREATE_COOKBOOK =
		    "CREATE TABLE " + CookbookTable.COOKBOOK_TABLE_NAME + " (" +
		    		CookbookTable._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
		    		CookbookTable.COOKBOOK_COLUMN_RECIPE_NAME + TEXT_TYPE + COMMA_SEP +
		    CookbookTable.COOKBOOK_COLUMN_RECIPE_METHOD + TEXT_TYPE +
		    " )";
	
	private static final String SQL_CREATE_INGREDIENTS =
		    "CREATE TABLE " + IngredientTable.INGREDIENT_TABLE_NAME + " (" +
		    		IngredientTable._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
		    		IngredientTable.INGREDIENT_COLUMN_INGREDIENT_NAME + TEXT_TYPE + COMMA_SEP +
		    IngredientTable.INGREDIENT_COLUMN_AMOUNT + NUM_TYPE + COMMA_SEP +
		    IngredientTable.INGREDIENT_COLUMN_RECIPE_ID + " INTEGER REFERENCES" + CookbookTable._ID +  // n.b. sqlite documentation states that foreign keys must be referencing a named field. this might be a problem with the _id field
		   
		    " )";
	
	private static final String SQL_CREATE_MEALPLAN =
			"CREATE TABLE "+ MealPlanTable.MEAL_PLAN_TABLE_NAME+" ("+
					MealPlanTable._ID +" INTEGER PRIMARY KEY"+ COMMA_SEP +
					MealPlanTable.MEAL_PLAN_COLUMN_DAY+TEXT_TYPE+COMMA_SEP+
					MealPlanTable.MEAL_PLAN_COLUMN_RECIPE_ID+" INTEGER REFERENCES" + CookbookTable._ID +
					 " )";

	// delete commands
	private static final String SQL_DELETE_STORAGE =
	    "DROP TABLE IF EXISTS " + StorageTable.STORAGE_TABLE_NAME;
	private static final String SQL_DELETE_COOKBOOK =
		    "DROP TABLE IF EXISTS " + CookbookTable.COOKBOOK_TABLE_NAME;
	private static final String SQL_DELETE_INGREDIENTS =
		    "DROP TABLE IF EXISTS " + IngredientTable.INGREDIENT_TABLE_NAME;
	private static final String SQL_DELETE_MEALPLAN =
			"DROP TABLE IF EXISTS "+ MealPlanTable.MEAL_PLAN_TABLE_NAME;
	

	public PantryDbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public PantryDbHelper(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_STORAGE);
		db.execSQL(SQL_CREATE_COOKBOOK);
		db.execSQL(SQL_CREATE_INGREDIENTS);
		db.execSQL(SQL_CREATE_MEALPLAN);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO get current content of database
		
		// delete old database
		db.execSQL(SQL_DELETE_STORAGE);
		db.execSQL(SQL_DELETE_INGREDIENTS);
		db.execSQL(SQL_DELETE_COOKBOOK);
		db.execSQL(SQL_DELETE_MEALPLAN);
		// create new database
		onCreate(db);
		
		// TODO repopulate database
		
		

	}

}
