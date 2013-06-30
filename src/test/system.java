package test;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class system{

	public static void sleep(int sec){
		try {
			TimeUnit.SECONDS.sleep(sec);
		}catch(InterruptedException e) {
			e.printStackTrace();
			exw.sh("A program was interrupted. Please, restart");
			io.empty();
		}
	}

	public static boolean amb(double a, double b){
		long ax = Double.doubleToLongBits(a);
		long ay = Double.doubleToLongBits(b);
		return ax>ay;
	}

	public static double r5avgstrd(String in){
		BigDecimal bigDecimal = new BigDecimal(Double.parseDouble(in));
		bigDecimal = bigDecimal.setScale(5, BigDecimal.ROUND_UP);
		return bigDecimal.doubleValue();	
	}

	public static String r5avgdstr(double in){
		BigDecimal bigDecimal = new BigDecimal(in);
		bigDecimal = bigDecimal.setScale(5, BigDecimal.ROUND_UP);
		if(bigDecimal.toPlainString().length()>7){
			return bigDecimal.toPlainString().substring(0,7);
		}else return bigDecimal.toPlainString();
	}

	public static String r3avgdstr(double in){// test
		BigDecimal bigDecimal = new BigDecimal(in);
		bigDecimal = bigDecimal.setScale(3, BigDecimal.ROUND_HALF_UP);
		return bigDecimal.toPlainString();
	}   

	public static String r3avgstrstr(String ins){// io
		double in = Double.parseDouble(ins);
		BigDecimal bigDecimal = new BigDecimal(in);
		bigDecimal = bigDecimal.setScale(3, BigDecimal.ROUND_HALF_UP);
		return bigDecimal.toPlainString();
	}

	public static String rvdstr(double in){
		BigDecimal bigDecimal = new BigDecimal(in);
		String ins = bigDecimal.toPlainString();
		if (ins.length()>=7) {return ins.substring(0,7);}else{return ins;}
	}

	public static double rvdd(double in){
		BigDecimal bigDecimal = new BigDecimal(in);
		String ins = bigDecimal.toPlainString();
		String tmp = "";
		if (ins.length()>=7) {tmp=ins.substring(0,7);}else{tmp=ins;}
		return Double.parseDouble(tmp);
	}

	public static String rv1dstr(double in){
		BigDecimal bigDecimal = new BigDecimal(in);
		bigDecimal = bigDecimal.setScale(1, BigDecimal.ROUND_DOWN);
		return bigDecimal.toPlainString();
	}

	public static String rvavgdstr(double in){// test
		BigDecimal bigDecimal = new BigDecimal(in);
		if (amb(in, 100000)){bigDecimal = bigDecimal.setScale(0, BigDecimal.ROUND_DOWN);}
		else if (amb(in, 10000)&amb(100000, in)){bigDecimal = bigDecimal.setScale(0, BigDecimal.ROUND_DOWN);}
		else if (amb(in, 1000)&amb(10000, in)){bigDecimal = bigDecimal.setScale(1, BigDecimal.ROUND_DOWN);}
		else if (amb(in, 100)&amb(1000, in)){bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_DOWN);}
		else if (amb(in, 10)&amb(100, in)){bigDecimal = bigDecimal.setScale(3, BigDecimal.ROUND_DOWN);}
		else if (amb(in, 1)&amb(10, in)){bigDecimal = bigDecimal.setScale(4, BigDecimal.ROUND_DOWN);}
		else {bigDecimal = bigDecimal.setScale(5, BigDecimal.ROUND_DOWN);}
		return bigDecimal.toPlainString();
	}

}