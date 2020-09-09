package de.pantrycook.pantryview;

import de.pantrycook.pantryview.PantryStorageContract.CookbookTable;
import de.pantrycook.pantryview.StorageViewFragment.OnIngredientSelectedListener;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class RecipeListFragment extends ListFragment {
	
	OnRecipeSelectedListener mListener;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_show_recipe_list,
				container, false);
		return rootView;
	}
	
	 @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        try {
	            mListener = (OnRecipeSelectedListener) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString() + " must implement OnRecipeSelectedListener");
	        }
	    }
	 


	
	public void onListItemClick(ListView l, View v, int position, long id){
		//l.setItemChecked(position, true);
		// show a Dialog that lets user select whether xe wants to view the recipe or add the recipe to the meal plan
		Bundle args = new Bundle();
		SimpleCursorAdapter adp = (SimpleCursorAdapter) getListAdapter();
		Cursor item = (Cursor) adp.getItem(position);
		args.putInt(RecipeListActivity.RECIPE_ID,item.getInt(item.getColumnIndex(CookbookTable._ID)));
		mListener.showRecipeDialog(v, args);
		
		// copied from storageviwefragment, might be recyclable in parts
//		SimpleCursorAdapter adp = (SimpleCursorAdapter) getListAdapter();
//		Cursor item = (Cursor) adp.getItem(position);	
//		String Name= item.getString(item.getColumnIndex("ingredient_name"));
//		PantryViewActivity parentActivity = (PantryViewActivity) this.getActivity();
//		Bundle args = new Bundle();
//		args.putString(PantryViewActivity.ING_KEY, ingName);
//		parentActivity.openManualAdd(parentActivity.findViewById(R.id.container),args);
	}
	
	/*
	 * Interface to communicate with parent activity, must be implemented by any activity using the Storage View
	 */
	public interface OnRecipeSelectedListener{
		
		public void showRecipeDialog(View view,Bundle args);
	}

}
	

