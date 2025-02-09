package com.arnonhaglili.myfirstaapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Dinami extends AppCompatActivity {

    LinearLayout main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinami);


        // מציאת ה-LinearLayout
        main = findViewById(R.id.llDinami); // ודא כי יש לך LinearLayout עם id main_layout ב-XML

        // יצירת ImageView חדש
        ImageView iv = new ImageView(this);

        // הגדרת גודל התמונה באמצעות LayoutParams
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                100, 100 // רוחב וגובה התמונה
        );

        // הגדרת LayoutParams ל-ImageView
        iv.setLayoutParams(layoutParams);

        // הגדרת התמונה
        iv.setImageResource(R.drawable.maz); // בחר את התמונה הרצויה מתוך תיקיית drawable


        // הוספת ImageView ל-LinearLayout
        main.addView(iv);
    }
}