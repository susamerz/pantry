package de.pantrycook.pantryview;

import android.provider.BaseColumns;

public final class PantryStorageContract {

	public PantryStorageContract() {
		// empty constructor, because this is only the contract class and should not be instantiated
	}

	
	
	public static abstract class StorageTable implements BaseColumns{
		
		public static final String STORAGE_TABLE_NAME = "storage_table";
// no need for id, because automatic ids from superclass		public static final String STORAGE_COLUMN_INGREDIENT_ID = "ingredient_id";
		public static final String STORAGE_COLUMN_INGREDIENT = "ingredient_name";
		public static final String STORAGE_COLUMN_AMOUNT = "stored_amount";
		public static final String STORAGE_COLUMN_USEBY = "use_by_date";
		public static final String STORAGE_COLUMNS_TYPICAL_AMOUNT = "typical_used_amount";
	}
	
	public static abstract class CookbookTable implements BaseColumns{
		
		public static final String COOKBOOK_TABLE_NAME = "cookbook_table";
//		public static final String COOKBOOK_COLUMN_RECIPE_ID = "recipe_id";
		public static final String COOKBOOK_COLUMN_RECIPE_NAME = "recipe_name";
		public static final String COOKBOOK_COLUMN_RECIPE_METHOD = "method";
	}
	public static abstract class IngredientTable implements BaseColumns{
		
		public static final String INGREDIENT_TABLE_NAME = "ingredient_table";
//		public static final String Ingredient_COLUMN_INGREDIENT_ID = "ingredient_id";
		public static final String INGREDIENT_COLUMN_INGREDIENT_NAME = "ingredient_name";
		public static final String INGREDIENT_COLUMN_AMOUNT = "ingredient_amount";
		public static final String INGREDIENT_COLUMN_RECIPE_ID = "recipe_id";
		
		
	}
	
	public static abstract class MealPlanTable implements BaseColumns{
		public static final String MEAL_PLAN_TABLE_NAME= "meal_plan_table";
		public static final String MEAL_PLAN_COLUMN_DAY="weekday";
		public static final String MEAL_PLAN_COLUMN_RECIPE_ID = "recipe_id";
	}
}
