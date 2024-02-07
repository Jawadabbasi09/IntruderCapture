package com.example.intrudercapture;

import android.app.NotificationManager;
import android.content.Context;

public class NotificationHelper {

    /* loaded from: classes.dex */
    public enum Priority {


        LOW(2, -1);


        private final int higher24;
        private final int lower24;

        Priority(int i, int i2) {
            this.higher24 = i;
            this.lower24 = i2;
        }

        public int getBelow24() {
            return this.lower24;
        }

        public int getAboveAnd24() {
            return this.higher24;
        }
    }

    public static void cancelAll(Context context) {
        try {
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
        } catch (Exception unused) {
        }
    }
}