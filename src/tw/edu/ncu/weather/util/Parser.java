package tw.edu.ncu.weather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Log;

public class Parser {
	
	//設定表示式以抓取資料
	public String[] setFilter(){
		String[] data = {
				"-&nbsp; (.*)&nbsp; (.*)&nbsp; -",
				"&nbsp;  (.*)&nbsp; -",
				"溫度：&nbsp; (.*) ℃ <br>",
				"濕度：&nbsp; (.*) % <br>",
				"風向：&nbsp; (.*) ° <br>",
				"風速：&nbsp; (.*) m/s <br>",
				"氣壓：&nbsp; (.*) hPa <br>",
				"降雨：&nbsp; (.*) mm/hr"
				};
		
		return data;
	}
	
	//給定URL先抓取網頁資料
	public String fetchURLData(URL url){
		String result="";
		
		try{
			Log.e("jerry54010", "fetching");
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			InputStream input = conn.getInputStream();
			BufferedReader buf = new BufferedReader(new InputStreamReader(input, "big5"));
			String line = buf.readLine();
			
			while(line !=null){
				result +=line;
				line = buf.readLine();
			}
			buf.close();
			input.close();
			
			Log.e("jerry54010", "fetching done");
		}catch (Exception e){
			Log.e("jerry54010", "fetching failed");
			result = "failed";
			Log.e("jerry54010", e.toString());
		}
		
		return result;
	}
	
	//分析資料
	public String[] anlysisData(String[] toFind, String pageData){
		String[] result = {"","","","","","","",""};
		String[] temp;
		
		Pattern p;
		Matcher matcher;
		
		//先將大致符合的字串抓出來
		for(int i=0; i<toFind.length; i++){
			p = Pattern.compile(toFind[i]);
			matcher = p.matcher(pageData);
			
			if (matcher.find()){
				result[i] = matcher.group(0);
			}
		}
		
		//再切割出數據資料 - 測量數據
		for(int i=2; i<result.length; i++){
			temp = result[i].split(" ");
			result[i] = temp[1];
		}
		
		//日期資料
		temp = result[0].split("&nbsp; ");
		result[0] = temp[1];
		result[1] = temp[2];
		
		return result;
	}
	
}
