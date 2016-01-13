package br.com.albertowd.notificationdrain.util;

import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import br.com.albertowd.notificationdrain.R;
import br.com.albertowd.notificationdrain.activities.FilterNotificationActivity;

public class Filter {
    /**
     * Key word EditText.
     */
    private final EditText etFilter;

    /**
     * TextWatcher to validate filter.
     */
    private final FilterTextWatcher textWatcher;

    /**
     * Wrapper LinearLayout.
     */
    private final LinearLayout llFilter;

    /**
     * ToggleButton to set the word to contains or not in the regex.
     */
    private final ToggleButton tbFilter;

    /**
     * Default constructor.
     *
     * @param llFilter Layout with the desired children.
     */
    public Filter(LinearLayout llFilter) {
        this.llFilter = llFilter;
        etFilter = (EditText) this.llFilter.findViewById(R.id.etFilter);
        tbFilter = (ToggleButton) this.llFilter.findViewById(R.id.tbFilter);
        textWatcher = new FilterTextWatcher((FilterNotificationActivity) this.llFilter.getContext(), etFilter);

        tbFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((FilterNotificationActivity) Filter.this.llFilter.getContext()).updateFilters(true);
            }
        });
        etFilter.requestFocus();
    }

    /**
     * @return True if the word needs to be contained in the notification.
     */
    public boolean hasToContain() {
        return tbFilter.isChecked();
    }

    /**
     * @return True if there is no error in the filter.
     */
    public boolean isValid() {
        return etFilter.getText().length() > 0;
    }

    /**
     * @return The wrapper layout.
     */
    public LinearLayout getLayout() {
        return llFilter;
    }

    /**
     * @return The actual key word.
     */
    public String getText() {
        return etFilter.getText().toString().trim();
    }

    /**
     * Set if the regex has to contains the key word.
     *
     * @param hasToContains True to contains, false to not contains.
     */
    public void setHasToContains(boolean hasToContains) {
        tbFilter.setChecked(hasToContains);
    }

    /**
     * Set a new key word to the EditText.
     *
     * @param text New key word.
     */
    public void setText(String text) {
        etFilter.setText(text);
    }
}
