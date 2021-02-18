// Bismillah Hirrahman Nirrahim
package com.jogger.setup;

public class Test {
	double field;
	double secondField;
	double thirdField = secondField/field;
	
	public Test(double f, double s) {
		this.field = f;
		this.secondField = s;
	}
	

	public static void main(String[] args) {
		Test one = new Test(23d, 230d);
		System.out.println(one.thirdField);
		one.secondField = 620d;
		System.out.println(one.thirdField);
	}

}
