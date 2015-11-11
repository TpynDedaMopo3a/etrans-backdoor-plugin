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

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaResourceApi;
import org.apache.cordova.PluginResult;

public class BackDoor extends CordovaPlugin {

    private Context context;
    private CordovaActivity cordovaActivity;
    private CallbackContext callback = null;

    public BackDoor() {
        context = this.cordova.getActivity().getApplicationContext();
        cordovaActivity = (CordovaActivity) this.cordova.getActivity();
    }

    private CallbackContext onNewIntentCallbackContext = null;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        if (action.equals("setCallback") {
            callback = callbackContext;
            return true;
        } else if (action.equals("goToGPSSettings")) {
            actionGoToGPSSettings(args, callbackContext);
            return true;
        } else if (action.equals("checkGPS")) {
            actionCheckGPS(args, callbackContext);
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


    private void actionIsEnableGPS(JSONArray args, CallbackContext callbackContext) {
        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, isGPSEnabled()));
    }

    private void actionGoToGPSSettings(JSONArray args, CallbackContext callbackContext) {
        showAlertDialog();
    }

    private void actionCheckGPS(JSONArray args, CallbackContext callbackContext) {
        if (!isGPSEnabled()) {
            showAlertDialog();
        }
    }

    private boolean isGPSEnabled() {
        try {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            return false;
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Location Services Not Active");
        builder.setMessage("Please turn on GPS in High Accuracy mode in order to use application.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                goToGPSSettings();
                closeApplication();
            }
        });
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void goToGPSSettings() {
        cordovaActivity.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
    }

    private void closeApplication() {
        System.exit(0);
    }

}
