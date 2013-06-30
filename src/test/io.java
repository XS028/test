package test;

import org.json.simple.JSONObject;

public class io{

	public static void p(Object s){
		System.out.println(s);
	}

	public static void t(){
		System.exit(0);
	}

	public static void pg(String s){
		test.ggh.text1.append(s);
	}

	public static void pgclr(){
		test.ggh.text1.setText("");
	}


	public static void empty(){
		while (true){
			system.sleep(60);
		}
	}

	public static void info(int mode){
		call.callapi(test.privateapiurl,"getInfo",0);
		if(mode==1){call.callapi(test.privateapiurl,"OrderList",0);}
		JSONObject tmp = (JSONObject) call.account.get("funds");

		if (mode == 1){
			test.ggh.text4.setText("");
			test.ggh.text4.append("Balance: "+system.r3avgstrstr(tmp.get(test.mainm).toString())+" "+test.mainm.toUpperCase()+"\n");
			test.ggh.text4.append("Orders opened: "+call.account.get("open_orders")+"\n");
			//
			test.ggh.text4.append("--------------------------------\n");
			test.ggh.text4.append("Mode: "+test.mode+"\n");
			test.ggh.text4.append("Main dep.: "+test.mainm+"\n");
			test.ggh.text4.append("Steps: "+test.steps+"\n");
			test.ggh.text4.append("Gain: "+system.rv1dstr(test.mingain*100)+"%-"+system.rv1dstr((Double.parseDouble(call.fee.get(test.mode).toString())*2))+"%="+system.rv1dstr(test.mingain*100-(Double.parseDouble(call.fee.get(test.mode).toString())*2))+"%\n");
			

			
				
		} else {
			int[] nextordbuy = new int[20];
			int[] nextordsell = new int[20];

			int buy = 0;
			int sell = 0;
			for (int i = 1; i<=test.steps; i++){
				if (!(test.buyord[i] == 0)){buy++;nextordbuy[i] = 1;}else{nextordbuy[i] = 0;}
				if (!(test.sellord[i] == 0)){sell++;nextordsell[i] = 1;}else{nextordsell[i] = 0;}
			}

			test.ggh.text4.setText("");
			test.ggh.text4.append("Balance: "+system.r3avgstrstr(tmp.get(test.mainm).toString())+" "+test.mainm.toUpperCase()+"\n");
			test.ggh.text4.append("Orders opened: "+call.account.get("open_orders")+"\n");
			test.ggh.text4.append("Buy orders: "+buy+"\n");
			test.ggh.text4.append("Sell orders: "+sell+"\n");
			//
			test.ggh.text4.append("--------------------------------\n");
			test.ggh.text4.append("Mode: "+test.mode+"\n");
			test.ggh.text4.append("Main dep.: "+test.mainm+"\n");
			test.ggh.text4.append("Steps: "+test.steps+"\n");
			test.ggh.text4.append("Gain: "+system.rv1dstr(test.mingain*100)+"%-"+system.rv1dstr((Double.parseDouble(call.fee.get(test.mode).toString())*2))+"%="+system.rv1dstr(test.mingain*100-(Double.parseDouble(call.fee.get(test.mode).toString())*2))+"%\n");
			if((!(buy==0))||(!(sell==0))){
				test.ggh.text4.append("--------------------------------\n");
				test.ggh.text4.append("Next orders:\n");
			}
			for (int i=1; i<=test.steps; i++){if (!(nextordbuy[i]==0)){test.ggh.text4.append("buy "+system.r5avgdstr(test.look_aside[i])+" @ "+system.r5avgdstr(test.acttrig[i])+"\n");}}
			for (int i=1; i<=test.steps; i++){if (!(nextordsell[i]==0)){test.ggh.text4.append("sell "+system.r5avgdstr(test.look_aside[i])+" @ "+system.r5avgdstr(test.acttrig[i]+test.acttrig[i]*test.mingain)+"\n");}}
		}
	}
	
}