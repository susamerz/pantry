package de.pantrycook.pantryview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * A fragment containing the entry mask for manual entry of ingredients.
 */
public class ManualEntryFragment extends Fragment {

	public ManualEntryFragment() {
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_manual_change_ingredient,
				container, false);
		return rootView;
	}
	
	
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		Bundle arg = getArguments();
		EditText t_field = (EditText) getActivity().findViewById(R.id.storage_ingredient);
		String text_content=arg.getString(PantryViewActivity.ING_KEY);
		if (text_content.equalsIgnoreCase("Enter Ingredient")){
			t_field.setHint(text_content);
		}
		else{t_field.setText(text_content);}
		
		
	}
	
}
