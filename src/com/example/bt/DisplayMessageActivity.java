package com.example.bt;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayMessageActivity extends Activity {
	DBAdapter myDb;
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.diaplaymsg_activity);
	       
	       
	        openDB();//open db
	        
	        Bundle b = new Bundle();
	        b = getIntent().getExtras();
	        long id = b.getLong("id");
	        
	       // Intent intent = getIntent();
	       // String value = intent.getStringExtra("key"); //if it's a string you stored.
	        Toast.makeText(this, String.valueOf(id), Toast.LENGTH_LONG).show();
	        
	       displayRecord(id);
	        }
	 
	 private void displayRecord(long idInDB) {
			Cursor cursor = myDb.getRow(idInDB);
			if (cursor.moveToFirst()) {
				long idDB = cursor.getLong(DBAdapter.COL_ROWID);
				String name = cursor.getString(DBAdapter.COL_NAME);
				String imagename = cursor.getString(DBAdapter.COL_IMAGENAME);
				String favColour = cursor.getString(DBAdapter.COL_DESCRIPTION);
				
				String message = "ID: " + idDB + "\n" 
						+ "Name: " + name + "\n"
						+ "Std#: " + imagename + "\n"
						+ "FavColour: " + favColour;
				Toast.makeText(DisplayMessageActivity.this, message, Toast.LENGTH_LONG).show();
				TextView tv1=(TextView) findViewById(R.id.textView1);
				tv1.setText(message);
				ImageView imgv=(ImageView) findViewById(R.id.imageView1);
				int imgid = getResources().getIdentifier("com.example.bt:drawable/" + imagename, null, null);
				imgv.setImageResource(imgid);
				//
			}
			cursor.close();
			
			closeDB();
		}
	 private void openDB() {
			myDb = new DBAdapter(this);
			myDb.open();
		}
		private void closeDB() {
			myDb.close();
		}
		@Override
		protected void onDestroy() {
			super.onDestroy();	
			closeDB();
		}
		@Override
		protected void onPause(){
	        super.onPause();
	        Toast.makeText(DisplayMessageActivity.this, "on pause", Toast.LENGTH_LONG).show();
	        closeDB();
		}
		
		@Override
		  public void onResume() {
		    	super.onResume();
		    	Toast.makeText(DisplayMessageActivity.this, "on resume", Toast.LENGTH_LONG).show();
		    	openDB();
		    }
		  @Override
		    protected void onRestart() {
		        // TODO Auto-generated method stub
		        super.onRestart();
		        Toast.makeText(DisplayMessageActivity.this, "on restart", Toast.LENGTH_LONG).show();
		    	
		      

		    }
		  @Override
		  public void startManagingCursor(Cursor c) {

		      // TODO Auto-generated method stub
		      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {

		      super.startManagingCursor(c); 

		      }
		  }
	 }



