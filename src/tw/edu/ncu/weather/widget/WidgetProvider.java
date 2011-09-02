package tw.edu.ncu.weather.widget;

import tw.edu.ncu.weather.R;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider{
	private WebView web;
	
	public void onUpdate(Context context, AppWidgetManager awm, int[] appWidgetIds){
		final int N = appWidgetIds.length;
		
		for (int i=0 ; i< N; i++){
			int appWidgetId = appWidgetIds[i];
			
			//建立一個intent來啟動main
			Intent intent = new Intent(context, tw.edu.ncu.weather.NCUweatherActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
			
			//拿到widget的layout, 並加入listener
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ui);
			views.setOnClickPendingIntent(R.id.textView1, pendingIntent);
			
			// Tell the AppWidgetManager to perform an update on the current app widget
			awm.updateAppWidget(appWidgetId, views);
		}
		
	}
}