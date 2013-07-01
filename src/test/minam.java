package test;

import org.json.simple.JSONObject;

public class minam{

	@SuppressWarnings("unused")
	public static int check(){

		String mainm = test.mainm;
		String mode = test.mode;
		JSONObject tmp = new JSONObject();

		double min = 0;
		double max = 0;
		double steps = 0;

		try{
			min = Double.parseDouble(test.ggh.text2.getText());
			max = Double.parseDouble(test.ggh.text7.getText());
			steps = Integer.parseInt(test.ggh.text5.getText());
		}catch(Exception e){return 1;}

		tmp = (JSONObject) call.account.get("funds");//

		double point = 0;
		if (test.linx==1){
			point = (double) tmp.get(mainm)/steps;//
		}else{
			int stp = 0;
			for(int i=1; i<=steps; i++){stp = stp+i;} 
			point = (double) tmp.get(mainm)/stp;//
		}

		//double range = (double)((max-min)/(steps-1));
		double firstc = max;/// MUST BE SYNCED WITH test CLASS!

		tmp=(JSONObject) call.minamount.get(mode);// min amount check
		double minam = Double.parseDouble(tmp.get("min_amount").toString());

		if (system.amb(minam,system.rvdd(point/firstc))){if(steps>1){test.ggh.text5.setBackground(paint.red);}else{test.ggh.text5.setBackground(paint.gray);paint.button2.setBackground(paint.red);}return 0;}else{return 1;}
		
	}
	
}