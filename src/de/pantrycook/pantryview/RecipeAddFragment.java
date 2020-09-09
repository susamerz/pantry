package de.pantrycook.pantryview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RecipeAddFragment extends DialogFragment {
	OnRecipeSubmitListener mListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
            mListener = (OnRecipeSubmitListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnRecipeSubmitListener");
        }
		
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_recipe_add_single,
				container, false);
		Button b = (Button) rootView.findViewById(R.id.recipe_submit);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle recipedetails = new Bundle();
				View root=v.getRootView();
				EditText title_field=(EditText)root.findViewById(R.id.recipe_title);
				EditText ing_field=(EditText)root.findViewById(R.id.recipe_ingredients);
				EditText method_field=(EditText)root.findViewById(R.id.recipe_method);
				recipedetails.putString(RecipeListActivity.RECIPE_TITLE, title_field.getText().toString());
				recipedetails.putString(RecipeListActivity.RECIPE_INGREDIENTS, ing_field.getText().toString());
				recipedetails.putString(RecipeListActivity.RECIPE_METHOD, method_field.getText().toString());
				mListener.submitRecipe(recipedetails);
				dismiss();

				
			}
		});			
		
		return rootView;
	}
	
	




	@Override
	public void onResume() {
		super.onResume();
		
	}



	public interface OnRecipeSubmitListener{
		public void submitRecipe(Bundle args);
	}





}

