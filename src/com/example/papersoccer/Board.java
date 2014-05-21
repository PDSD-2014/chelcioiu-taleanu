package com.example.papersoccer;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Board extends SurfaceView implements SurfaceHolder.Callback {

	protected Thread thread;
	public static final String TAG = "tag";
	private Bitmap bitmap;
	private int width;
	private int height;
	private static final int COLS = 10;
	private static final int ROWS = 12;
	private float lineLength;
	private float maxDist;
	private Point[][] boardPoints = new Point[ROWS - 1][COLS - 1];
	private Set<Line> playedLines = new HashSet<Line>();
	protected Point ball;
	protected Point pressed;
	private Runnable networkThread;
	public static SurfaceHolder holder;
	private boolean touched;
	private boolean isServer;

	public Board(Context context, int width, int height, boolean isServer) {
		super(context);
		// add callback
		getHolder().addCallback(this);

		this.width = width;
		this.height = height;
		this.isServer = isServer;
		holder = getHolder();
		lineLength = width / Board.COLS;
		maxDist = (float) (lineLength * Math.sqrt(2.1));

		// make the board focusable so it can handle events
		setFocusable(true);

		// init points
		for (int i = 1; i < Board.ROWS; i++) {
			final Point[] linePoints = new Point[COLS - 1];
			for (int j = 1; j < Board.COLS; j++) {
				Point start = new Point(j * width / Board.COLS, height / 2 - 5 * width / Board.COLS + (i - 1) * width
						/ Board.COLS);
				linePoints[j - 1] = start;
			}
			boardPoints[i - 1] = linePoints;
		}
		// init ball
		ball = boardPoints[ROWS / 2 - 1][COLS / 2 - 1];
		// init nw thread
	}
	
	public void setNetworkThread(Runnable networkThread) {
		this.networkThread = networkThread;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (isServer) {
			thread = new ServerBoard(32923, holder, this);
			thread.start();
		} else {
			thread = new ClientBoard(32923, holder, this);
			thread.start();			
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		while (retry) {
			try {

			} catch (Exception e) {
				Log.wtf("exception", "surfaceDestroyed exception");
				e.printStackTrace();
			}
		}
	}

	private void drawBoard(Canvas canvas, Paint paint) {
		// draw lines
		for (int i = 0; i < ROWS - 1; i++)
			for (int j = 0; j < COLS - 2; j++)
				canvas.drawLine(boardPoints[i][j].x, boardPoints[i][j].y, boardPoints[i][j + 1].x,
						boardPoints[i][j + 1].y, paint);
		// draw columns
		for (int i = 0; i < ROWS - 2; i++)
			for (int j = 0; j < COLS - 1; j++)
				canvas.drawLine(boardPoints[i][j].x, boardPoints[i][j].y, boardPoints[i + 1][j].x,
						boardPoints[i + 1][j].y, paint);
		// up gate
		canvas.drawLine(4 * lineLength, height / 2 - 5 * lineLength, 4 * lineLength, height / 2 - 5 * lineLength
				- lineLength, paint);
		canvas.drawLine(6 * lineLength, height / 2 - 5 * lineLength, 6 * lineLength, height / 2 - 5 * lineLength
				- lineLength, paint);
		canvas.drawLine(4 * lineLength, height / 2 - 5 * lineLength - lineLength, 6 * lineLength, height / 2 - 5
				* lineLength - lineLength, paint);
		// down gate
		canvas.drawLine(4 * lineLength, height / 2 + 5 * lineLength, 4 * lineLength, height / 2 + 5 * lineLength
				+ lineLength, paint);
		canvas.drawLine(6 * lineLength, height / 2 + 5 * lineLength, 6 * lineLength, height / 2 + 5 * lineLength
				+ lineLength, paint);
		canvas.drawLine(4 * lineLength, height / 2 + 5 * lineLength + lineLength, 6 * lineLength, height / 2 + 5
				* lineLength + lineLength, paint);
		// draw the moves
		for (Line line : playedLines) {
			paint.setColor(Color.RED);
			canvas.drawLine(line.getStart().x, line.getStart().y, line.getStop().x, line.getStop().y, paint);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		canvas.drawColor(Color.WHITE);// To make background

		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(7);

		drawBoard(canvas, paint);

		if (touched) {
			Line ballToPressed = new Line(ball, pressed);
			Line pressedToBall = new Line(pressed, ball);
			if (distBetween(ball, pressed) <= maxDist && !playedLines.contains(ballToPressed)
					&& !playedLines.contains(pressedToBall)) {
				playedLines.add(ballToPressed);
				canvas.drawLine(ball.x, ball.y, pressed.x, pressed.y, paint);
				ball = pressed;
				pressed = null;
			}
			touched = false;
		}
	}
	
	public double distBetween(Point p1, Point p2) {
		return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
	}

	public Point getClosestPoint(Point point) {
		Point closest = null;
		double min = Double.MAX_VALUE;
		for (Point[] points : boardPoints) {
			for (Point other : points) {
				double dist = distBetween(point, other);
				if (min > dist) {
					min = dist;
					closest = other;
				}
			}
		}
		return closest;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Point screenPoint = new Point((int) event.getX(), (int) event.getY());
			pressed = getClosestPoint(screenPoint);
			touched = true;
		}
		return false;
	}
}
