package de.pantrycook.pantryview;


import de.pantrycook.pantryview.PantryStorageContract.MealPlanTable;
import android.app.Activity;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class MealPlanFragment extends ListFragment {
	
	int [] selected_recipes = {};
	OnMealPlanActionListener mListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	
		try {
            mListener = (OnMealPlanActionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnMealPlanActionListener");
        }
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_meal_plan,
				container, false);
		
		// TODO get buttons and implement respective onClick
		Button suggestButton=(Button) rootView.findViewById(R.id.suggest_button);
		suggestButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				mListener.suggestRecipes();
			
			}
			
		});
		
		Button removeButton= (Button) rootView.findViewById(R.id.remove_meal);
		removeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String[] ids = getCheckedRecipeIDs(v);
				mListener.removeMealfromMealPlan(ids);
			}
		});
		
		Button cookButton = (Button) rootView.findViewById(R.id.cook_meal);
		cookButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String[] ids = getCheckedRecipeIDs(v);
				mListener.cookMeal(ids);
				
			}
		});		
		
		
		
		return rootView;
	}
	
	
	private String[] getCheckedRecipeIDs(View v){
		String[] chkedIDs= {};
		ListView l = (ListView )v.getRootView().findViewById(android.R.id.list);
		SparseBooleanArray chked=l.getCheckedItemPositions();
		for(int i =0; i<chked.size();i++){
		SQLiteCursor tmp = (SQLiteCursor)l.getAdapter().getItem(chked.keyAt(i));
		chkedIDs[i]=tmp.getString(tmp.getColumnIndex(MealPlanTable.MEAL_PLAN_COLUMN_RECIPE_ID));
		}
		return chkedIDs;
	}
	
	
	
	// TODO define interface for suggets recipes method, remove from meal plan method and cook meal method
	
public interface OnMealPlanActionListener{
	public void suggestRecipes();
	public void removeMealfromMealPlan(String[] recipe_ids);
	public void cookMeal(String[] recipe_ids);
		
	}
	

}
