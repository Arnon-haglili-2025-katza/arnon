package com.arnonhaglili.myfirstaapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final int START_GAME = 222; // קוד ייחודי לפעולה של התחלת המשחק
    Button b1, b2 , b3 ,b4 ,b5;
    TextView tv1;
    Switch sw;
    SeekBar sb;
    ConstraintLayout constraintLayout;
    ImageView iv;
    ImageView iv2;
    private MediaPlayer mediaPlayer;
    private Button playPauseButton;
    private boolean isPlaying = false;
    private SharedPreferences sharedPreferences;


    private BroadcastReceiver wifiReceiver;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playPauseButton = findViewById(R.id.playPauseButton);
        sharedPreferences = getSharedPreferences("MusicPrefs", MODE_PRIVATE);

        // בודק אם המוזיקה היתה פועלת בעת יציאה מהאפליקציה
        isPlaying = sharedPreferences.getBoolean("isPlaying", false);

        // אם המוזיקה היתה פועלת, הפעל אותה
        if (isPlaying) {
            startMusic();
        }

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    pauseMusic();
                } else {
                    startMusic();
                }
            }
        });

        initViews();
        wifiReceiver = new MyWiFiBroadcastReceiver();
    }
    private void startMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.music_file); // השתמש בשם הקובץ עם הסיומת .mp3
            mediaPlayer.setLooping(true); // אם ברצונך שהמוזיקה תשחזר את עצמה
            mediaPlayer.start();
            isPlaying = true;
            playPauseButton.setText("עצור");
        }
        mediaPlayer.start();
        isPlaying = true;
        playPauseButton.setText("עצור");

        // שמירת המצב של המוזיקה ב-SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isPlaying", isPlaying);
        editor.apply();
    }

    private void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            isPlaying = false;
            playPauseButton.setText("הפעל");

            // שמירת המצב של המוזיקה ב-SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isPlaying", isPlaying);
            editor.apply();
        }
    }




    @Override
    protected void onStart(){
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
        super.onStart();

    }
    @Override

    protected void onDestroy(){
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();

        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    @Override

    protected void onStop(){
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();

        super.onStop();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // בודק אם הקוד תואם לפעולה הנכונה (המשחק מתחיל)
        if (requestCode == START_GAME) {
            if (resultCode == RESULT_OK && data != null) {
                // קבלת התוצאה מ-GuessActivity
                int numGuesses = data.getIntExtra("num_guesses", -1); // מספר הניחושים
                String userName = data.getStringExtra("user_name"); // שם המשתמש

                // הצגת התוצאה על המסך
                Toast.makeText(this, "Game Finished! Number of guesses: " + numGuesses + ", User: " + userName, Toast.LENGTH_SHORT).show();
            } else {
                // במקרה שאין תוצאה או שלא הוחזרו נתונים
                Toast.makeText(this, "Game was canceled or did not finish successfully.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void initViews() {
        sw = findViewById(R.id.sw);
        sw.setOnCheckedChangeListener(this);

        constraintLayout = findViewById(R.id.constraintLayout);

        b1 = findViewById(R.id.btnClickListener1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "clickListener1", Toast.LENGTH_LONG).show();

            }
        });
        iv = findViewById(R.id.iv);
        iv2 = findViewById(R.id.iv2);

        b2 = findViewById(R.id.btnClickListener2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "clickListener2", Toast.LENGTH_LONG).show();
            }
        });
        b3 = findViewById(R.id.b3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(this, LinearActivity.class);
                Intent intent = new Intent(MainActivity.this, LinearActivity.class);
                startActivity(intent);
                finish();
            }
        });
        b4 = findViewById(R.id.b4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(this, LinearActivity.class);
                Intent intent = new Intent(MainActivity.this, FrameActivity.class);

                startActivityForResult(intent,START_GAME);
            }
        });
        b5 = findViewById(R.id.b5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GuessActivity.class);
                startActivityForResult(intent, START_GAME); // תוודא שה-START_GAME הוא קבוע ואינו משתנה
            }
        });



        sb = findViewById(R.id.sb);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float alpha = (float)i/100;
                float beta = 1 - (float) i/100;
                iv.setAlpha(alpha);
                iv2.setAlpha(beta);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override
    public void onClick(View view) {

    }

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
        // Handle item selection.
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item2:
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}