package com.example.papersoccer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class ClientBoard extends Thread {

	private final int port;
	
	private Socket server;
	
	private DataInputStream rd;
	
	private DataOutputStream wr;
	
	private boolean running;
	
	private SurfaceHolder surfaceHolder;

	private Board board;
	
	public static final String TAG = "tag";

	public ClientBoard(int port, SurfaceHolder surfaceHolder, Board board) {
		this.port = port;
		this.surfaceHolder = surfaceHolder;
		this.board = board;
	}
	
	private void setupClient() {
		try {
			//
			server = new Socket(InetAddress.getByName("192.168.0.104"), port);
			//
			rd = new DataInputStream(server.getInputStream());
			wr = new DataOutputStream(server.getOutputStream());
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		}
	}
	
	private void closeResources() {
		
	}

	@Override
	public void run() {
		setupClient();
		Canvas canvas = null;
		while (true) {
			// update game state
			// render state to the screen
			try {
				canvas = this.surfaceHolder.lockCanvas(null);
				// recv
				board.pressed.x = rd.readInt();
				int x = 1;
				x = board.pressed.x;
				Log.wtf("tag", "____________");
				Log.wtf("tag", "# " + x);
				Log.wtf("tag", "____________");
				
				
				board.pressed.y = rd.readInt();
				board.draw(canvas);
				// send
				wr.writeInt(board.ball.x);
				wr.writeInt(board.ball.y);
				board.draw(canvas);
			} catch (Exception e) {
				Log.wtf("exception", "thread ex");
				e.printStackTrace();
			} finally {
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
}
