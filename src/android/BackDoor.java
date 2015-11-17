package com.etrans.cordova.plugin.backdoor;

import org.apache.cordova.CordovaActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.*;
import android.provider.Settings;
import android.app.*;
import android.location.*;
import android.net.Uri;
import android.text.Html;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaResourceApi;
import org.apache.cordova.PluginResult;

public class BackDoor extends CordovaPlugin {

    private Context context = null;
    private CordovaActivity cordovaActivity;
    private CallbackContext callback = null;
    private BroadcastReceiver receiver;

    private String appVersion = "none";

    private static final String ACTION_EXIT = "com.etrans.driverems.backdoor.ems.EXIT";
    private static final String ACTION_RELOGIN = "com.etrans.driverems.backdoor.ems.RELOGIN";

    public BackDoor() {
        /*context = this.cordova.getActivity().getApplicationContext();
        cordovaActivity = (CordovaActivity) this.cordova.getActivity();


        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_EXIT);
        filter.addAction(ACTION_RELOGIN);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(ACTION_EXIT)) {
                    Log.d("BackDoorPlugin", "receive broadcast: " + ACTION_EXIT);
                    System.exit(0);
                } else if (intent.getAction().equals(ACTION_RELOGIN)) {
                    Log.d("BackDoorPlugin", "receive broadcast: " + ACTION_RELOGIN);
                    if (callback != null) {
                        PluginResult result = new PluginResult(PluginResult.Status.OK, intent.getDataString());
                        result.setKeepCallback(true);
                        callback.sendPluginResult(result);
                    }
                }
            }
        };
        context.registerReceiver(receiver, filter);*/
    }

    private void initVars(){
        if(context == null){
            cordovaActivity = (CordovaActivity) this.cordova.getActivity();
            context = cordovaActivity.getBaseContext();

            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_EXIT);
            filter.addAction(ACTION_RELOGIN);

            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(ACTION_EXIT)) {
                        Log.d("BackDoorPlugin", "receive broadcast: " + ACTION_EXIT);
                        System.exit(0);
                    } else if (intent.getAction().equals(ACTION_RELOGIN)) {
                        Log.d("BackDoorPlugin", "receive broadcast: " + ACTION_RELOGIN);
                        if (callback != null) {
                            PluginResult result = new PluginResult(PluginResult.Status.OK, intent.getDataString());
                            result.setKeepCallback(true);
                            callback.sendPluginResult(result);
                        }
                    }
                }
            };
            context.registerReceiver(receiver, filter);
        }
    }

    private CallbackContext onNewIntentCallbackContext = null;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        initVars();
        if (action.equals("setAppVersion")) {
            Log.d("BackDoorPlugin", "setAppVersion");

            appVersion = args.getString(0);

            PluginResult result = new PluginResult(PluginResult.Status.OK);
            callbackContext.sendPluginResult(result);
            return true;
        }
        else if (action.equals("setCallback")) {
            Log.d("BackDoorPlugin", "set callback");
            callback = callbackContext;

            PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
            result.setKeepCallback(true); //re-use the callback on intent events
            callbackContext.sendPluginResult(result);
            return true;
        }

        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
        return false;
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (this.onNewIntentCallbackContext != null) {
            PluginResult result = new PluginResult(PluginResult.Status.OK, intent.getDataString());
            result.setKeepCallback(true);
            this.onNewIntentCallbackContext.sendPluginResult(result);
        }
    }

}
