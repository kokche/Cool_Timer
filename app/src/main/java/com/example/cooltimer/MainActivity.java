package com.example.cooltimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    SeekBar time;
    Button Start;
    TextView timer;
    CountDownTimer countDownTimer;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        time =  findViewById(R.id.seekBar2);
        Start = findViewById(R.id.button);
        timer = findViewById(R.id.editText);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        time.setMax(1500);
        setDefaultValueFromPreference(sharedPreferences);

        time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);
                String firstVar = progress / 60 < 10 ? "0" + progress / 60 : String.valueOf(progress / 60) ;
                String secondVar = progress % 60 < 10 ? "0" + progress % 60 : String.valueOf(progress % 60) ;
                timer.setText(firstVar + ":" + secondVar);

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void setDefaultValueFromPreference(SharedPreferences sharedPreferences) {
        int progress=0;
        try {
            progress = Integer.parseInt(sharedPreferences.getString("default_value","300"));
        }catch (NumberFormatException nfc){
            Toast.makeText(this,"Some EX",Toast.LENGTH_SHORT).show();
        }
        time.setProgress(progress);
        String firstVar = progress / 60 < 10 ? "0" + progress / 60 : String.valueOf(progress / 60) ;
        String secondVar = progress % 60 < 10 ? "0" + progress % 60 : String.valueOf(progress % 60) ;
        timer.setText(firstVar + ":" + secondVar);
    }

    public void ButtonClick(View view) {
        if (time.isEnabled()){
            time.setEnabled(false);
            Start.setText("Stop");
            countDownTimer = new CountDownTimer(time.getProgress()*1000,1000) {
                @Override
                public void onTick(long millisUntilFinished){
                    time.setProgress((int)millisUntilFinished/1000);
                    Log.d("mil",millisUntilFinished+"");
                }
                @Override
                public void onFinish() {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String sound = sharedPreferences.getString("soundOfBell","bell");;
                    if (sharedPreferences.getBoolean("enabled_sound", true)) {
                        if (sound.equals("bell")) {
                            MediaPlayer mew = MediaPlayer.create(getApplicationContext(), R.raw.original);
                            mew.start();
                        }else {
                            MediaPlayer mew = MediaPlayer.create(getApplicationContext(), R.raw.original);
                            mew.start();
                        }
                    }
                    resetTimer();
                }
            };
            countDownTimer.start();
        }else{
            resetTimer();
            countDownTimer.cancel();
        }
    }

    private void resetTimer() {
        time.setEnabled(true);
        Start.setText("Start");
        setDefaultValueFromPreference(sharedPreferences);
        //timer.setText("05:00");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("default_value"))
            setDefaultValueFromPreference(sharedPreferences);
    }
}
