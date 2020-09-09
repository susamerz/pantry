package de.pantrycook.pantryview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class RecipeDisplayFragment extends Fragment {
	
	private String titleString="not set";
	private String methodString="not set";
	private String [] ingsStrings= {"ingredient not set", "NAN"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_recipe_display,
				container, false);
		((TextView)rootView.findViewById(R.id.show_recipe_title)).setText(titleString);
		// create an adapter for the grid view from the ings array
				ArrayAdapter<String> adpt = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ingsStrings);
				// set the adapter
				GridView v = (GridView)rootView.findViewById(R.id.show_recipe_ing);
				v.setAdapter(adpt);
		//		v.setVisibility(View.VISIBLE);
				((TextView)rootView.findViewById(R.id.show_recipe_method)).setText(methodString);
		return rootView;
	}
	
	
	public void setTitle(String title){
		titleString=title;
	}
	
	public void setIngredients(String[] ings){
		
		ingsStrings=ings;
	}
	public void setMethod(String method){
		methodString=method;
		
	}
	

}
