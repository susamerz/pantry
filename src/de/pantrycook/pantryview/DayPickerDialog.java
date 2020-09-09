package de.pantrycook.pantryview;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DayPickerDialog extends DialogFragment {
	
	OnWeekdaySelectListener mListener;
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (OnWeekdaySelectListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement OnWeekdaySelectListener");
        }
	}


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(R.string.pick_day)
	           .setItems( R.array.weekdays_abrev , new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   String selDay=getResources().getStringArray(R.array.weekdays_abrev)[which];
	            	   mListener.daySelected(selDay);
	           }
	    });
	    return builder.create();
	}
	

}
