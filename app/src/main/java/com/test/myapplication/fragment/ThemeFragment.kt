package com.test.myapplication.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.myapplication.R
import com.test.myapplication.databinding.FragmentThemeBinding

class ThemeFragment : DialogFragment() {
    private lateinit var binding: FragmentThemeBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentThemeBinding.inflate(LayoutInflater.from(requireContext()))
        val view = binding.root

        val sharedPreferences =
            requireContext().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val sharedPrefEditor = sharedPreferences.edit()

        // Initialize the checked state based on shared preferences
        val isDarkMode = sharedPreferences.getBoolean("darkMode", false)
        if (isDarkMode) {
            binding.darkThemeButton.isChecked = true
        } else {
            binding.lightThemeButton.isChecked = true
        }

        binding.themeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.lightThemeButton -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    sharedPrefEditor.putBoolean("darkMode", false)
                }

                R.id.darkThemeButton -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    sharedPrefEditor.putBoolean("darkMode", true)
                    sharedPrefEditor.putString("chosenTheme", "dark")
                }

                R.id.blackThemeButton -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    sharedPrefEditor.putBoolean("darkMode", true)
                    sharedPrefEditor.putString("chosenTheme", "black")
                }
            }
            sharedPrefEditor.apply()
        }

        if (binding.themeRadioGroup.checkedRadioButtonId == View.NO_ID) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Choose Theme")
            .setView(view)
            .setPositiveButton("OK", null)
            .setNegativeButton("Cancel") { _, _ -> dismiss() }
            .create()
    }
}
