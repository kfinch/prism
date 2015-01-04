package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class PaintableShapes {
	
	public double xLoc, yLoc;
	private List<Object> shapes;
	
	public PaintableShapes(double xLoc, double yLoc){
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		shapes = new ArrayList<Object>();
	}
	
	public void addFixedCircle(double xLoc, double yLoc, double radius, Color color){
		shapes.add(new Circle(xLoc, yLoc, radius, color, false));
	}
	
	public void addRotatableCircle(double xLoc, double yLoc, double radius, Color color){
		shapes.add(new Circle(xLoc, yLoc, radius, color, true));
	}
	
	public void addFixedPolygon(int nPoints, double[] xPoints, double[] yPoints, Color color){
		shapes.add(new Polygon(nPoints, xPoints, yPoints, color, false));
	}
	
	public void addRotatablePolygon(int nPoints, double[] xPoints, double[] yPoints, Color color){
		shapes.add(new Polygon(nPoints, xPoints, yPoints, color, true));
	}
	
	public void addFixedRectangle(double x1, double y1, double x2, double y2, Color color){
		shapes.add(new Rectangle(x1, y2, x2, y2, color));
	}
	
	public void addRotatableRectangle(double x1, double y1, double x2, double y2, Color color){
		double[] xPoints = {x1, x1, x2, x2};
		double[] yPoints = {y1, y2, y2, y1};
		addRotatablePolygon(4, xPoints, yPoints, color);
	}
	
	public void rotate(double radians){
		for(Object o : shapes){
			if(o instanceof Circle && ((Circle) o).rotatable){
				Circle c = (Circle) o;
				Vector2d vec = new Vector2d(c.xLoc, c.yLoc);
				vec.setAngle(vec.angle() + radians);
				c.xLoc = vec.x;
				c.yLoc = vec.y;
			}
			else if(o instanceof Polygon && ((Polygon)o).rotatable){
				Polygon p = (Polygon) o;
				Vector2d vec;
				for(int i=0; i<p.nPoints; i++){
					vec = new Vector2d(p.xPoints[i], p.yPoints[i]);
					vec.setAngle(vec.angle() + radians);
					p.xPoints[i] = vec.x;
					p.yPoints[i] = vec.y;
				}
			}
		}
	}
	
	public void setAngle(double radians){
		for(Object o : shapes){
			if(o instanceof Circle && ((Circle) o).rotatable){
				Circle c = (Circle) o;
				Vector2d vec = new Vector2d(c.origXLoc, c.origYLoc);
				vec.setAngle(vec.angle() + radians);
				c.xLoc = vec.x;
				c.yLoc = vec.y;
			}
			else if(o instanceof Polygon && ((Polygon)o).rotatable){
				Polygon p = (Polygon) o;
				Vector2d vec;
				for(int i=0; i<p.nPoints; i++){
					vec = new Vector2d(p.origXPoints[i], p.origYPoints[i]);
					vec.setAngle(vec.angle() + radians);
					p.xPoints[i] = vec.x;
					p.yPoints[i] = vec.y;
				}
			}
		}
	}
	
	public void paintShapes(Graphics2D g2d, int centerX, int centerY, int tileSize){
		for(Object o : shapes){
			if(o instanceof Circle){
				Circle c = (Circle) o;
				g2d.setColor(c.color);
				g2d.fillOval((int)(centerX + (c.xLoc - c.radius)*tileSize), (int)(centerY + (c.yLoc - c.radius)*tileSize),
						     (int)(c.radius*2*tileSize), (int)(c.radius*2*tileSize));
			}
			else if(o instanceof Polygon){
				Polygon p = (Polygon) o;
				int[] absXPoints = new int[p.nPoints];
				int[] absYPoints = new int[p.nPoints];
				for(int i=0; i<p.nPoints; i++){
					absXPoints[i] = (int) (centerX + p.xPoints[i]*tileSize);
					absYPoints[i] = (int) (centerY + p.yPoints[i]*tileSize);
				}
				g2d.setColor(p.color);
				g2d.fillPolygon(absXPoints, absYPoints, p.nPoints);
			}
			else if(o instanceof Rectangle){
				Rectangle r = (Rectangle) o;
				g2d.setColor(r.color);
				g2d.fillRect((int)(centerX + r.x1*tileSize), (int)(centerY + r.y1*tileSize),
						     (int)(centerX + (r.x2-r.x1)*tileSize), (int)(centerY + (r.y2-r.y1)*tileSize));
			}
		}
	}
}

class Circle {
	
	boolean rotatable;
	double xLoc, yLoc, radius;
	double origXLoc, origYLoc;
	Color color;
	
	public Circle(double xLoc, double yLoc, double radius, Color color, boolean rotatable){
		this.xLoc = xLoc;
		this.origXLoc = xLoc;
		this.yLoc = yLoc;
		this.origYLoc = yLoc;
		this.radius = radius;
		this.color = color;
		this.rotatable = rotatable;
	}
}

class Polygon {
	
	boolean rotatable;
	int nPoints;
	double[] xPoints;
	double[] yPoints;
	double[] origXPoints;
	double[] origYPoints;
	Color color;
	
	public Polygon(int nPoints, double[] xPoints, double[] yPoints, Color color, boolean rotatable){
		this.nPoints = nPoints;
		this.xPoints = xPoints;
		this.origXPoints = new double[nPoints];
		for(int i=0; i<nPoints; i++)
			origXPoints[i] = xPoints[i];
		this.yPoints = yPoints;
		this.origYPoints = new double[nPoints];
		for(int i=0; i<nPoints; i++)
			origYPoints[i] = yPoints[i];
		this.color = color;
		this.rotatable = rotatable;
	}
}

class Rectangle {
	
	double x1, y1, x2, y2;
	Color color;
	
	public Rectangle(double x1, double y1, double x2, double y2, Color color){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.color = color;
	}
}
