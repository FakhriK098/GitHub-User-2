package com.example.gbgithubuser2.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import com.example.gbgithubuser2.Alarm.AlarmReceiver
import com.example.gbgithubuser2.R

class MyPreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var ALARM : String

    private lateinit var isAlarmNotif: SwitchPreference
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        alarmReceiver = AlarmReceiver()

        init()
        initSharedPreferences()
    }

    private fun initSharedPreferences() {
        val sh = preferenceManager.sharedPreferences
        isAlarmNotif.isChecked = sh.getBoolean(ALARM,false)
    }

    private fun init() {
        ALARM = getString(R.string.key_notif)
        isAlarmNotif = findPreference<SwitchPreference>(ALARM) as SwitchPreference
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == ALARM){
            if (sharedPreferences != null){
                isAlarmNotif.isChecked = sharedPreferences.getBoolean(ALARM, false)
            }
        }

        val state : Boolean = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(ALARM, false)

        setReminder(state)
    }

    private fun setReminder(state: Boolean) {
        if (state){
            context?.let { alarmReceiver.setRepeatingAlarm(it) }
        }else{
            context?.let { alarmReceiver.cancelAlarm(it) }
        }
    }
}