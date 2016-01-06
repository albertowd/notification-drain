package br.com.albertowd.notificationdrain.services;

import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import br.com.albertowd.notificationdrain.settings.DrainSettings;
import br.com.albertowd.notificationdrain.util.DrainFilter;

public class DrainService extends NotificationListenerService {

    /**
     * Application settings utility class.
     */
    private DrainSettings settings;

    @Override
    public void onCreate() {
        super.onCreate();
        settings = new DrainSettings(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // Let's check if the service can drain this notification.
        if (settings.isServiceEnabled() && sbn.isClearable()) {
            // Now it's time to check if the notification does not match the filter.
            if (!DrainFilter.notificationMatched(sbn, settings.getRegexFilter())) {
                // If it is not, let's cancel the notification.
                if (Build.VERSION.SDK_INT >= 21)
                    this.cancelNotification(sbn.getKey());
                else
                    this.cancelNotification(sbn.getPackageName(), sbn.getTag(), sbn.getId());

            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // Does nothing.
    }
}
