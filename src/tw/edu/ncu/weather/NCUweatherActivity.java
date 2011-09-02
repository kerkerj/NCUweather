package tw.edu.ncu.weather;

import tw.edu.ncu.weather.util.*;
import java.net.URL;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NCUweatherActivity extends Activity {
	private TextView data;
	private Button refresh, getWeb;
	private String[] results;
	private WebView web;

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        data = (TextView) findViewById(R.id.result);
        refresh = (Button) findViewById(R.id.refresh);
        getWeb = (Button) findViewById(R.id.getWeb);
        web = (WebView) findViewById(R.id.web);
        web.getSettings().setDefaultTextEncodingName("Big5");
        
        data.setText("請點refresh");
        data.setText("");
        refresh.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				results = fetchData();

				if(isGetData(results)){
					String print = 
				    	"本日日期: "	+ results[0] 			+ "\n"+
						"現在時間:"	+ results[1] 			+ "\n"+
						"氣溫: "		+ results[2] + "℃"		+ "\n"+
						"溼度: "		+ results[3] + "%"		+ "\n"+
						"風向: " 	+ results[4] + "°"		+ "\n"+
						"風速: " 	+ results[5] + "m/s"	+ "\n"+
						"氣壓: " 	+ results[6] + "hPa"	+ "\n"+
						"降雨: " 	+ results[6] + "mm/hr"	+ "\n";
					data.setText(print);
				}else{
					data.setText("null");
				}
			}
        });
        
        getWeb.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				web.loadUrl("http://pblap.atm.ncu.edu.tw/ncucwb/indexReal.asp");
			}
        });
    }
    
    private String[] fetchData(){
    	Parser weather = new Parser();
        String[] finaldata = {""};
    	try{
			String[] toFind = weather.setFilter();
			String pageData = weather.fetchURLData(new URL("http://pblap.atm.ncu.edu.tw/ncucwb/indexReal.asp"));
			
			if (pageData.equals("failed")){
				finaldata[0] = "failed";
				Log.e("jerry54010", "get data false");
			}else{
				finaldata = weather.anlysisData(toFind, pageData);
				Log.e("jerry54010", "get data: true");
			}
    	}catch (Exception e){
    		e.printStackTrace();
    	}
		return finaldata;
    }
    
    private boolean isGetData(String[] data){
    	if(data[0].equals("failed")){
    		return false;
    	}else{
    		return true;
    	}
    }
    
    public void showData(String[] results){
		System.out.println("本日日期: "+ results[0]);
		System.out.println("現在時間:"+ results[1]);
		System.out.println("氣溫: "+ results[2] + "℃");
		System.out.println("溼度: "+ results[3] + "%");
		System.out.println("風向: " + results[4] + "°");
		System.out.println("風速: " + results[5] + "m/s");
		System.out.println("氣壓: " + results[6] + "hPa");
		System.out.println("降雨: " + results[7] + "mm/hr");
	}
    
    protected void onStop(){
    	super.onStop();
    	web.clearCache(true);
    	Toast.makeText(this, "Cleared cache and Leave", Toast.LENGTH_SHORT).show();
    }
}