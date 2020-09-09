package de.pantrycook.pantryview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

//inner subclass to open the apps database
		public class OpenDatabaseTask extends AsyncTask< Integer, Integer, SQLiteDatabase>{
			
			Context mContext;
			
			public OpenDatabaseTask(Context context) {
				mContext = context;
			}


			
			
			
			public void setContext(Context con){
				mContext=con;
			}
			
			

			@Override
			protected SQLiteDatabase doInBackground(Integer... params) {
			PantryDbHelper pDbHelper = new PantryDbHelper(mContext.getApplicationContext(),PantryDbHelper.DATABASE_NAME,null,PantryDbHelper.DATABASE_VERSION);
				SQLiteDatabase db = pDbHelper.getWritableDatabase();
				return db;
			}
		}
	
	