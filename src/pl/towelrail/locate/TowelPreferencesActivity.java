package pl.towelrail.locate;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

/**
 * Preferences window activity for basic settings management.
 */
public class TowelPreferencesActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new TowelPreferencesFragment()).commit();
    }

    public static class TowelPreferencesFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            SharedPreferences sharedPreferences =
                    getActivity().getSharedPreferences("PRIVATE_PREFERENCES", MODE_PRIVATE);
            String apiKey = sharedPreferences.getString("api_key", "");

            if (apiKey == null || apiKey.isEmpty()) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Resources resources = getResources();
                String apiKeyFromResources = resources.getString(R.string.api_key);
                editor.putString("api_key", apiKeyFromResources);
                editor.commit();
                apiKey = apiKeyFromResources;
            }
            // TODO: clean up
            PreferenceScreen preferenceManager = getPreferenceScreen();
            EditTextPreference apiKeyEditTextPreference = (EditTextPreference)
                    preferenceManager.findPreference(getResources()
                            .getString(R.string.preferences_route_post_url_edit_key));
            apiKeyEditTextPreference.setText(apiKey);
        }
    }
}
