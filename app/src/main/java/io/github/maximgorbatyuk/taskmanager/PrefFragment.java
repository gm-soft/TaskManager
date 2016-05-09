package io.github.maximgorbatyuk.taskmanager;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;

import io.github.maximgorbatyuk.taskmanager.help.IntEditTextPreference;
import io.github.maximgorbatyuk.taskmanager.help.PreferencesHelper;

public class PrefFragment extends PreferenceFragment {


    PreferencesHelper preferencesHelper;
    CheckBoxPreference enableNotification;
    IntEditTextPreference editWorkHours;
    IntEditTextPreference editWorkDays;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_preference);

        enableNotification  = (CheckBoxPreference) findPreference("notifications");
        editWorkHours       = (IntEditTextPreference) findPreference("work_hours");
        editWorkDays        = (IntEditTextPreference) findPreference("work_days");

        preferencesHelper = new PreferencesHelper(getActivity().getApplicationContext());
        loadSettings();
    }


    private void saveSettings(){
        boolean enableNotifications = enableNotification.isChecked();
        String editWorkHoursText = editWorkHours.getText();
        String editWorkDaysText = editWorkDays.getText();

        int workHours = !editWorkHoursText.isEmpty() ? Integer.parseInt(editWorkHoursText) : 8;
        int workDays = !editWorkDaysText.isEmpty() ? Integer.parseInt(editWorkDaysText) : 5;

        preferencesHelper.SaveSettings(enableNotifications, workHours, workDays);

    }

    private void loadSettings(){
        enableNotification.setChecked(preferencesHelper.getEnableNotifications() );
        editWorkHours.setText( String.valueOf(preferencesHelper.getWorkHours()) );
        editWorkDays.setText( String.valueOf(preferencesHelper.getWorkDays()) );
    }

    @Override
    public void onStop() {
        saveSettings();
        super.onStop();
    }

}
