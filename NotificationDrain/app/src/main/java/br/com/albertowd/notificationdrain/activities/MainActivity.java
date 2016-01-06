package br.com.albertowd.notificationdrain.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import br.com.albertowd.notificationdrain.R;
import br.com.albertowd.notificationdrain.settings.DrainSettings;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Defines with view is the default to call.
        Intent intent;
        if (DrainSettings.VIEW_MODE.ADVANCED == new DrainSettings(this).getViewMode())
            intent = new Intent(this, RegexNotificationActivity.class);
        else
            intent = new Intent(this, FilterNotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        super.startActivity(intent);
    }
}
