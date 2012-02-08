package tw.edu.ncu.weather;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
	private WebView web;
	private HttpPost httprequest;
	private List<NameValuePair> SendData;
	private String HttpResponseText = "", uri = "http://www.mgt.ncu.edu.tw/~jerry54010/get_data.php";
	private String date, time, temperature, humidity, wind_direction, wind_velocity, pressure, rain, weather, error_code, error_msg;
	private int webflag=0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        refresh = (Button) findViewById(R.id.refresh);
        getWeb = (Button) findViewById(R.id.getWeb);
        web = (WebView) findViewById(R.id.web);
        web.getSettings().setDefaultTextEncodingName("Big5");
        data = (TextView) findViewById(R.id.result);
        data.setText("");
        
        refresh.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if (haveInternet() == true) {
					Thread http = new Thread(new httpcon());
					http.start();
				} else {
					Toast.makeText(NCUweatherActivity.this, "沒有網路連線", Toast.LENGTH_LONG).show();
				}
			}
        });
        
        getWeb.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				//GONE=8 ; INVISIBLE=4 ; VISIBLE=0
				if (webflag == 0){
					web.loadUrl("http://pblap.atm.ncu.edu.tw/ncucwb/indexReal.asp");
					web.setVisibility(0); //VISIBLE
					webflag = 1;
				}
				else{
					web.setVisibility(8); //GONE 
					webflag = 0;
				} 
			}
        });
    }

    protected void onStop(){
    	super.onStop();
    	web.clearCache(true);
    }
    
    //偵測連線
    private class httpcon implements Runnable {
		public void run() {
			try {
				runOnUiThread(new Runnable() {
					public void run(){
						Toast.makeText(NCUweatherActivity.this, "連線中", Toast.LENGTH_SHORT).show();
					}
				});
				
				SendData = new ArrayList<NameValuePair>();
				SendData.add(new BasicNameValuePair("user_agent", "Android"));
				httprequest = new HttpPost(uri);
				httprequest.setEntity(new UrlEncodedFormEntity(SendData, HTTP.UTF_8));
				
				HttpResponse httpResponse = new DefaultHttpClient().execute(httprequest);
				
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					HttpResponseText = EntityUtils.toString(httpResponse.getEntity());
					
					JSONObject jsonObject = new JSONObject(HttpResponseText);
					
					date = jsonObject.getString("date");
					time = jsonObject.getString("time");
					temperature = jsonObject.getString("temperature");
					humidity = jsonObject.getString("humidity");
					wind_direction = jsonObject.getString("wind_direction");
					wind_velocity = jsonObject.getString("wind_velocity");
					pressure = jsonObject.getString("pressure");
					rain = jsonObject.getString("rain");
					weather = jsonObject.getString("weather");
					error_code = jsonObject.getString("error_code");
					error_msg = jsonObject.getString("error_msg");
					
					Log.e("url", date+","+time+","+temperature+","+humidity+","+wind_direction+","+wind_velocity+","+pressure+","+rain+","+weather+","+error_code+","+error_msg);
					
					//跑UI的thread
					runOnUiThread(new Runnable() {
						public void run() {
							if(error_code.equals("0")){
								data.setText("");
								data.setText(
										"日期："+date+"\n"+
										"時間："+time+"\n"+
										"溫度："+temperature+" ℃"+"\n"+
										"濕度："+humidity+" %"+"\n"+
										"風向："+wind_direction+" °"+"\n"+
										"風速："+wind_velocity+" m/s"+"\n"+
										"氣壓："+pressure+" hPa"+"\n"+
										"雨量："+rain+" mm/hr"+"\n"+
										"天氣："+weather+"\n");
							}
							else{
								data.setText("");
								data.setText("噢歐，出錯了QQ\n"+"error code: "+error_code+"\n"+"error message: "+error_msg);
							}
						}
					});
				}
			} catch (Exception e) {
				Log.e("url", "false"+e);
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(NCUweatherActivity.this, "沒有網路連線", Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
    }
    
    //偵測網路連線
    private boolean haveInternet() {
		boolean result = false;
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			result = false;
		} else {
			if (!info.isAvailable()) {
				result = false;
			} else {
				result = true;
			}
		}
		return result;
	}
}