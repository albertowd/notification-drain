package br.com.albertowd.notificationdrain.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class DrainSettings {

    private static final String ACTION_NOFITICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String DEFAULT_VIEW_MODE = "view_mode";
    private static final String REGEX_ANY_CHAR = ".*";
    private static final String REGEX_CASE_INSENSITIVE = "(?i)";
    private static final String REGEX_FILTER = "regex_filter";
    private static final String REGEX_WORD_FINISH = "\\E";
    private static final String REGEX_WORD_START = "\\Q";
    private static final String SERVICE_STATUS_FLAG = "service_status_flag";

    /**
     * Enumerates with view is the default.
     */
    public enum VIEW_MODE {
        ADVANCED,
        BASIC
    }

    /**
     * Context to get the application context.
     */
    private Context context;

    /**
     * Default constructor.
     *
     * @param context Context to get the application context.
     */
    public DrainSettings(Context context) {
        this.context = context;
    }

    /**
     * Verify if the service has access to the notifications.
     *
     * @return True if the service has notification access.
     */
    public boolean hasServiceAccess() {
        String enabledAppList = Settings.Secure.getString(
                context.getApplicationContext().getContentResolver(), ENABLED_NOTIFICATION_LISTENERS);
        return enabledAppList != null && enabledAppList.contains(context.getApplicationContext().getPackageName());
    }

    /**
     * Verify if the service is running and it have notification access.
     *
     * @return True if the service is running.
     */
    public boolean isServiceEnabled() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return settings.getBoolean(SERVICE_STATUS_FLAG, false) && this.hasServiceAccess();
    }

    /**
     * Set the regex filter string.
     *
     * @param regex Regex filter or empty string to set the default filter "\S*".
     * @return True if the regex was validated.
     */
    public boolean setRegexFilter(String regex) {
        boolean validRegex = false;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String regexString = regex.isEmpty() ? REGEX_ANY_CHAR : regex;
        String oldRegex = this.getRegexFilter();

        try {
            Pattern pattern = Pattern.compile(regexString);
            validRegex = true;
        } catch (PatternSyntaxException pse) {
        }

        if (validRegex) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(REGEX_FILTER, regexString);
            editor.apply();
        }

        return validRegex;
    }

    /**
     * Set the service status to running/stopped.
     *
     * @param enabled True to enable the service.
     * @return Return the service status.
     */
    public boolean setServiceEnabled(boolean enabled) {
        boolean reallyEnable = enabled && this.hasServiceAccess();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(SERVICE_STATUS_FLAG, reallyEnable);
        editor.apply();

        return reallyEnable;
    }

    /**
     * Retrieve the list of key words in the regex filter.
     *
     * @return List of words to filter.
     */
    public List<String> getKeyWords() {
        List<String> keyWords = new ArrayList<>();

        String regex = this.getRegexFilter();
        if (regex.equals(REGEX_ANY_CHAR))
            return keyWords;

        regex = regex.replace(REGEX_CASE_INSENSITIVE, "");
        String[] splitted = regex.split("\\|");

        for (String reg : splitted)
            keyWords.add(reg.replace("(" + REGEX_ANY_CHAR + "?" + REGEX_WORD_START, "").replace(REGEX_WORD_FINISH + REGEX_ANY_CHAR + ")", ""));
        return keyWords;
    }

    /**
     * Return the last saved regex filter or the default filter.
     *
     * @return The last saved regex filter or the default filter.
     */
    public String getRegexFilter() {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getString(REGEX_FILTER, REGEX_ANY_CHAR);
    }

    /**
     * Return the preffered view mode.
     *
     * @return The mode of view of the user choice or none.
     */
    public VIEW_MODE getViewMode() {
        int viewMode = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getInt(DEFAULT_VIEW_MODE, 0);
        return 1 == viewMode ? VIEW_MODE.ADVANCED : VIEW_MODE.BASIC;
    }

    /**
     * Start the Notification Access Settings activity.
     */
    public void goToNotificationAccessSettings() {
        if (Build.VERSION.SDK_INT >= 22)
            context.startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
        else
            context.startActivity(new Intent(ACTION_NOFITICATION_LISTENER_SETTINGS));
    }

    /**
     * Sets the new mode of view of the application at start up.
     *
     * @param viewMode New view mode.
     */
    public void setViewMode(VIEW_MODE viewMode) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).edit();
        editor.putInt(DEFAULT_VIEW_MODE, VIEW_MODE.ADVANCED == viewMode ? 1 : 0);
        editor.apply();
    }

    /**
     * Set a list of key words to the regex filter.
     *
     * @param keyWords List of key words.
     */
    public void setKeyWordFilters(List<String> keyWords) {
        String regex = "";
        for (String word : keyWords)
            regex += "|(" + REGEX_ANY_CHAR + "?" + REGEX_WORD_START + word + REGEX_WORD_FINISH + REGEX_ANY_CHAR + ")";
        this.setRegexFilter(REGEX_CASE_INSENSITIVE + regex.replaceFirst("\\|", ""));
    }
}
