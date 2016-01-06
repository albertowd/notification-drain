package br.com.albertowd.notificationdrain.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import br.com.albertowd.notificationdrain.R;
import br.com.albertowd.notificationdrain.activities.RegexNotificationActivity;
import br.com.albertowd.notificationdrain.settings.DrainSettings;

public class RegexTextWatcher implements TextWatcher {
    /**
     * Parent activity to get the error string.
     */
    private RegexNotificationActivity parent;

    /**
     * Application settings to update the regex filter.
     */
    private DrainSettings settings;

    /**
     * Parent EditText to update it's error string.
     */
    private EditText etFilter;

    /**
     * Default constructor, receives the parent activity or context and a EditText to listen.
     *
     * @param parent   Parent activity to set the filter settings.
     * @param etFilter Filter EditText to listen to changes and validate the filter.
     */
    public RegexTextWatcher(RegexNotificationActivity parent, EditText etFilter) {
        super();
        this.etFilter = etFilter;
        this.parent = parent;

        settings = new DrainSettings(parent);
        etFilter.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Does nothing.
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Does nothing.
    }

    @Override
    public void afterTextChanged(Editable s) {
        // If the filter ir correct, clear the error message.
        if (settings.setRegexFilter(s.toString()))
            etFilter.setError(null);
        else
            etFilter.setError(parent.getString(R.string.regex_error));
    }
}
