package com.example.server_client;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.WindowManager;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	
	String greeting = "Hello world";
	String TAG = "tag";
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		Log.d(TAG, "onCreate."); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startGame(this.getApplicationContext());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void startGame(Context context) {
		// making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // set our Board as the View
		Display display = getWindowManager().getDefaultDisplay();	
        Point point = new Point();
        display.getSize(point);
 
        SurfaceView surfaceView = new Board(context, point.x, point.y);
		setContentView(surfaceView);
	}


}
