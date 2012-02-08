package tw.edu.ncu.weather;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UpdateReceiver extends BroadcastReceiver{
		
	@Override //若使用者解鎖手機，則會更新資料
	public void onReceive(Context context, Intent intent) {
		String actionType = intent.getAction();
		
		if (actionType != null && actionType.equals(Intent.ACTION_USER_PRESENT)) {
			boolean isServiceRunning = isMyServiceRunning(context, "tw.edu.ncu.weather.UpdateService");

			if(!isServiceRunning){
				Intent UpdateService = new Intent(context, UpdateService.class);
				context.startService(UpdateService);
			}
		}
	}
	
	//偵測所呼叫的Service有沒有在運作
	private boolean isMyServiceRunning(Context context, String ServiceName) {
	    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (ServiceName.equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
}
