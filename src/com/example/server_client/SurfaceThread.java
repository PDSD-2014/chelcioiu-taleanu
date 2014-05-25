package com.example.server_client;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class SurfaceThread implements Runnable {

	private static final String TAG = "tag";
	private SurfaceHolder surfaceHolder;
	private Board board;
	
	public SurfaceThread(SurfaceHolder surfaceHolder, Board board) {
		this.surfaceHolder = surfaceHolder;
		this.board = board;
	}
	
	public void run () {
		Canvas canvas = null;
		while (true) {
			try {	
				//getting from screen point pressed
				
				canvas = this.surfaceHolder.lockCanvas(null);
				board.draw(canvas);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
			} finally {
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
	
}
