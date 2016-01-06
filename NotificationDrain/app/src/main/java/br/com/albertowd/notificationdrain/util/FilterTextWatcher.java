package br.com.albertowd.notificationdrain.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import br.com.albertowd.notificationdrain.R;
import br.com.albertowd.notificationdrain.activities.FilterNotificationActivity;

public class FilterTextWatcher implements TextWatcher {
    /**
     * Parent activity to get the error string.
     */
    private FilterNotificationActivity parent;

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
    public FilterTextWatcher(FilterNotificationActivity parent, EditText etFilter) {
        super();
        this.etFilter = etFilter;
        this.parent = parent;

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
        if (s.toString().trim().isEmpty())
            etFilter.setError(parent.getString(R.string.key_word_error));
        else
            etFilter.setError(null);
        parent.validateFilters();
    }
}
