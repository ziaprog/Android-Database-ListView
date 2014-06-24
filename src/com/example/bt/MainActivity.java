package com.example.bt;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


/**
 * Demo application to show how to use the 
 * built-in SQLite database with a cursor to populate
 * a ListView.
 */
public class MainActivity extends Activity {
	private static final String EXTRA_MESSAGE = null;
	int[] imageIDs = {
		
			};
	int nextImageIndex = 0;
	
	DBAdapter myDb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		openDB();
		populateListViewFromDB();
		registerListClickCallback();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();	
		closeDB();
	}
	@Override
	protected void onPause(){
        super.onPause();
        Toast.makeText(MainActivity.this, "on pause", Toast.LENGTH_LONG).show();
        closeDB();
	}
	@Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        Toast.makeText(MainActivity.this, "on restart", Toast.LENGTH_LONG).show();
    	cursor.requery();
    }
	@Override
	public void startManagingCursor(Cursor c) {

	    // TODO Auto-generated method stub
	    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {

	    super.startManagingCursor(c); 

	    }
	}
	@Override
	  public void onResume() {
	    	super.onResume();
	    	Toast.makeText(MainActivity.this, "on resume", Toast.LENGTH_LONG).show();
	    	openDB();
	    }
	
	private void openDB() {
		myDb = new DBAdapter(this);
		myDb.open();
	}
	private void closeDB() {
		myDb.close();
	}

	/* 
	 * UI Button Callbacks
	 */
	/*public void onClick_AddRecord(View v) {
		int imageId = imageIDs[nextImageIndex];
		nextImageIndex = (nextImageIndex + 1) % imageIDs.length;

		// Add it to the DB and re-draw the ListView
		myDb.insertRow("Jenny" + nextImageIndex, imageId, "Green");
		
		populateListViewFromDB();
	}*/
	public void onClick_AddSampleRecord(View v) {
		
		EditText nameTxt=(EditText)findViewById(R.id.editTextName);
		EditText numTxt=(EditText)findViewById(R.id.editTextNumber);
		EditText descTxt=(EditText)findViewById(R.id.editTextDesc);
		
		
		if( nameTxt.getText().toString().trim().equals("") )
		 {    
		   nameTxt.setError( "First name is required!" );

		    nameTxt.setHint("please enter username");
		    
		 } 
		else if(numTxt.getText().toString().trim().equals("") )
			 {    
			numTxt.setError( "Number is required!" );

			numTxt.setHint("please enter a number");
		}
		else if(descTxt.getText().toString().trim().equals("") )
		 {    
			descTxt.setError( "Color is required!" );

			descTxt.setHint("please enter a color");
		 }
	
		else {
		
		
		// Add it to the DB and re-draw the ListView
		myDb.insertRow(nameTxt.getText().toString(),numTxt.getText().toString(), descTxt.getText().toString());
		
		populateListViewFromDB();
		
		 }
	}
	public void onClick_ClearAll(View v) {
		myDb.deleteAll();
		populateListViewFromDB();
	}

	Cursor cursor ;
	private void populateListViewFromDB() {
		 cursor = myDb.getAllRows();
		
		// Allow activity to manage lifetime of the cursor.
		// DEPRECATED! Runs on the UI thread, OK for small/short queries.
		startManagingCursor(cursor);
		
		// Setup mapping from cursor to view fields:
		String[] fromFieldNames = new String[] 
				{DBAdapter.KEY_NAME, DBAdapter.KEY_IMAGENAME, DBAdapter.KEY_DESCRIPTION, DBAdapter.KEY_IMAGENAME};
		int[] toViewIDs = new int[]
				{R.id.item_name,     R.id.item_icon,           R.id.item_favcolour,     R.id.item_studentnum};
		
		// Create adapter to may columns of the DB onto elemesnt in the UI.
		SimpleCursorAdapter myCursorAdapter = 
				new SimpleCursorAdapter(
						this,		// Context
						R.layout.item_layout,	// Row layout template
						cursor,					// cursor (set of DB records to map)
						fromFieldNames,			// DB Column names
						toViewIDs				// View IDs to put information in
						);
		
		// Set the adapter for the list view
		ListView myList = (ListView) findViewById(R.id.listViewFromDB);
		myList.setAdapter(myCursorAdapter);
	}
	
	private void registerListClickCallback() {
		ListView myList = (ListView) findViewById(R.id.listViewFromDB);
		myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, 
					int position, long idInDB) {

				updateItemForId(idInDB);
				displayToastForId(idInDB);
				
				launchNewActivity(idInDB);
			}
		});
	}
	
	private void updateItemForId(long idInDB) {
		Cursor cursor = myDb.getRow(idInDB);
		if (cursor.moveToFirst()) {
			long idDB = cursor.getLong(DBAdapter.COL_ROWID);
			String name = cursor.getString(DBAdapter.COL_NAME);
			String imagename = cursor.getString(DBAdapter.COL_IMAGENAME);
			String favColour = cursor.getString(DBAdapter.COL_DESCRIPTION);

			favColour += "!";
			myDb.updateRow(idInDB, name, imagename, favColour);
		}
		cursor.close();
		populateListViewFromDB();		
	}
	
	private void displayToastForId(long idInDB) {
		Cursor cursor = myDb.getRow(idInDB);
		if (cursor.moveToFirst()) {
			long idDB = cursor.getLong(DBAdapter.COL_ROWID);
			String name = cursor.getString(DBAdapter.COL_NAME);
			int studentNum = cursor.getInt(DBAdapter.COL_IMAGENAME);
			String favColour = cursor.getString(DBAdapter.COL_DESCRIPTION);
			
			String message = "ID: " + idDB + "\n" 
					+ "Name: " + name + "\n"
					+ "Std#: " + studentNum + "\n"
					+ "FavColour: " + favColour;
			Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
		}
		cursor.close();
	}
	
	public void launchNewActivity(long idInDB) {
	    Intent intent = new Intent(MainActivity.this, DisplayMessageActivity.class);
	    intent.putExtra("id",idInDB); //Optional parameters
	    MainActivity.this.startActivity(intent);
	}
	
	
	
}










