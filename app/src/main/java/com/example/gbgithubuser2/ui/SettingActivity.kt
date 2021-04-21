package com.example.gbgithubuser2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gbgithubuser2.Alarm.AlarmReceiver
import com.example.gbgithubuser2.R
import com.example.gbgithubuser2.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var alarmReceiver: AlarmReceiver

    companion object {
        private const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.setting_holder, MyPreferenceFragment()).commit()

        alarmReceiver = AlarmReceiver()
    }
}