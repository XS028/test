package test;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.json.simple.JSONObject;

public class test{
	// GUI
	public static Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static int ww = sSize.width;;
	public static int wh = sSize.height;;
	public static paint ggh = new paint();	//draw GUI
	public static exw ex_w = new exw();		//exception window
	// Var. init
	public static int fifo = 0;
	public static int initializedonce = 0;
	public static int canclosenow = 0;
	public static int sleeptime = 30;
	public static int docall = 1;
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
	public static int success = 0;
	public static int startlater[] = new int[20];
	////////////

	public static void main(String[] args){
		while (true){
			io.p("--------");
			if (ggh.viewForm.isVisible()==false){io.t();}
			canclosenow = 0;
			if (initializedonce == 0){call.getnonce(); call.getkeys(); cfg.readcfg();arrinit();};
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
	
	public static void arrinit(){
		for(int i=1; i<=steps; i++){buyord[i]=0;sellord[i]=0;needtobuy[i]=0;needtosell[i]=0;startlater[i]=0;}
	}

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
		if (docall==1){call.callapi(publicapiurl+mode+"/ticker","",0);}

		if(initializedonce==0){								//Только 1 раз (при старте)
			call.callapi(publicapiurl+mode+"/fee","",0);	//Получить процент комиссии
			call.callapi(publicapiv3url+"info","",0);		//Получить минимальный объем покупки
		}

		if(fifo==0&auen==0){io.info(1);}// if auto mode is off, show short info

		io.pgclr();
		JSONObject tmp = (JSONObject) call.ticker.get(mode);
		io.pg(mode.toUpperCase().replace("_", "/")+"     "+system.rvavgdstr(Double.parseDouble(tmp.get("buy").toString()))+"\n");
		if(linx==1){io.pg("\nlinear\n");}else{io.pg("\nmultiplier\n");}
		if(escapemode==1){io.pg("escape\n");}
		if (io.sendc==1){io.pg("\n"+test.mode+"/"+test.mainm+"/"+test.steps+"/"+system.rv1dstr(test.mingain*100-(Double.parseDouble(call.fee.get(test.mode).toString())*2))+"%");}
		if (success==0){ggh.text10.setText("-");}else{ggh.text10.setText(new Integer(success).toString());}
		
		if (initializedonce == 0){ // min field autofill
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
		
		double range = 0;
		double rngrel = 0;
		double fee = Double.parseDouble(call.fee.get(mode).toString());
		
		if (steps>1){
			range = (double)((max-min)/(steps-1));
			rngrel = max;/// NOT! OPTIMISED FOR QUICK BUY // MUST BE SYNCED WITH minam CLASS!
		}else{
			range = (double)((max-min)/steps);
			rngrel = max;/// NOT! OPTIMISED FOR QUICK BUY // MUST BE SYNCED WITH minam CLASS!
		}
		
		tmp = (JSONObject) call.ticker.get(mode);
		double buycost = Double.parseDouble(tmp.get("buy").toString());

		for (int i=1; i<=steps; i++){
			acttrig[i]=rngrel;
			rngrel = rngrel-range;
			look_aside [i] = (system.rvdd(moneygr[i])/system.rvdd(acttrig[i]))-(system.rvdd(moneygr[i])/system.rvdd(acttrig[i]))*((fee/100)/20+fee/100);
			if (system.amb(acttrig[i], buycost)){startlater[i]=1;}
			else {call.callapi(privateapiurl,"Trade&pair="+mode+"&type=buy&rate="+system.rvdstr(acttrig[i])+"&amount="+system.rvdstr(moneygr[i]/acttrig[i]),i);}
		}

		writeall();
		auen = 1;
		ggh.text5.setBackground(paint.gray);
	}

	public static void au(){

		if (auen == 1){
			checkescape();

			JSONObject tmp = (JSONObject) call.ticker.get(mode);
			double buycost = Double.parseDouble(tmp.get("buy").toString());
			
			for (int i=1; i<=steps; i++){//докупить, если нужно
				if((system.amb(buycost,acttrig[i]))&(startlater[i]==1)){
					startlater[i]=0;
					call.callapi(privateapiurl,"Trade&pair="+mode+"&type=buy&rate="+system.rvdstr(acttrig[i])+"&amount="+system.rvdstr(moneygr[i]/acttrig[i]),i);
				}
			}
			
			call.callapi(privateapiurl,"OrderList",0);

			if (call.orderlist.containsKey("error")){
				if (call.orderlist.get("error").toString().contains("no orders")){
					for (int i=1; i<=steps; i++){
						if (!(sellord[i]==0)){needtobuy[i] = 1;}else{if (!(buyord[i]==0)){needtosell[i] = 1;}}
					}
				}
			}else{
				tmp = (JSONObject) call.orderlist.get("return");
				for (int i=1; i<=steps; i++){
					if ((!(tmp.containsKey(new Integer(sellord[i]).toString())))&(!(sellord[i]==0))){needtobuy[i] = 1;
					}else{
					if ((!(tmp.containsKey(new Integer(buyord[i]).toString())))&(!(buyord[i]==0))){needtosell[i] = 1;}}
				}
			} 
			
			int pl = 0;

			for (int i=1; i<=steps; i++){
				if(needtobuy[i] == 1){
					needtobuy[i] = 0;
					sellord[i] = 0;
					success ++;
					pl = 1;
					call.callapi(privateapiurl,"Trade&pair="+mode+"&type=buy&rate="+system.rvdstr(acttrig[i])+"&amount="+system.rvdstr(moneygr[i]/acttrig[i]),i);
				} else {
					if (needtosell[i] == 1){
						buyord[i] = 0;
						needtosell[i] = 0;
						call.callapi(privateapiurl,"Trade&pair="+mode+"&type=sell&rate="+system.rvdstr(acttrig[i]+acttrig[i]*mingain)+"&amount="+system.rvdstr(look_aside[i]),i);
					}
				}
			}
			
			if(pl==1){test.PlaySound("C:/test/notify.wav");}
			
			checkescape();
			if (fifo==0){io.info(2);}
			if (docall==5){writeall();} //запись в файл каждые 10 сек.
		}
	}
	
	public static void checkescape(){
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
			for (int i=1; i<startlater.length; i++){startlater[i] = Integer.parseInt(sc.next());}
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
			out.println();
			for (int i=1; i<startlater.length; i++){out.print(startlater[i]);if (!(i==startlater.length-1)){out.print(" ");}}
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
			startlater[i] = 0;
			writeall();
		}
		io.sendc = 0;
	}
	
	static void PlaySound(String f){
		try{
			AudioInputStream stream = AudioSystem.getAudioInputStream(new File(f));
			DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat());
			Clip clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
			clip.start();
		} catch (UnsupportedAudioFileException e){} 
		catch (IOException e){} 
		catch (LineUnavailableException e){}
		}
 
}//
