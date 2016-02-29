package com.etrans.cordova.plugin.backdoor;

import org.apache.cordova.CordovaActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.*;
import android.provider.Settings;
import android.app.*;
import android.location.*;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

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

    private void initVars() {
        if (context == null) {
            cordovaActivity = (CordovaActivity) this.cordova.getActivity();
            context = cordovaActivity.getBaseContext();

            cordova.getActivity().runOnUiThread(
                    new Runnable() {
                        public void run() {
                            cordovaActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        }
                    });

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
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) {
        initVars();
        try {
            if (action.equals("setAppVersion")) {
                Log.d("BackDoorPlugin", "setAppVersion");

//                appVersion = args.getString(0);

                PluginResult result = new PluginResult(PluginResult.Status.OK);
                callbackContext.sendPluginResult(result);
                return true;
            } else if (action.equals("keepScreenOn")) {
                Log.d("BackDoorPlugin", "keepScreenOn");
                PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
                WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "EMS Lock");
                wakeLock.acquire();

                PluginResult result = new PluginResult(PluginResult.Status.OK);
                callbackContext.sendPluginResult(result);
                return true;
            } else if (action.equals("setCallback")) {
                Log.d("BackDoorPlugin", "set callback");
                callback = callbackContext;

                PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
                result.setKeepCallback(true); //re-use the callback on intent events
                callbackContext.sendPluginResult(result);
                return true;
            } else if (action.equals("updateApp")) {
                Log.d("BackDoorPlugin", "update app");

                final String url = args.getString(0);
                final String appName = args.getString(1);

                Log.d("BackDoorPlugin", "update app|  url: " + url + "; appName: " + appName);

                this.cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        updateApp(callbackContext, url, appName);
                    }
                });

//                PluginResult result = new PluginResult(PluginResult.Status.OK);
//                callbackContext.sendPluginResult(result);
                return true;
            } else {
                Log.d("BackDoorPlugin", "invalid action: " + action.toString());
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
                return false;
            }
        } catch (JSONException exception) {
            Log.d("BackDoorPlugin", "JSONException: " + exception.toString());
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
            return false;
        }
    }

    public void updateApp(CallbackContext callbackContext, String serverAddress, String apkName) {
        try {
            String apkUrl = serverAddress + "files/" + apkName;
            String apkFileName = apkName;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL url = new URL(apkUrl);

            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.connect();

            String PATH = Environment.getExternalStorageDirectory() + "/Download/";
            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(file, apkFileName);
            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Download/" + apkFileName)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            callbackContext.success(); // Thread-safe.
        } catch (IOException e) {
            Log.d("BackDoorPlugin", "Update error: " + e.toString());
            callbackContext.error(e.toString()); // Thread-safe.
        }
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
