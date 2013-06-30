package test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class call {
	public static JSONObject ticker = new JSONObject();
	public static JSONObject fee = new JSONObject();
	public static JSONObject depth = new JSONObject();
	public static JSONObject account = new JSONObject();
	public static JSONObject orderlist = new JSONObject();
	public static JSONObject tradehistory = new JSONObject();
	public static JSONObject minamount = new JSONObject();
	public static String _key = "";
	public static String _secret = "";
	static long nonce = 0;
	public static int errcount = 0;
	
	public static void callapi(String urladdr, String method, int pos) {

		int cangonext = 0;
		if (!(errcount==0)){io.p("AUTOMATIC RESTART! IT'S ALL OK");}

		HttpURLConnection connection = null;

		try{
			URL url = new URL(urladdr);
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			cangonext = 1;
		} catch (Exception e) {
			e.printStackTrace();
			test.ggh.viewForm.setIconImage(paint.ind_err);
			errcount++;
			connection.disconnect();
			system.sleep(errcount);
			if(errcount<=10){callapi(urladdr,method,pos);errcount = 0;}else{exw.sh("Connection open error!");io.empty();}
		}
		if (cangonext==1){
			connection.setRequestProperty("Content-Language","en-US"); 
			connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			connection.setConnectTimeout(60000);
			connection.setReadTimeout(60000);

			if (method.equals("")){
				connection.setUseCaches(false);
				connection.setDoInput(true);
				connection.setDoOutput(false);
				cangonext++;
			} else {
				nonce++;
				writenonce();
				String urlParameters = "nonce="+(new Long(nonce).toString())+"&method="+method;
				connection.setRequestProperty("Key",_key);
				connection.setRequestProperty("Sign",gethash(method));
				connection.setUseCaches(false);
				connection.setDoInput(true);
				connection.setDoOutput(true);
				try {
					DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());
					dataout.writeBytes(urlParameters);
					dataout.flush();
					dataout.close();
					cangonext++;
				} catch (Exception e) {
					e.printStackTrace();
					test.ggh.viewForm.setIconImage(paint.ind_err);
					errcount++;
					connection.disconnect();
					system.sleep(errcount);
					if(errcount<=10){callapi(urladdr,method,pos);errcount = 0;}else{exw.sh("Connection send to server error!");io.empty();}
				}
				io.p(urlParameters);
			}
	        
			if (cangonext == 2){
				JSONObject js = new JSONObject();
				try{
					InputStream is = connection.getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
	        	
					String line = br.readLine();
					JSONParser parser = new JSONParser();
	        	
					Object obj = new Object();
					try {
						obj = parser.parse(line);
					} catch (ParseException e) {
						cangonext = 5;
						if(errcount<=10){callapi(urladdr,method,pos);errcount = 0;}else{exw.sh("JSON parse error!");io.empty();}
					}

					js = (JSONObject) obj;
					cangonext++;
				} catch (Exception e) {
					e.printStackTrace();
					test.ggh.viewForm.setIconImage(paint.ind_err);
					errcount++;
					connection.disconnect();
					system.sleep(errcount);
					if(errcount<=10){callapi(urladdr,method,pos);errcount = 0;}else{exw.sh("Connection get from server error!");io.empty();}
				}
				if(cangonext==3){
					io.p(js);
					include(js,urladdr,method,pos);
				}
			}
		}
	}

	/// json include
	@SuppressWarnings("unchecked")
	public static void include(JSONObject info, String urladdr, String method, int pos){
		int invalidnonce = 0;

		if (info.containsKey("error")){
			if (info.get("error").equals("invalid nonce parameter")){
				invalidnonce = 1;
				test.ggh.viewForm.setIconImage(paint.ind_err);
				errcount++;
				system.sleep(errcount);
				if(errcount<=10){callapi(urladdr,method,pos);errcount = 0;}else{exw.sh("Invalid nonce sequence! Too much retries!");io.empty();}
			}
		}
		
		if (invalidnonce==0){
			if (method.equals("")){	/// PUBLIC API
				if (!(urladdr.contains(test.publicapiv3url))){
					String pair = test.mode;
					if (urladdr.contains("ticker")){ticker.put(pair, info.get("ticker"));}
					if (urladdr.contains("fee")){fee.put(pair, info.get("trade"));}
					if (urladdr.contains("depth")){depth.put(pair, info);}
				}else {if (urladdr.contains("info")){minamount=(JSONObject) info.get("pairs");}}
				/// Trades doesn't realized
			} else {	/// PRIVATE / TRADE API
				if (method.contains("getInfo")){account = (JSONObject) info.get("return");} // w/o success
				if (method.contains("OrderList")){orderlist = info;}
				if (method.contains("TradeHistory")){tradehistory = info;}

				if (method.contains("Trade")&method.contains("buy")){
					if (info.containsKey("error")){test.buyord[pos] = 0;} else {
						JSONObject tmp = (JSONObject) info.get("return");
						test.buyord[pos] = Integer.parseInt(tmp.get("order_id").toString());
						if (test.buyord[pos] == 0){test.sellnow(pos);}// sell right now by id
					}
				}
			
				if (method.contains("Trade")&method.contains("sell")){
					if (info.containsKey("error")){test.sellord[pos] = 0;} else {
						JSONObject tmp = (JSONObject) info.get("return");
						test.sellord[pos] = Integer.parseInt(tmp.get("order_id").toString());
						if (test.sellord[pos] == 0){test.buynow(pos);}// buy right now by id
					}
				}
			}
		}
	}

		///// HMAC sha512
		public static String gethash(String method){
			String nonces = new Long(nonce).toString();
			String datas = "nonce="+nonces+"&method="+method;
			Mac mac;
			String result = "";
			try
			{
				SecretKeySpec secretKey = new SecretKeySpec(_secret.getBytes("UTF-8"),"HmacSHA512"); 
				mac = Mac.getInstance("HmacSHA512");
				mac.init(secretKey);
				final byte[] macData = mac.doFinal(datas.getBytes());
				byte[] hex = new Hex().encode(macData);
				result = new String(hex, "ISO-8859-1");
			}
			catch(final Exception e)
			{
				exw.sh("HMAC-sha512 encryption error!");io.empty();
			}
			return result;
		}
	   
		/// nonce read-write
		public static void getnonce() {
			Scanner sc;
			try {
				sc = new Scanner(new File("C:/test/nonce"));
				nonce = sc.nextLong();
				sc.close();
			}catch(FileNotFoundException e) {
				File file = new File("C:/test/nonce");
				try {
					file.createNewFile();
					nonce = 1;
				}catch(IOException e1) {
					exw.sh("Cannot read/write to file:C:/test/nonce\nIO error!");
					io.empty();
				}
			}
		}
	    
		public static void writenonce() {
			PrintWriter out;
			try {
				out = new PrintWriter("C:/test/nonce");
				out.print(nonce);
				out.close();
			}catch(FileNotFoundException e) {
				File file = new File("C:/test/nonce");
				try {
					file.createNewFile();
					writenonce();
				}catch(IOException e1) {
					exw.sh("Cannot read/write to file:C:/test/nonce\nIO error!");
					io.empty();
				}
			}
		}
	    
		/// get keys
		public static void getkeys() {
			Scanner sc;
			try {
				sc = new Scanner(new File("C:/test/keys"));
				_key = sc.next();
				sc.nextLine();
				_secret = sc.next();
				sc.close();
			}catch(FileNotFoundException e) {
				File file = new File("C:/test/keys");
				try {
					file.createNewFile();
					exw.sh("Please fill C:/btceh/keys file with you key/secret pair!");
					io.empty();
				}catch(IOException e1){
					exw.sh("Cannot read/write to file:C:/test/keys\nIO error!");
					io.empty();
				}
			}
		}
}//