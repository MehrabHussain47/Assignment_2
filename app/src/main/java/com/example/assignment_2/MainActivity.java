package com.example.assignment_2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    private RadioGroup moodRadioGroup;
    private CheckBox exerciseCheckBox, socializeCheckBox, workCheckBox, relaxCheckBox;
    private SeekBar energySeekBar;
    private RatingBar satisfactionRatingBar;
    private Switch nightModeSwitch;
    private Button submitButton;
    private TextView energyPercentageTextView;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        boolean isNightModeOn = sharedPreferences.getBoolean("NightMode", false);
        if (isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_main);

        moodRadioGroup = findViewById(R.id.moodRadioGroup);
        exerciseCheckBox = findViewById(R.id.exerciseCheckBox);
        socializeCheckBox = findViewById(R.id.socializeCheckBox);
        workCheckBox = findViewById(R.id.workCheckBox);
        relaxCheckBox = findViewById(R.id.relaxCheckBox);
        energySeekBar = findViewById(R.id.energySeekBar);
        satisfactionRatingBar = findViewById(R.id.satisfactionRatingBar);
        nightModeSwitch = findViewById(R.id.nightModeSwitch);
        submitButton = findViewById(R.id.submitButton);
        energyPercentageTextView = findViewById(R.id.energyPercentageTextView);

        nightModeSwitch.setChecked(isNightModeOn);

        nightModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("NightMode", true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("NightMode", false);
            }
            editor.apply();
        });

        energySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int percentage = (progress * 100) / energySeekBar.getMax();
                energyPercentageTextView.setText("Energy Level: " + percentage + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        submitButton.setOnClickListener(view -> {
            int selectedMoodId = moodRadioGroup.getCheckedRadioButtonId();
            if (selectedMoodId == -1) {
                Toast.makeText(MainActivity.this, "Please select your mood.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!exerciseCheckBox.isChecked() && !socializeCheckBox.isChecked() &&
                    !workCheckBox.isChecked() && !relaxCheckBox.isChecked()) {
                Toast.makeText(MainActivity.this, "Please select at least one activity.", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder summary = new StringBuilder();

            RadioButton selectedMood = findViewById(selectedMoodId);
            summary.append("Mood: ").append(selectedMood.getText()).append("\n");

            summary.append("Activities: ");
            if (exerciseCheckBox.isChecked()) summary.append("Exercise ");
            if (socializeCheckBox.isChecked()) summary.append("Socialize ");
            if (workCheckBox.isChecked()) summary.append("Work ");
            if (relaxCheckBox.isChecked()) summary.append("Relax ");
            summary.append("\n");

            int energyLevelPercentage = (energySeekBar.getProgress() * 100) / energySeekBar.getMax();
            summary.append("Energy Level: ").append(energyLevelPercentage).append("%\n");

            summary.append("Satisfaction: ").append(satisfactionRatingBar.getRating()).append(" stars\n");

            if (nightModeSwitch.isChecked()) {
                summary.append("Night Mode: ON\n");
            } else {
                summary.append("Night Mode: OFF\n");
            }

            Toast.makeText(MainActivity.this, summary.toString(), Toast.LENGTH_LONG).show();
        });
    }
}
