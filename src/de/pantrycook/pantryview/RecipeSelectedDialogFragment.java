package de.pantrycook.pantryview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

public class RecipeSelectedDialogFragment extends DialogFragment {
	
	OnRecipeOptionSelectedListener mListener;
	int CallingRecipeId = 0; // holds the id of the recipe that was selected

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
            mListener = (OnRecipeOptionSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnRecipeOptionSelectedListener");
        }
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(R.array.recipe_dialog, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // The 'which' argument contains the index position
            // of the selected item
            	switch (which) {
				case 0:
					mListener.showRecipe(getView(), null);
					break;
				case 1:
					mListener.addToMealPlan(getView(), null);
					break;

				default:
					break;
				}
        }
 });
 return builder.create();
	}
	
	public void setCallingId(int id) {
		CallingRecipeId=id;
		
	}
	
	public int getCallingId(){
		return CallingRecipeId;
	}
	
	public interface OnRecipeOptionSelectedListener{
		
		public void addToMealPlan(View view,Bundle args);
		
		public void showRecipe(View view,Bundle args);
	}

	
	
	

}
