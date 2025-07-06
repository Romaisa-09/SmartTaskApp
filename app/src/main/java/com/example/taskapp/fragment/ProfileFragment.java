package com.example.taskapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.taskapp.R;

public class ProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View v,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(v, savedInstanceState);

        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("profile", Context.MODE_PRIVATE);

        EditText etName   = v.findViewById(R.id.etName);
        EditText etEmail  = v.findViewById(R.id.etEmail);
        EditText etAge    = v.findViewById(R.id.etAge);
        RadioGroup rg     = v.findViewById(R.id.rgGender);
        Switch swTheme    = v.findViewById(R.id.swTheme);
        Button btnSave    = v.findViewById(R.id.btnSaveProfile);

        // Load
        etName.setText(prefs.getString("name", ""));
        etEmail.setText(prefs.getString("email", ""));
        etAge.setText(prefs.getString("age", ""));

        String gender = prefs.getString("gender", "Male");
        int genderId = gender.equals("Female") ? R.id.rbFemale : R.id.rbMale;
        ((RadioButton) v.findViewById(genderId)).setChecked(true);

        boolean dark = prefs.getBoolean("dark", false);
        swTheme.setChecked(dark);
        AppCompatDelegate.setDefaultNightMode(
                dark
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );

        // Listeners
        swTheme.setOnCheckedChangeListener((btn, on) -> {
            prefs.edit().putBoolean("dark", on).apply();
            AppCompatDelegate.setDefaultNightMode(
                    on
                            ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        btnSave.setOnClickListener(x -> {
            prefs.edit()
                    .putString("name", etName.getText().toString())
                    .putString("email", etEmail.getText().toString())
                    .putString("age", etAge.getText().toString())
                    .putString(
                            "gender",
                            rg.getCheckedRadioButtonId() == R.id.rbFemale
                                    ? "Female"
                                    : "Male"
                    )
                    .apply();
            Toast.makeText(requireContext(), "Profile saved", Toast.LENGTH_SHORT).show();
        });
    }
}
