package com.bzhang.mmall.util;

import java.math.BigDecimal;

public class BigDecimalUtil {
	private BigDecimalUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static BigDecimal add(double d1,double d2) {
		BigDecimal b1=new BigDecimal(Double.toString(d1));
		BigDecimal b2=new BigDecimal(Double.toString(d2));
		return b1.add(b2);
	}
	
	public static BigDecimal sub(double d1,double d2) {
		BigDecimal b1=new BigDecimal(Double.toString(d1));
		BigDecimal b2=new BigDecimal(Double.toString(d2));
		return b1.subtract(b2);
	}
	
	public static BigDecimal mul(double d1,double d2) {
		BigDecimal b1=new BigDecimal(Double.toString(d1));
		BigDecimal b2=new BigDecimal(Double.toString(d2));
		return b1.multiply(b2);
	}
	
	public static BigDecimal div(double d1,double d2) {
		BigDecimal b1=new BigDecimal(Double.toString(d1));
		BigDecimal b2=new BigDecimal(Double.toString(d2));
		return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);
	}
}
