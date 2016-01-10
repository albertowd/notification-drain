package br.com.albertowd.notificationdrain.util;

import android.app.Notification;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;

public class DrainFilter {

    /**
     * Get a notification and filter it in all the possible texts of (if available).
     *
     * @param sbn   StatusBar Notification to filter.
     * @param regex Regex filter to match.
     * @return True if the notification had matched the filter.
     */
    public static boolean notificationMatched(StatusBarNotification sbn, String regex) {
        String check = "";

        CharSequence tick = sbn.getNotification().tickerText;
        if (tick != null)
            check += tick.toString();

        // If theres extra bundle, search all the strings.
        if (Build.VERSION.SDK_INT >= 19) {
            Bundle bundle = sbn.getNotification().extras;
            check += bundle.getString(Notification.EXTRA_BIG_TEXT, "");
            check += bundle.getString(Notification.EXTRA_INFO_TEXT, "");
            check += bundle.getString(Notification.EXTRA_SUB_TEXT, "");
            check += bundle.getString(Notification.EXTRA_SUMMARY_TEXT, "");
            check += bundle.getString(Notification.EXTRA_TEXT, "");
            check += bundle.getString(Notification.EXTRA_TITLE, "");
            check += bundle.getString(Notification.EXTRA_TITLE_BIG, "");
        }

        return check.matches(regex);
    }
}
