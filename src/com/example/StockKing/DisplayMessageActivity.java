package com.example.StockKing;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.content.Intent;
import android.os.Build;

public class DisplayMessageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	    // Get the message from the intent
	    Intent intent = getIntent();
	    String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
	    

	    // Create the text view
	    TextView textView = new TextView(this);
	    textView.setTextSize(100);
	    textView.setText(message);

	    // Set the text view as the activity layout
	    setContentView(textView);
	}
}
