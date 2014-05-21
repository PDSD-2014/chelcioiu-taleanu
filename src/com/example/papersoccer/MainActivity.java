package com.example.papersoccer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {	
	Button serverButton;
	Button clientButton;
	private boolean isServer;
	private static Context context;
	private static final int PORT = 53123;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		MainActivity.context = getApplicationContext(); 
		clientButton = (Button)findViewById(R.id.b1);
		serverButton = (Button)findViewById(R.id.b2);
		
		serverButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				//start server;
				isServer = true;
				startGame();
			}
		});
		
		clientButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//start client;
				isServer = false;
				startGame();
			}
		});	
	}
	
	public void startGame() {
		// making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // set our Board as the View
		Display display = getWindowManager().getDefaultDisplay();	
        Point point = new Point();
        display.getSize(point);
 
        SurfaceView surfaceView = new Board(MainActivity.context, point.x, point.y, isServer);
        setContentView(surfaceView);
	}

}
