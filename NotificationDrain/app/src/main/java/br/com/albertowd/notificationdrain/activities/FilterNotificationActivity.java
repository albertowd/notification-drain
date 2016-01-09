package br.com.albertowd.notificationdrain.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import br.com.albertowd.notificationdrain.R;
import br.com.albertowd.notificationdrain.settings.DrainSettings;
import br.com.albertowd.notificationdrain.util.Filter;
import br.com.albertowd.notificationdrain.util.FilterTextWatcher;

public class FilterNotificationActivity extends Activity {

    /**
     * Go to notification access settings button.
     */
    private Button btNotificationAccess;

    /**
     * Remove a EditText filter.
     */
    private Button btRemoveFilter;

    /**
     * Checkbox to set the service running or not.
     */
    private CheckBox cbService;

    /**
     * Application settings utility class.
     */
    private DrainSettings settings;

    /**
     * First and required filter.
     */
    private Filter llFilter0;

    /**
     * Layout to add new EditText filters.
     */
    private LinearLayout llKeyList;

    /**
     * List of filters in the screen.
     */
    private List<Filter> filters;

    /**
     * If possible, add a new filter to the screen.
     *
     * @return If was possible to append the new filter.
     */
    private boolean appendFilter() {
        boolean canAppend = llFilter0.isValid();
        canAppend |= !filters.isEmpty() && filters.get(filters.size() - 1).isValid();
        if (canAppend) {
            Filter llFilter = new Filter((LinearLayout) this.getLayoutInflater().inflate(R.layout.layout_filter, null));
            llKeyList.addView(llFilter.getLayout(), llKeyList.getChildCount() - 1);
            filters.add(llFilter);
        }

        btRemoveFilter.setEnabled(!filters.isEmpty());
        return canAppend;
    }

    /**
     * Remove the last filter in the screen.
     */
    private void removeFilter() {
        if (!filters.isEmpty()) {
            Filter llFilter = filters.get(filters.size() - 1);
            llKeyList.removeView(llFilter.getLayout());
            filters.remove(filters.size() - 1);
        }

        btRemoveFilter.setEnabled(!filters.isEmpty());
    }

    /**
     * Setup the listener of the buttons plus make the regex listener of the EditText.
     */
    private void setupListeners() {
        // Set the go to notification access settings listener.
        btNotificationAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.goToNotificationAccessSettings();
            }
        });

        // Set the remove key button listener.
        btRemoveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFilter();
            }
        });

        // Set the service running CheckBox listener.
        cbService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (settings.setServiceEnabled(isChecked)) {
                    cbService.setChecked(true);
                    cbService.setText(FilterNotificationActivity.this.getText(R.string.service_on));
                } else {
                    cbService.setChecked(false);
                    cbService.setText(FilterNotificationActivity.this.getText(R.string.service_off));
                }
            }
        });

        // Set the add key button listener.
        super.findViewById(R.id.btAddFilter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendFilter();
            }
        });

        // Set the advanced mode button listener.
        super.findViewById(R.id.btSwitchToAdvancedMode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterNotificationActivity.this, RegexNotificationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                FilterNotificationActivity.super.startActivity(intent);
            }
        });
    }

    /**
     * Clear and updates the filters from shared settings.
     */
    private void updateFilters() {
        while (!filters.isEmpty())
            this.removeFilter();

        List<String> keyWords = settings.getKeyWords();
        List<String> notKeyWords = settings.getNotKeyWords();

        llFilter0.setText("");

        if (!keyWords.isEmpty()) {
            llFilter0.setText(keyWords.get(0));
            keyWords.remove(0);

            for (String key : keyWords) {
                if (this.appendFilter())
                    filters.get(filters.size() - 1).setText(key);
            }
        }

        if (!notKeyWords.isEmpty()) {
            if (llFilter0.getText() == "") {
                llFilter0.setText(keyWords.get(0));
                keyWords.remove(0);
            }
            for (String key : notKeyWords) {
                if (this.appendFilter()) {
                    Filter filter = filters.get(filters.size() - 1);
                    filter.setHasToContains(false);
                    filter.setText(key);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_filter_notification);

        btNotificationAccess = (Button) super.findViewById(R.id.btNotificationAccess);
        btRemoveFilter = (Button) super.findViewById(R.id.btRemoveFilter);
        cbService = (CheckBox) super.findViewById(R.id.cbService);
        llFilter0 = new Filter((LinearLayout) super.findViewById(R.id.llFilter0));
        llKeyList = (LinearLayout) super.findViewById(R.id.llKeyList);
        filters = new ArrayList<>();
        settings = new DrainSettings(this);

        this.setupListeners();
        settings.setViewMode(DrainSettings.VIEW_MODE.BASIC);
    }

    @Override
    protected void onResume() {
        super.onResume();

        cbService.setChecked(settings.isServiceEnabled());

        int notificationAccessVisibility = settings.hasServiceAccess() ? View.GONE : View.VISIBLE;
        btNotificationAccess.setVisibility(notificationAccessVisibility);
        super.findViewById(R.id.tvNotificationHelp).setVisibility(notificationAccessVisibility);

        this.updateFilters();
    }

    /**
     * Get the valid filters to make the key words list regex and save it in the settings.
     */
    public void validateFilters() {
        List<String> keyWords = new ArrayList<>();
        List<String> notKeyWords = new ArrayList<>();
        if (llFilter0.isValid()) {
            if (llFilter0.hasToContain())
                keyWords.add(llFilter0.getText());
            else
                notKeyWords.add(llFilter0.getText());
            for (Filter filter : filters) {
                if (filter.isValid()) {
                    if (filter.hasToContain())
                        keyWords.add(filter.getText());
                    else
                        notKeyWords.add(filter.getText());
                }
            }
        }
        settings.setKeyWordFilters(keyWords, notKeyWords);
    }
}
