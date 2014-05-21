package com.example.papersoccer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class ServerBoard extends Thread {

	private final int port;

	private ServerSocket server;

	private Socket client;

	private DataInputStream rd;

	private DataOutputStream wr;

	private boolean running;
	
	private SurfaceHolder surfaceHolder;

	private Board board;
	
	public static final String TAG = "tag";
	
	public ServerBoard(int port, SurfaceHolder surfaceHolder, Board board) {
		this.port = port;
		this.surfaceHolder = surfaceHolder;
		this.board = board;
	}

	private void setupServer() {
		try {
			//
			server = new ServerSocket(port);
			//
			client = server.accept();
			Log.wtf(TAG, "Client connected " + client.toString());
			//
			rd = new DataInputStream(client.getInputStream());
			wr = new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
			
		} finally {
			Log.wtf(TAG, "client = " + client.toString());
		}
	}

	private void closeResources() {
		try {
			if (rd != null)
				rd.close();
			if (wr != null)
				wr.close();
			if (client != null)
				client.close();
			if (server != null)
				server.close();
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		setupServer();
		Canvas canvas = null;
		while (running) {
			// update game state
			// render state to the screen
			try {
				canvas = this.surfaceHolder.lockCanvas(null);
				// send
				wr.writeInt(board.ball.x);
				wr.writeInt(board.ball.y);
				board.draw(canvas);
				// recv
				board.pressed.x = rd.readInt();
				board.pressed.y = rd.readInt();
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
