package util;

public final class Point2d {
	
	public final double x, y;
	
	public Point2d(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public Point2d(Point2d p) {
		this.x = p.x;
		this.y = p.y;
	}

	public double distanceTo(Point2d p){
		return Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2));
	}
	
	public Point2d afterTranslate(Vector2d translateVector){
		return new Point2d(x + translateVector.x, y + translateVector.y);
	}
	
	@Override
	public boolean equals(Object o){
		Point2d p = (Point2d)o;
		return ((x == p.x) && (y == p.y));
	}
	
	@Override
	public String toString(){
		return "(" + x + "," + y + ")";
	}
}
