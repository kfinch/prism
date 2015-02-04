package util;

import java.io.Serializable;

/**
 * A representation of a vector in 2D space.
 * Generates the x and y magnitude from angle and absolute magnitude.
 * angle is in radians.
 * 
 * @author Kelton Finch
 */
public final class Vector2d implements Serializable {
	
	private static final long serialVersionUID = 131002731141050668L;
	
	public final double x, y;
	
	public Vector2d(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2d(Vector2d v){
		this.x = v.x;
		this.y = v.y;
	}
	
	public Vector2d(Point2d start, Point2d finish){
		this.x = finish.x - start.x;
		this.y = finish.y - start.y;
	}
	
	/**
	 * Compute vector's angle with respect to the origin
	 * @return vector's angle, in radians
	 */
	public double getAngle(){
		return Math.atan2(y, x);
	}
	
	/**
	 * Compute vector's magnitude
	 * @return vector's magnitude
	 */
	public double getMagnitude(){
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	/**
	 * Generates a new vector with the given angle and magnitude
	 * @param angle The new vector's angle
	 * @param magnitude The new vector's magnitude
	 */
	public static Vector2d vectorFromAngleAndMagnitude(double angle, double magnitude){
		return new Vector2d(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}
	
	public Vector2d afterSetAngle(double angle){
		return vectorFromAngleAndMagnitude(angle, getMagnitude());
	}
	
	public Vector2d afterSetMagnitude(double magnitude){
		return vectorFromAngleAndMagnitude(getAngle(), magnitude);
	}
	
	/**
	 * Generates the result of adding another vector to this vector
	 * @param add the vector to add
	 * @return the result of this + add
	 */
	public Vector2d afterAdd(Vector2d add){
		return new Vector2d(x + add.x, y + add.y);
	}
	
	/**
	 * Generates the result of subtracting another vector from this vector
	 * @param subtract the vector to subtract
	 * @return the result of this - add
	 */
	public Vector2d afterSubtract(Vector2d subtract){
		return new Vector2d(x - subtract.x, y - subtract.y);
	}
	
	/**
	 * Generates the result of scalar multiplying this vector
	 * @param mult the amount to scalar multiply this vector by
	 * @return the result of this * mult
	 */
	public Vector2d afterMultiply(double mult){
		return new Vector2d(x*mult, y*mult);
	}
	
	/**
	 * Generates the result of scalar dividing this vector
	 * @param divide the amount to scalar divide this vector by
	 * @return the result of this / divide
	 */
	public Vector2d afterDivide(double divide){
		return new Vector2d(x/divide, y/divide);
	}
	
	/**
	 * Computes the dot product of this vector with a supplied vector
	 * @param v The other vector to use in the dot product
	 * @return The dot product of this vector with v
	 */
	public double dot(Vector2d v){
		return (x * v.x) + (y * v.y);
	}
	
	/**
	 * Computes the result of normalizing this vector.
	 * @return A vector that is this vector after normalization
	 */
	public Vector2d afterNormalize(){
		double mag = getMagnitude();
		return new Vector2d(x/mag, y/mag);
	}
	
	public Vector2d vectorProjection(double angle){
		double resultMag = getMagnitude() * Math.cos(getAngle() - angle);
		return Vector2d.vectorFromAngleAndMagnitude(angle, resultMag);
	}
	
	public Vector2d vectorRejection(double angle){
		return afterSubtract(vectorProjection(angle));
	}
	
	/**
	 * Gives the smallest angle required to get from angle start to angle finish
	 * @param start The starting angle (between -pi and pi)
	 * @param finish The finishing angle (between -pi and pi)
	 * @return The smallest angle that can be added to start to get to finish (between -pi and pi)
	 */
	public static double deltaAngle(double start, double finish){
		double result = finish - start;
		double crossUp = finish - start - 2*Math.PI;
		double crossDown = finish - start + 2*Math.PI;
		if(Math.abs(result) > Math.abs(crossUp))
			result = crossUp;
		if(Math.abs(result) > Math.abs(crossDown))
			result = crossDown;
		return result;
	}
	
	@Override
	public String toString(){
		return "{" + x + "," + y + "}";
	}
}
