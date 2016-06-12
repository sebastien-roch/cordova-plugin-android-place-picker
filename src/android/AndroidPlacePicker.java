
package roch.sebastien;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import java.lang.Exception;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.Place;

import android.util.Log;

public class AndroidPlacePicker extends CordovaPlugin {

    private static final String TAG = "AndroidPlacePicker";

    protected CordovaPlugin activityResultCallback;

    protected CallbackContext callbackContext;

    /**
     * Executes the request and returns PluginResult
     *
     * @param  action
     * @param  args
     * @param  callbackContext
     * @return boolean
     * @throws JSONException
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equals("pickPlace")) {
            this.callbackContext = callbackContext;
            try {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                cordova.startActivityForResult((CordovaPlugin) this, builder.build(cordova.getActivity()), 1);
            } catch (Exception ex) {
                Log.e(TAG, "Google Place service may not be present");
                callbackContext.error("Google Place service not present");
            }

            return true;
        }

        // Default response to say the action hasn't been handled
        return false;
    }

    // @Override
    // public void setActivityResultCallback(CordovaPlugin plugin) {
    //     this.activityResultCallback = plugin;
    // }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (this.callbackContext == null) {
            return;
        }

        if (resultCode == Activity.RESULT_CANCELED) {
            this.callbackContext.error("canceled");
            return;
        }

        // this can happen when a network error occurs
        if (intent == null) {
            this.callbackContext.error("picker error. No connection?");
            return;
        }

        Place place = PlacePicker.getPlace(intent, cordova.getActivity());

        try {
            JSONObject responseObject = new JSONObject();

            responseObject.put("name", place.getName().toString());

            JSONObject latlng = new JSONObject();
            latlng.put("latitude", place.getLatLng().latitude);
            latlng.put("longitude", place.getLatLng().longitude);
            responseObject.put("coords", latlng);

            if (place.getViewport() != null) {
                JSONObject southwest = new JSONObject();
                southwest.put("latitude", place.getViewport().southwest.latitude);
                southwest.put("longitude", place.getViewport().southwest.longitude);
                JSONObject northeast = new JSONObject();
                northeast.put("latitude", place.getViewport().northeast.latitude);
                northeast.put("longitude", place.getViewport().northeast.longitude);

                JSONObject viewport = new JSONObject();
                viewport.put("southwest", southwest);
                viewport.put("northeast", northeast);
                responseObject.put("viewport", place.getViewport());
            } else {
                responseObject.put("viewport", JSONObject.NULL);
            }

            this.callbackContext.success(responseObject);
        }
        catch(JSONException ex) {
            this.callbackContext.error(ex.toString());
        }
    }

    public void onRestoreStateForActivityResult(Bundle state, CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }
}