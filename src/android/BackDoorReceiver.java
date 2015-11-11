package com.etrans.cordova.plugin.backdoor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BackDoorReceiver extends BroadcastReceiver {

    private static final String ACTION_EXIT = "com.etrans.driverems.backdoor.ems.EXIT";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_EXIT)) {
            System.exit(0);
        }
        //context.startService(new Intent(context, MainService.class));
    }
}
