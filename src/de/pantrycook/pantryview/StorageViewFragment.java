package de.pantrycook.pantryview;
import android.app.Activity;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import de.pantrycook.pantryview.R;

public class StorageViewFragment extends ListFragment {
		
		OnIngredientSelectedListener mListener;

		public StorageViewFragment(){}

		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
		}
		
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_storage_view,
					container, false);
			return rootView;
		}
		
		 @Override
		    public void onAttach(Activity activity) {
		        super.onAttach(activity);
		        try {
		            mListener = (OnIngredientSelectedListener) activity;
		        } catch (ClassCastException e) {
		            throw new ClassCastException(activity.toString() + " must implement OnIngredientSelectedListener");
		        }
		    }
		 
	

		
		public void onListItemClick(ListView l, View v, int position, long id){
			l.setItemChecked(position, true);
			// show a manual add fragment with the Ingredient pre-entered
			SimpleCursorAdapter adp = (SimpleCursorAdapter) getListAdapter();
			Cursor item = (Cursor) adp.getItem(position);
			String ingName= item.getString(item.getColumnIndex("ingredient_name"));
			PantryViewActivity parentActivity = (PantryViewActivity) this.getActivity();
			Bundle args = new Bundle();
			args.putString(PantryViewActivity.ING_KEY, ingName);
			parentActivity.openManualAdd(parentActivity.findViewById(R.id.container),args);
		}
		
		/*
		 * Interface to communicate with parent activity, must be implemented by any activity using the Storage View
		 */
		public interface OnIngredientSelectedListener{
			public void openManualAdd(View view,Bundle args);
		}

	}