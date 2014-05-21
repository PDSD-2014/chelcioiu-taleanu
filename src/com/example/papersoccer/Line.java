package com.example.papersoccer;

import android.graphics.Point;

public class Line {
	private Point start;
	private Point stop;

	public Line(Point start, Point stop) {
		this.start = start;
		this.stop = stop;
	}

	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public Point getStop() {
		return stop;
	}

	public void setStop(Point stop) {
		this.stop = stop;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		result = prime * result + ((stop == null) ? 0 : stop.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Line other = (Line) obj;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		if (stop == null) {
			if (other.stop != null)
				return false;
		} else if (!stop.equals(other.stop))
			return false;
		return true;
	}

	
}
