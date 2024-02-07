package com.example.intrudercapture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class PhoneUnlockedReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }
        String action = intent.getAction();
        char c = 65535;
        int hashCode = action.hashCode();
        if (hashCode != -2128145023) {
            if (hashCode != -1454123155) {
                if (hashCode == 823795052 && action.equals("android.intent.action.USER_PRESENT")) {
                    c = 0;
                }
            } else if (action.equals("android.intent.action.SCREEN_ON")) {
                c = 2;
            }
        } else if (action.equals("android.intent.action.SCREEN_OFF")) {
            c = 1;
        }
        if (c != 0) {
            return;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("IntruderButton", 0);
        sharedPreferences.edit().putInt("failedcount", 1).commit();
//        CameraManager.getInstance(context).takePhoto();

            //postDataUsingVolley(context, sharedPreferences.getString("mail_address", null));

    }

    int get_size(Context context) {
        File dir = getDir(context);
        dir.mkdirs();
        File[] listFiles = dir.listFiles();
        if (listFiles != null && listFiles.length > 1) {
            Collections.sort(Arrays.asList(listFiles), new Comparator<File>() { // from class: com.foxbytecode.captureintruder.receiver.PhoneUnlockedReceiver.1
                @Override // java.util.Comparator
                public int compare(File file, File file2) {
                    long lastModified = file.lastModified();
                    long lastModified2 = file2.lastModified();
                    if (lastModified2 < lastModified) {
                        return -1;
                    }
                    return lastModified > lastModified2 ? 1 : 0;
                }
            });
        }
        if (listFiles != null) {
            return listFiles.length;
        }
        return 0;
    }

    private File getDir(Context context) {
        return new File(context.getFilesDir(), "IntruderCapure");
    }

//    private void postDataUsingVolley(Context context, String str) {
//        HashMap hashMap = new HashMap();
//        hashMap.put("email", str);
//        hashMap.put("subject", "Intruder Detected");
//        hashMap.put("body", "Someone tried to unlock your phone.");
//        hashMap.put("key", get_key());
//        Volley.newRequestQueue(context).add(new JsonObjectRequest(1, "https://www.foxbytecode.com/api/mail", new JSONObject(hashMap), new Response.Listener<JSONObject>() { // from class: com.foxbytecode.captureintruder.receiver.PhoneUnlockedReceiver.2
//            @Override // com.android.volley.Response.Listener
//            public void onResponse(JSONObject jSONObject) {
//            }
//        }, new Response.ErrorListener() { // from class: com.foxbytecode.captureintruder.receiver.PhoneUnlockedReceiver.3
//            @Override // com.android.volley.Response.ErrorListener
//            public void onErrorResponse(VolleyError volleyError) {
//                volleyError.printStackTrace();
//            }
//        }));
//    }

//    String get_key() {
//        return reverse(decrease(decrease("c#qWTyE269BW")));
//    }

//    static String decrease(String str) {
//        StringBuilder sb = new StringBuilder();
//        for (char c : str.toCharArray()) {
//            sb.append((char) (c - 1));
//        }
//        return sb.toString();
//    }
//
//    static String reverse(String str) {
//        return new StringBuilder(str).reverse().toString();
//    }
}

