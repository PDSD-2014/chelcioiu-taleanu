package com.example.server_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

public class Server implements Runnable {
	/**
	 * Tag used in logger.
	 */
	private static final String TAG = "tag";
	/**
	 * Socket used to accept connections.
	 */
	private ServerSocket in;
	
	public Server(int port) {
		try {
			// Beware: Only privileged users can use ports below 1023.
			in = new ServerSocket(port);
		} catch (IOException e) {
			Log.e(TAG, "Cannot create socket. Due to: " + e.getMessage());
		}
	}
	

	public void run () {
		String recvMsg = "";
		
		Socket incomingRequest = null;
		try {
			// Wait in blocked state for a request.
			incomingRequest = in.accept();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		
		while(true) {
			// When accept() returns a new request was received.
			// We use the incomingRequest socket for I/O
			Log.d(TAG, "New request from: " + incomingRequest.getInetAddress());

			// Get its associated OutputStream for writing.
			OutputStream responseStream = null;
			try {
				responseStream = incomingRequest.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "Cannot get outputstream.");
			}

			// Wrap it with a PrinStream for convenience.
			PrintStream writer = null;
			writer = new PrintStream(responseStream);
			synchronized (Board.pressed) {
				try {
					Log.e(TAG, "Waiting ...");
					Board.pressed.wait();
					Log.e(TAG, "--------------");
				} catch (InterruptedException e) {
					Log.e(TAG, e.getMessage(), e);
				}

				Log.e(TAG, "Server: " + Board.pressed.toString());
				String pressedStr = Board.pressed.toString();
				writer.print(pressedStr);
			}
			
			try {
				BufferedReader responseReader = new BufferedReader(new InputStreamReader(incomingRequest.getInputStream()));
				recvMsg = responseReader.readLine();
				Log.e(TAG, recvMsg);
				if (recvMsg.equals("-")){
					incomingRequest.close();
				}
			} catch (IOException e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
	}
	
	
}
