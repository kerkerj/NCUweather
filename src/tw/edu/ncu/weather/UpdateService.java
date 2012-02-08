package tw.edu.ncu.weather;

import tw.edu.ncu.weather.widget.WidgetProvider;
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
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class UpdateService extends Service{

	private HttpPost httprequest;
	private List<NameValuePair> SendData;
	private String HttpResponseText = "", uri = "http://www.mgt.ncu.edu.tw/~jerry54010/get_data.php";
	private String date, time, temperature, humidity, wind_direction, wind_velocity, pressure, rain, weather, error_code, error_msg;
	
	AppWidgetManager appWidgetManager;
	RemoteViews remoteViews;
	ComponentName projectWidget;
	
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		appWidgetManager = AppWidgetManager.getInstance(this);
		remoteViews = new RemoteViews(this.getPackageName(), R.layout.widget_ui);
		projectWidget = new ComponentName(this, WidgetProvider.class);
		
		if (haveInternet() == true) {
			Toast.makeText(this, "開始擷取資料", Toast.LENGTH_SHORT).show();
			updateWidgetViews();
		} else {
			Toast.makeText(this, "沒有網路連線",Toast.LENGTH_SHORT).show();
		}
		this.stopSelf();
	}
	
	private void updateWidgetViews() {
        // only update widgets if some exist
        if (appWidgetManager.getAppWidgetIds(projectWidget).length > 0) {
            Thread http = new Thread(new httpcon());
    		http.start();
        }
    }

	public void onDestroy() {
		super.onDestroy();
	}

	public IBinder onBind(Intent intent) {
		return null;
	}

	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	private class httpcon implements Runnable {
		public void run() {
			try {
				SendData = new ArrayList<NameValuePair>();
				SendData.add(new BasicNameValuePair("user_agent", "Android"));
				httprequest = new HttpPost(uri);
				httprequest.setEntity(new UrlEncodedFormEntity(SendData, HTTP.UTF_8));
				
				HttpResponse httpResponse = new DefaultHttpClient().execute(httprequest);
				
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					HttpResponseText = EntityUtils.toString(httpResponse.getEntity());
				}
				
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
				
				Log.v("url", date+","+time+","+temperature+","+humidity+","+wind_direction+","+wind_velocity+","+pressure+","+rain+","+weather+","+error_code+","+error_msg);
				
				remoteViews.setTextViewText(R.id.text, 
						"目前：" + time +"\n溫度："+temperature+" ℃"+"\n"+"天氣："+weather+"\n"
						);
				appWidgetManager.updateAppWidget(projectWidget, remoteViews);
			} catch (Exception e) {
				e.printStackTrace();
				Looper.prepare();
				Toast.makeText(UpdateService.this, "Service: no network" ,Toast.LENGTH_LONG).show();
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
