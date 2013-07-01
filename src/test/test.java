package test;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import org.json.simple.JSONObject;

public class test{

	public static Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static int ww = sSize.width;;
	public static int wh = sSize.height;;
	public static paint ggh = new paint();	//draw GUI
	public static exw ex_w = new exw();		//exception window

	public static int fifo = 0;
	public static int initializedonce = 0;
	public static int canclosenow = 0;
	public static int sleeptime = 30;
	public static String publicapiurl = "https://btc-e.com/api/2/";
	public static String publicapiv3url = "https://btc-e.com/api/3/";
	public static String privateapiurl = "https://btc-e.com/tapi";

	/////////// AU
	public static String mode = "ltc_rur";
	public static String mainm = "rur";
	public static double autofill = 0.05;
	public static int steps = 3;
	public static double min = 0;
	public static double max = 0;
	public static double mingain = 0.01;
	public static double[] moneygr = new double[20];
	public static double[] acttrig = new double[20];
	public static double[] look_aside = new double[20];
	public static int[] buyord = new int[20];
	public static int[] sellord = new int[20];
	public static int[] needtosell = new int[20];
	public static int[] needtobuy = new int[20];
	public static int auen = 0;
	public static int escapemode = 0;
	public static int linx = 1; // 1-linear, 2-multiplier
	////////////

	public static void main(String[] args){
		while (true){
			if (ggh.viewForm.isVisible()==false){io.t();}
			canclosenow = 0;
			if (initializedonce == 0){call.getnonce(); call.getkeys(); cfg.readcfg();};
			ggh.viewForm.setIconImage(paint.ind_wait);
			getall();
			au();
			ggh.viewForm.setIconImage(paint.ind_ok);
			fifoexecute();
			resetcolor();
			canclosenow = 1;
			system.sleep(sleeptime);
		}
	}//

	public static void fifoexecute(){
		switch(fifo){
		case 1:{auinit();io.info(2);fifo=0;break;}
		case 2:{readall();io.info(2);fifo=0;break;}
		case 3:{cancelall();io.info(1);fifo=0;break;}
		case 4:{io.t();break;}
		}
		if (ggh.viewForm.isVisible()==false){io.t();}
	}

	public static void resetcolor(){
		paint.button2.setBackground(paint.white);
		paint.button3.setBackground(paint.white);
		paint.button4.setBackground(paint.white);
	}

	// get all
	public static void getall(){
		call.callapi(publicapiurl+mode+"/ticker","",0);
		if(initializedonce==0){
			call.callapi(publicapiurl+mode+"/fee","",0);
			call.callapi(publicapiv3url+"info","",0);
	}

		if(fifo==0&auen==0){io.info(1);}// if auto mode is off, show short info

		io.pgclr();
		JSONObject js = (JSONObject) call.ticker.get(mode);
		io.pg(mode.toUpperCase().replace("_", "/")+"     "+system.rvavgdstr(Double.parseDouble(js.get("last").toString()))+"\n");
		if(linx==1){io.pg("\nlinear\n");}else{io.pg("\nmultiplier\n");}
		if(escapemode==1){io.pg("escape\n");}
		
		if (initializedonce == 0){ // min filed autofill
			JSONObject tmp = (JSONObject) call.ticker.get(mode);
			double temp = Double.parseDouble(tmp.get("sell").toString());
			temp = temp - temp*autofill;
			ggh.text2.setText(system.rvavgdstr(temp));
			ggh.text5.setText(new Integer(steps).toString());
			ggh.text7.setText(system.rvavgdstr(Double.parseDouble(tmp.get("sell").toString())));
		}

		initializedonce = 1;
	}

	public static void auinit(){
		JSONObject tmp = new JSONObject();
		tmp = (JSONObject) call.account.get("funds");//

		if (linx==1){
			double point = (double) tmp.get(mainm)/steps;//
			for(int i=1; i<=steps; i++){moneygr[i] = point;}//
		} else{
			int stp = 0;
			for(int i=1; i<=steps; i++){stp = stp+i;} 
			double point = (double) tmp.get(mainm)/stp;//
			for(int i=1; i<=steps; i++){moneygr[i] = i*point;}//
		}
		
		double range = (double)(max-min)/steps;
		double rngrel = max;/// NOT! OPTIMISED FOR QUICK BUY // MUST BE SYNCED WITH minam CLASS!

		for (int i=1; i<=steps; i++){
			rngrel = rngrel-range;
			acttrig[i]=rngrel;
		}

		for (int i=1; i<=steps; i++){
			call.callapi(privateapiurl,"Trade&pair="+mode+"&type=buy&rate="+system.rvdstr(acttrig[i])+"&amount="+system.rvdstr(moneygr[i]/acttrig[i]),i);
		}

		double fee = Double.parseDouble(call.fee.get(mode).toString());
		for (int i=1; i<=steps; i++){
			look_aside [i] = (system.rvdd(moneygr[i])/system.rvdd(acttrig[i]))-(system.rvdd(moneygr[i])/system.rvdd(acttrig[i]))*((fee/100)/20+fee/100);
		}

		writeall();
		auen = 1;
		ggh.text5.setBackground(paint.gray);
	}

	public static void au(){

		if (auen == 1){
			
			// escape mode check
			if (escapemode==1){
				int stop = 1;
				for (int i=1; i<=steps; i++){
					if (!(sellord[i]==0)){stop=0;}
				}
				if (stop==1){
					writeall();
					exw.text.setBackground(exw.green);
					exw.sh("SELL ORDERS COMPLETED\nPROGRAMM STOPPED");
					io.empty();
				}
			}
			/////
			
			call.callapi(privateapiurl,"OrderList",0);

			if (call.orderlist.containsKey("error")){
				for (int i=1; i<=steps; i++){
					if (!(sellord[i]==0)){needtobuy[i] = 1;}
					if (!(buyord[i]==0)){needtosell[i] = 1;}
				}
			}else{
				JSONObject tmp = (JSONObject) call.orderlist.get("return");
				for (int i=1; i<=steps; i++){
					if (tmp.containsKey(new Integer(sellord[i]).toString())){needtobuy[i] = 0;}else if (!(sellord[i]==0)){needtobuy[i] = 1;}
					if (tmp.containsKey(new Integer(buyord[i]).toString())){needtosell[i] = 0;}else if (!(buyord[i]==0)){needtosell[i] = 1;}
				}
			} 

			for (int i=1; i<=steps; i++){
				if(needtobuy[i] == 1){
					needtobuy[i] = 0;
					sellord[i] = 0;
					call.callapi(privateapiurl,"Trade&pair="+mode+"&type=buy&rate="+system.rvdstr(acttrig[i])+"&amount="+system.rvdstr(moneygr[i]/acttrig[i]),i);
				}
				if (needtosell[i] == 1){
					needtosell[i] = 0;
					buyord[i] = 0;
					call.callapi(privateapiurl,"Trade&pair="+mode+"&type=sell&rate="+system.rvdstr(acttrig[i]+acttrig[i]*mingain)+"&amount="+system.rvdstr(look_aside[i]),i);
				}
			}
			
			if (fifo==0){io.info(2);}
			writeall();
		}
	}

	public static void sellnow(int pos){
		call.callapi(privateapiurl,"Trade&pair="+mode+"&type=sell&rate="+system.rvdstr(acttrig[pos]+acttrig[pos]*mingain)+"&amount="+system.rvdstr(look_aside[pos]),pos);
	}

	public static void buynow(int pos){
		call.callapi(privateapiurl,"Trade&pair="+mode+"&type=buy&rate="+system.rvdstr(acttrig[pos])+"&amount="+system.rvdstr(moneygr[pos]/acttrig[pos]),pos);
	}

	public static void readall(){
		Scanner sc;
		try {
			sc = new Scanner(new File("C:/test/main"));
			for (int i=1; i<moneygr.length; i++){moneygr[i] = Double.parseDouble(sc.next());}
			sc.nextLine();
			for (int i=1; i<acttrig.length; i++){acttrig[i] = Double.parseDouble(sc.next());}
			sc.nextLine();
			for (int i=1; i<look_aside.length; i++){look_aside[i] = Double.parseDouble(sc.next());}
			sc.nextLine();
			for (int i=1; i<buyord.length; i++){buyord[i] = Integer.parseInt(sc.next());}
			sc.nextLine();
			for (int i=1; i<sellord.length; i++){sellord[i] = Integer.parseInt(sc.next());}
			sc.nextLine();
			for (int i=1; i<needtosell.length; i++){needtosell[i] = Integer.parseInt(sc.next());}
			sc.nextLine();
			for (int i=1; i<needtobuy.length; i++){needtobuy[i] = Integer.parseInt(sc.next());}
			sc.nextLine();
			steps = Integer.parseInt(sc.next());
			sc.close();

			ggh.text5.setText(new Integer(steps).toString());

			int buy = 0;
			int sell = 0;
			for (int i = 1; i<=test.steps; i++){
				if(!(test.buyord[i] == 0)){buy++;}
				if(!(test.sellord[i] == 0)){sell++;}
			}
			if (buy==0&sell==0){auen = 0;paint.button3.setBackground(paint.yellow);}else{auen = 1;}
 
		} catch (FileNotFoundException e){e.printStackTrace();paint.button3.setBackground(paint.red);}
		catch (Exception e){
			exw.sh("Cannot read from file:C:/test/main\nFile is corrupt!");
			io.empty();
		}
	}

	public static void writeall() {
		PrintWriter out;
		try {
			File file = new File("C:/test/main");
			if (file.exists()){file.delete(); file.createNewFile();}
			out = new PrintWriter("C:/test/main");
			for (int i=1; i<moneygr.length; i++){out.print(moneygr[i]);if (!(i==moneygr.length-1)){out.print(" ");}}
			out.println();
			for (int i=1; i<acttrig.length; i++){out.print(acttrig[i]);if (!(i==acttrig.length-1)){out.print(" ");}}
			out.println();
			for (int i=1; i<look_aside.length; i++){out.print(look_aside[i]);if (!(i==look_aside.length-1)){out.print(" ");}}
			out.println();
			for (int i=1; i<buyord.length; i++){out.print(buyord[i]);if (!(i==buyord.length-1)){out.print(" ");}}
			out.println();
			for (int i=1; i<sellord.length; i++){out.print(sellord[i]);if (!(i==sellord.length-1)){out.print(" ");}}
			out.println();
			for (int i=1; i<needtosell.length; i++){out.print(needtosell[i]);if (!(i==needtosell.length-1)){out.print(" ");}}
			out.println();
			for (int i=1; i<needtobuy.length; i++){out.print(needtobuy[i]);if (!(i==needtobuy.length-1)){out.print(" ");}}
			out.println();/////
			out.print(steps);/////
			out.close();
		}catch(IOException e1) {
			exw.sh("Cannot read/write to file:C:/test/main\nIO error!");
			io.empty();
		}
	}

	public static void cancelall(){ // cancel all orders, auto to off, arrays reset, write all
		for (int i=1; i<=steps; i++){
			auen = 0;
			if (!(buyord[i]==0)){call.callapi(privateapiurl,"CancelOrder&order_id="+buyord[i],0);buyord[i] = 0;}
			if (!(sellord[i]==0)){call.callapi(privateapiurl,"CancelOrder&order_id="+sellord[i],0);buyord[i] = 0;}
		}
		for (int i=1; i<=steps; i++){
			needtosell[i] = 0;
			needtobuy[i] = 0;
			writeall();
		}
	}
 
}//
