package com.guluwa.gulumusicpro.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import com.guluwa.gulumusicpro.manage.MyApplication;

public class PreferenceUtil {

    private static PreferenceUtil sInstance;

    private final SharedPreferences mPreferences;

    private PreferenceUtil(@NonNull final Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PreferenceUtil getInstance() {
        if (sInstance == null) {
            sInstance = new PreferenceUtil(MyApplication.Companion.getContext());
        }
        return sInstance;
    }

    public static final String GAPLESS_PLAYBACK = "gapless_playback";

    public final boolean gaplessPlayback() {
        return mPreferences.getBoolean(GAPLESS_PLAYBACK, false);
    }

    public void registerOnSharedPreferenceChangedListener(
            SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener) {
        mPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    public void unregisterOnSharedPreferenceChangedListener(
            SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener) {
        mPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    public static final String CLASSIC_NOTIFICATION = "classic_notification";

    public final boolean classicNotification() {
        return mPreferences.getBoolean(CLASSIC_NOTIFICATION, false);
    }

    public void setClassicNotification(final boolean value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(CLASSIC_NOTIFICATION, value);
        editor.apply();
    }

    public static final String ALBUM_ART_ON_LOCKSCREEN = "album_art_on_lockscreen";

    public static final String BLURRED_ALBUM_ART = "blurred_album_art";

    public static final String COLORED_NOTIFICATION = "colored_notification";

    public static final String DOMINANT_COLOR = "dominant_color";

    public static final String TOGGLE_HEADSET = "toggle_headset";

    public boolean getHeadsetPlugged() {
        return mPreferences.getBoolean(TOGGLE_HEADSET, false);
    }

    private static final String AUDIO_DUCKING = "audio_ducking";

    public final boolean audioDucking() {
        return mPreferences.getBoolean(AUDIO_DUCKING, true);
    }

    private static final String TOGGLE_SHUFFLE = "toggle_shuffle";

    public boolean isShuffleModeOn() {
        return mPreferences.getBoolean(TOGGLE_SHUFFLE, false);
    }

    public static final String TOGGLE_HOME_BANNER = "toggle_home_banner";

    public final boolean isHomeBanner() {
        return mPreferences.getBoolean(TOGGLE_HOME_BANNER, false);
    }

    public static final String BANNER_IMAGE_PATH = "banner_image_path";

    public String getBannerImage() {
        return mPreferences.getString(BANNER_IMAGE_PATH, "");
    }

    public static final String PROFILE_IMAGE_PATH = "profile_image_path";

    public void saveProfileImage(String profileImagePath) {
        mPreferences.edit().putString(PROFILE_IMAGE_PATH, profileImagePath).apply();
    }

    public String getProfileImage() {
        return mPreferences.getString(PROFILE_IMAGE_PATH, "");
    }

    public static final String TOGGLE_FULL_SCREEN = "toggle_full_screen";

    public boolean getFullScreenMode() {
        return mPreferences.getBoolean(TOGGLE_FULL_SCREEN, false);
    }

    public void setFullScreenMode(int newValue) {
        mPreferences.edit().putInt(TOGGLE_FULL_SCREEN, newValue).apply();
    }
}
