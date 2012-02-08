package tw.edu.ncu.weather.widget;

import tw.edu.ncu.weather.R;
import tw.edu.ncu.weather.UpdateService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider{
	private RemoteViews remoteViews;
	private ComponentName thisWidget;
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_ui);
		thisWidget = new ComponentName(context, WidgetProvider.class);
		
		//點左側可進入主程式
		Intent intent = new Intent(context, tw.edu.ncu.weather.NCUweatherActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.to_main, pendingIntent);
		
		//點擊右側呼叫更新Service
		Intent intent2 = new Intent(context, UpdateService.class);
        PendingIntent pendingIntent2 = PendingIntent.getService(context, 0, intent2, 0);
        remoteViews.setOnClickPendingIntent(R.id.refresh, pendingIntent2);
        
        //更新
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
		
		if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
			final int appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
			
			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				this.onDeleted(context, new int[] { appWidgetId });
			}
		} else {
			super.onReceive(context, intent);
		}
	}
}