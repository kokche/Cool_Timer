package com.example.cooltimer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {
    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.fragment_xml);
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();
        for (int i = 0; i < count; ++i){
            Preference preference = preferenceScreen.getPreference(i);
            if (preference instanceof ListPreference){
                setLabel(preference,sharedPreferences.getString(preference.getKey(),""));
            }else if (preference instanceof EditTextPreference){
                setEditLabel(preference);
            }
        }
        Preference preference = findPreference("default_value");
        preference.setOnPreferenceChangeListener(this);
    }

    private void setLabel(Preference preference,String value) {
        ListPreference listPreference = (ListPreference) preference;
        int index = listPreference.findIndexOfValue(value);
        if (index >=0){
            listPreference.setSummary(listPreference.getEntries()[index]);
        }
    }
    private void setEditLabel(Preference preference) {
        EditTextPreference editTextPreference = (EditTextPreference) preference;
        editTextPreference.setSummary(editTextPreference.getText());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (preference instanceof ListPreference){
            setLabel(preference,sharedPreferences.getString(preference.getKey(),""));
        }else if (preference instanceof EditTextPreference){
            setEditLabel(preference);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        Toast toast = Toast.makeText(getContext(),"Value is not correct",Toast.LENGTH_LONG);
        if (preference.getKey().equals("default_value")) {
            try {
                if (Integer.parseInt(newValue.toString()) > 1500){
                    Toast.makeText(getContext(),"Value must be less then 1500 sec",Toast.LENGTH_LONG).show();
                    return false;
                }
            } catch (NumberFormatException nfe) {
                toast.show();
                return false;
            }
        }
        return true;
    }
}
