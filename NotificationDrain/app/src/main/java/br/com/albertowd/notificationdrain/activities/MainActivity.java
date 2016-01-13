package br.com.albertowd.notificationdrain.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.albertowd.notificationdrain.R;
import br.com.albertowd.notificationdrain.settings.DrainSettings;

public class MainActivity extends Activity {

    /**
     * Application settings utility class.
     */
    private DrainSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);

        settings = new DrainSettings(this);

        // Set the go to notification access settings listener.
        super.findViewById(R.id.btNotificationAccess).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.goToNotificationAccessSettings();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (settings.hasServiceAccess()) {
            // Defines with view is the default to call.
            Intent intent;
            if (DrainSettings.VIEW_MODE.ADVANCED == settings.getViewMode())
                intent = new Intent(this, RegexNotificationActivity.class);
            else
                intent = new Intent(this, FilterNotificationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            super.startActivity(intent);
        }
    }
}
