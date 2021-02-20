package com.faridaaidah.consumerapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.faridaaidah.consumerapp.reminder.AlarmReceiver

class SettingsActivity : AppCompatActivity() {
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        alarmReceiver = AlarmReceiver()
    }

    class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {
        private lateinit var alarmStatus: String
        private lateinit var alarmPreference: SwitchPreference
        private lateinit var alarmReceiver: AlarmReceiver

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
            init()
            setSummaries()
            alarmReceiver = AlarmReceiver()
        }

        private fun init() {
            alarmStatus = resources.getString(R.string.switch_preference)
            alarmPreference = findPreference<SwitchPreference>(alarmStatus) as SwitchPreference
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
            if (key == alarmStatus) {
                if (alarmPreference.isChecked) {
                    val repeatMessage = getString(R.string.alarm_message)
                    alarmPreference.isChecked = true
                    context?.let {
                        alarmReceiver.setRepeatingAlarm(
                            it,
                            AlarmReceiver.TYPE_REPEATING,
                            repeatMessage
                        )
                    }
                } else {
                    alarmPreference.isChecked = false
                    context?.let { alarmReceiver.cancelAlarm(it) }
                }
            }
        }

        private fun setSummaries() {
            val sh = preferenceManager.sharedPreferences
            alarmPreference.isChecked = sh.getBoolean(alarmStatus, false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar_menu2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_user -> {
                startActivity(Intent(this, MainActivity::class.java))
                return true
            }
            R.id.nav_set -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
        }
        return true
    }
}
