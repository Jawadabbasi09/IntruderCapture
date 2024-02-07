package com.example.intrudercapture;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;

public class Utility {
    public static boolean isMyServiceRunning(Context context, Class<?> cls) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasLuckypatcher(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return isPackageInstalled("ru.aaaaaaad.installer", packageManager) || isPackageInstalled("ru.sxbuIDfx.pFSOyagrF", packageManager) || isPackageInstalled("ru.HUounqZv.qGDvALdrY", packageManager) || isPackageInstalled("ru.yFarPSsi.lSWLCBgGE", packageManager) || isPackageInstalled("ru.auLSaZJK.OldqqVPqY", packageManager) || isPackageInstalled("ru.HvZVLLax.FuBLzbTId", packageManager) || isPackageInstalled("ru.FxCVdppm.yVDnvQgJU", packageManager) || isPackageInstalled("ru.oCHfhtgN.LaiQlIeIK", packageManager) || isPackageInstalled("ru.ohHbeFjR.uZvxvLPnK", packageManager) || isPackageInstalled("ru.oSFnVIfs.fUUFExgWn", packageManager) || isPackageInstalled("ru.PDOIPrWH.abjKeIKLW", packageManager) || isPackageInstalled("ru.UaLzEHLI.yXTTBtSFW", packageManager) || isPackageInstalled("ru.uBVJgfKc.udsaLjziD", packageManager) || isPackageInstalled("com.chelpus.lackypatch", packageManager) || isPackageInstalled("com.dimonvideo.luckypatcher", packageManager) || isPackageInstalled("com.luckypatchers.luckypatcherinstaller", packageManager) || isPackageInstalled("com.android.vending.billing.InAppBillingService.LACK", packageManager) || isPackageInstalled("com.android.vending.billing.InAppBillingService.COIN", packageManager) || isPackageInstalled("com.android.vending.billing.InAppBillingService.LOCK", packageManager) || isPackageInstalled("com.android.vending.billing.InAppBillingService.CRAC", packageManager) || isPackageInstalled("com.android.vending.billing.InAppBillingService.COIO", packageManager);
    }

    public static boolean isPackageInstalled(String str, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(str, 0);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }
}