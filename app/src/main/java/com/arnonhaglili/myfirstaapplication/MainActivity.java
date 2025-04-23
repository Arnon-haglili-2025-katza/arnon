package com.arnonhaglili.myfirstaapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final int START_GAME = 222;

    Button b1, b2, b3, b4, b5;
    TextView tv1;
    Switch sw;
    SeekBar sb;
    ConstraintLayout constraintLayout;
    ImageView iv, iv2;
    private MediaPlayer mediaPlayer;
    private Button playPauseButton;
    private boolean isPlaying = false;
    private SharedPreferences sharedPreferences;
    private BroadcastReceiver wifiReceiver;

    // 🔠 Translator של ML Kit
    private Translator englishGermanTranslator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playPauseButton = findViewById(R.id.playPauseButton);
        sharedPreferences = getSharedPreferences("MusicPrefs", MODE_PRIVATE);

        isPlaying = sharedPreferences.getBoolean("isPlaying", false);
        if (isPlaying) {
            startMusic();
        }

        playPauseButton.setOnClickListener(v -> {
            if (isPlaying) {
                pauseMusic();
            } else {
                startMusic();
            }
        });

        initViews();
        wifiReceiver = new MyWiFiBroadcastReceiver();

        // 🔠 התחלת הגדרת Translator של ML Kit
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.GERMAN)
                .build();

        englishGermanTranslator = Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        englishGermanTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(unused -> {
                    englishGermanTranslator.translate("Hello, how are you?")
                            .addOnSuccessListener(translatedText -> {
                                Toast.makeText(MainActivity.this, "מתורגם: " + translatedText, Toast.LENGTH_LONG).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(MainActivity.this, "שגיאה בתרגום: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "שגיאה בהורדת מודל: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void startMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.music_file);
            mediaPlayer.setLooping(true);
        }
        mediaPlayer.start();
        isPlaying = true;
        playPauseButton.setText("עצור");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isPlaying", isPlaying);
        editor.apply();
    }

    private void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            isPlaying = false;
            playPauseButton.setText("הפעל");

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isPlaying", isPlaying);
            editor.apply();
        }
    }

    private void initViews() {
        sw = findViewById(R.id.sw);
        sw.setOnCheckedChangeListener(this);

        constraintLayout = findViewById(R.id.constraintLayout);
        b1 = findViewById(R.id.btnClickListener1);
        b1.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "clickListener1", Toast.LENGTH_LONG).show();
        });

        iv = findViewById(R.id.iv);
        iv2 = findViewById(R.id.iv2);

        b2 = findViewById(R.id.btnClickListener2);
        b2.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "clickListener2", Toast.LENGTH_LONG).show();
        });

        b3 = findViewById(R.id.b3);
        b3.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, LinearActivity.class);
            startActivity(intent);
            finish();
        });

        b4 = findViewById(R.id.b4);
        b4.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, FrameActivity.class);
            startActivityForResult(intent, START_GAME);
        });

        b5 = findViewById(R.id.b5);
        b5.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, GuessActivity.class);
            startActivityForResult(intent, START_GAME);
        });

        sb = findViewById(R.id.sb);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float alpha = (float) i / 100;
                float beta = 1 - (float) i / 100;
                iv.setAlpha(alpha);
                iv2.setAlpha(beta);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isPlaying", true);
            editor.apply();
        }

        unregisterReceiver(wifiReceiver);
    }

    @Override
    protected void onStop() {
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // 🧼 סגירת המתרגם כדי לשחרר משאבים
        if (englishGermanTranslator != null) {
            englishGermanTranslator.close();
        }
    }

    @Override
    protected void onStart() {
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == START_GAME) {
            if (resultCode == RESULT_OK && data != null) {
                int numGuesses = data.getIntExtra("num_guesses", -1);
                String userName = data.getStringExtra("user_name");
                Toast.makeText(this, "Game Finished! Number of guesses: " + numGuesses + ", User: " + userName, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Game was canceled or did not finish successfully.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {}

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b)
            constraintLayout.setBackgroundColor(Color.CYAN);
        else
            constraintLayout.setBackgroundColor(Color.RED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "1", Toast.LENGTH_SHORT)
        ;}
        return false;
    }
}
