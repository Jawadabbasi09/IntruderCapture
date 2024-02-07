package com.example.intrudercapture;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.UserHandle;
import android.util.Log;

public class MyDeviceAdminReceiver extends DeviceAdminReceiver {
    SharedPreferences pref;

    @Override // android.app.admin.DeviceAdminReceiver
    public void onEnabled(Context context, Intent intent) {
        SharedPreferences.Editor edit = context.getSharedPreferences("IntruderButton", 0).edit();
        edit.putBoolean("deviceadmin", true);
        edit.commit();
    }

    @Override // android.app.admin.DeviceAdminReceiver
    public void onPasswordFailed(Context context, Intent intent, UserHandle userHandle) {
        super.onPasswordFailed(context, intent, userHandle);
        Log.d("failedpassword", "onPasswordFailed: ");
        CameraManager.getInstance(context).takePhoto();


    }

    @Override // android.app.admin.DeviceAdminReceiver
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "Admin rights are being requested to be disabled for the app called: '" + context.getString(R.string.app_name) + "'.";
    }

    @Override // android.app.admin.DeviceAdminReceiver
    public void onDisabled(Context context, Intent intent) {
        SharedPreferences.Editor edit = context.getSharedPreferences("IntruderButton", 0).edit();
        edit.putBoolean("deviceadmin", false);
        edit.commit();
        edit.putString("detectionmode", "INTERACTION");
        edit.commit();
    }


}
