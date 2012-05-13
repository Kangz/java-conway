package util;

public class Position2D {
	public final int x, y;
	
	public boolean equals(Object o) {
		if (! (o instanceof Position2D) ) {
			return false;
		}
		Position2D p = (Position2D) o;
		return p.x == x && p.y == y;
	}
	
	public int hashCode() {
		return x + 31*y;
	}
	
	public Position2D(int x_,  int y_) {
		x = x_;
		y = y_;
	}
}