package br.com.albertowd.notificationdrain.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import br.com.albertowd.notificationdrain.R;
import br.com.albertowd.notificationdrain.settings.DrainSettings;
import br.com.albertowd.notificationdrain.util.RegexTextWatcher;

public class RegexNotificationActivity extends Activity {

    /**
     * Checkbox to set the service running or not.
     */
    private CheckBox cbService;

    /**
     * Application settings utility class.
     */
    private DrainSettings settings;

    /**
     * EditText to change the filter.
     */
    private EditText etFilter;

    /**
     * Setup the listener of the buttons plus make the regex listener of the EditText.
     */
    private void setupListeners() {
        // Set the filter validator.
        new RegexTextWatcher(this, etFilter);

        // Set the service running CheckBox listener.
        cbService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (settings.setServiceEnabled(isChecked)) {
                    cbService.setChecked(true);
                    cbService.setText(RegexNotificationActivity.this.getText(R.string.service_on));
                } else {
                    cbService.setChecked(false);
                    cbService.setText(RegexNotificationActivity.this.getText(R.string.service_off));
                }
            }
        });

        ((TextView) super.findViewById(R.id.tvFilter)).setMovementMethod(LinkMovementMethod.getInstance());

        super.findViewById(R.id.btSwitchToBasicMode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegexNotificationActivity.this, FilterNotificationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                RegexNotificationActivity.super.startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_regex_notification);

        cbService = (CheckBox) super.findViewById(R.id.cbService);
        etFilter = (EditText) super.findViewById(R.id.etFilter);
        settings = new DrainSettings(this);

        this.setupListeners();
        settings.setViewMode(DrainSettings.VIEW_MODE.ADVANCED);
    }

    @Override
    protected void onResume() {
        super.onResume();

        cbService.setChecked(settings.isServiceEnabled());
        etFilter.setText(settings.getRegexFilter());
    }
}
