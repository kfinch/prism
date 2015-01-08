package util;

public class GeometryUtils {
	
	public static double dist(double x1, double y1, double x2, double y2){
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
	}
	
	public static double dist(Point2d p1, Point2d p2){
		return dist(p1.x, p1.y, p2.x, p2.y);
	}
	
	/*
	 * Towers are assumed to be 2x2 square, given coords are from center of tower.
	 * It doesn't matter which of the two points is the tower and which is the point.
	 */
	public static double distFromTowerEdge(double x1, double y1, double x2, double y2){
		double xDiff = Math.abs(x1 - x2);
		double yDiff = Math.abs(y1 - y2);
		xDiff = (xDiff <= 1) ? 0 : xDiff - 1;
		yDiff = (yDiff <= 1) ? 0 : yDiff - 1;
		return Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
	}
	
	public static double distFromTowerEdge(Point2d p1, Point2d p2){
		return distFromTowerEdge(p1.x, p1.y, p2.x, p2.y);
	}
	
	// [math intensifies]
	public static double distPointToLine(double xp, double yp, double xl1, double yl1, double xl2, double yl2){
		Vector2d lineVec = new Vector2d(xl2-xl1, yl2-yl1);
		Vector2d p1Vec = new Vector2d(xp-xl1, yp-yl1);
		Vector2d p2Vec = new Vector2d(xp-xl2, yp-yl2);
		
		if(Math.signum(lineVec.dot(p1Vec)) == Math.signum(lineVec.dot(p2Vec))){
			return Math.min(dist(xp,yp,xl1,yl1), dist(xp,yp,xl2,yl2));
		}
		else{ //inbetween points, use the line to point formula
			return Math.abs((yl2-yl1)*xp - (xl2-xl1)*yp + xl2*yl1 - xl1*yl2) /
	               Math.sqrt(Math.pow(xl2-xl1, 2) + Math.pow(yl2-yl1, 2));
		}
	}
	
	public static double distPointToLine(Point2d p, Point2d l1, Point2d l2){
		return distPointToLine(p.x, p.y, l1.x, l1.y, l2.x, l2.y);
	}
	
}
