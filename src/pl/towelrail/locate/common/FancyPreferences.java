package pl.towelrail.locate.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;
import java.util.Set;

public class FancyPreferences {
    private FancyPreferences() {
    }

    public static <T> boolean edit(Context ctx, Class<T> objClass, T obj, String name) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preferences.edit();

        if (objClass.equals(String.class)) {
            editor.putString(name, String.valueOf(obj));
        } else if (objClass.equals(Boolean.class)) {
            editor.putBoolean(name, (Boolean) obj);
        } else if (objClass.equals(Float.class)) {
            editor.putFloat(name, (Float) obj);
        } else if (objClass.equals(Set.class)) {
            editor.putStringSet(name, (Set<String>) obj);
        } else if (objClass.equals(Integer.class)) {
            editor.putInt(name, (Integer) obj);
        } else if (objClass.equals(Long.class)) {
            editor.putLong(name, (Long) obj);
        }

        editor.commit();

        return preferences.contains(name);
    }

    public static <T> T get(Context ctx, Class<T> objClass, String name, T defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);

        Map<String, ?> allPreferences = preferences.getAll();

        for (Map.Entry entry : allPreferences.entrySet()) {
            if (entry.getKey().equals(name) && entry.getValue().getClass().equals(objClass)) {
                return objClass.cast(entry.getValue());
            }
        }
        return defaultValue;
    }
}
