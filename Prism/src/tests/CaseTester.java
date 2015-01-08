package tests;

import util.GeometryUtils;

public class CaseTester {

	public static void main(String args[]){
		linePointDistTest();
	}
	
	public static void linePointDistTest(){
		System.out.println(GeometryUtils.distPointToLine(0, 0, -2, 2, 2, 2));
		System.out.println(GeometryUtils.distPointToLine(0, 0, 2, 0, 0, 2));
		System.out.println(GeometryUtils.distPointToLine(4, 2, 2, 0, 0, 2));
		
		System.out.println(GeometryUtils.distPointToLine(0, 1, 2, 2, 4, 4));
	}
	
}
