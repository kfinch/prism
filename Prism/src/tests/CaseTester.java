package tests;

import util.GeometryUtils;
import util.Vector2d;

public class CaseTester {

	public static void main(String args[]){
		dotProductTest();
	}
	
	public static void linePointDistTest(){
		System.out.println(GeometryUtils.distPointToLine(0, 0, -2, 2, 2, 2));
		System.out.println(GeometryUtils.distPointToLine(0, 0, 2, 0, 0, 2));
		System.out.println(GeometryUtils.distPointToLine(4, 2, 2, 0, 0, 2));
		
		System.out.println(GeometryUtils.distPointToLine(0, 1, 2, 2, 4, 4));
	}
	
	public static void dotProductTest(){
		Vector2d upLeft = new Vector2d(1,-1);
		Vector2d left = new Vector2d(1,0);
		Vector2d downLeft = new Vector2d(1,1);
		
		Vector2d attract = new Vector2d(4,4);
		
		System.out.println(upLeft.dot(attract));
		System.out.println(left.dot(attract));
		System.out.println(downLeft.dot(attract));
	}
	
}
