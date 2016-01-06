package br.com.albertowd.notificationdrain.util;

import android.os.Bundle;
import android.service.notification.StatusBarNotification;

public class DrainFilter {

    /**
     * Get a notification and filter it in the message, title and ticker text (if available).
     *
     * @param sbn   StatusBar Notification to filter.
     * @param regex Regex filter to match.
     * @return True if the notification had matched the filter.
     */
    public static boolean notificationMatched(StatusBarNotification sbn, String regex) {
        Bundle extras = sbn.getNotification().extras;
        CharSequence message = extras.getCharSequence("android.text");
        CharSequence tick = sbn.getNotification().tickerText;
        String title = extras.getString("android.title", "");

        boolean tickMatches = false;
        boolean titleMatches = title.matches(regex);
        boolean messageMatches = false;

        if (message != null) {
            String textStr = message.toString();
            messageMatches = textStr.matches(regex);
        }
        if (tick != null) {
            String tickStr = tick.toString();
            tickMatches = tickStr.matches(regex);
        }

        return messageMatches || tickMatches || titleMatches;
    }
}
