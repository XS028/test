package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class cfg{
	
	public static void readcfg(){
		Scanner sc;
		try {
			sc = new Scanner(new File("C:/test/config.ini"));
			sc.nextLine();
			test.mode=sc.next();
			sc.nextLine();sc.nextLine();
			test.autofill=Double.parseDouble(sc.next());
			sc.nextLine();sc.nextLine();
			test.steps=Integer.parseInt(sc.next());
			sc.nextLine();sc.nextLine();
			test.mingain=Double.parseDouble(sc.next());
			sc.nextLine();sc.nextLine();
			test.sleeptime = Integer.parseInt(sc.next());
			sc.nextLine();sc.nextLine();
			test.escapemode = Integer.parseInt(sc.next());
			sc.nextLine();sc.nextLine();
			test.linx = Integer.parseInt(sc.next());
			sc.close();

			String[] tmp = test.mode.split("[_]+");
			test.mainm = tmp[1];

			if (test.escapemode==1){io.p("ESCAPE MODE ON!");}

			io.p("-------");
			io.p(test.mode);
			io.p(test.mainm);
			io.p(test.autofill);
			io.p(test.steps);
			io.p(test.mingain);
			io.p(test.sleeptime);
			io.p(test.linx);
			io.p("-------");
 
		}catch(FileNotFoundException e) {e.printStackTrace();exw.sh("Error reading cfg:C:/test/config.ini");io.empty();} 
		catch(Exception e){exw.sh("Wrong cfg file:C:/test/config.ini");io.empty();}
	}

}//