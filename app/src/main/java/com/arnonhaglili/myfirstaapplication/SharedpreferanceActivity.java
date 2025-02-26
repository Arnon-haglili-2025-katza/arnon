package com.arnonhaglili.myfirstaapplication;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SharedpreferanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharedpreferance);

        // השגת ה-EditText ו-Button
        EditText firstNameEditText = findViewById(R.id.editTextFirstName);
        EditText lastNameEditText = findViewById(R.id.editTextLastName);
        EditText ageEditText = findViewById(R.id.editTextAge);
        Button saveButton = findViewById(R.id.saveButton);

        // שמירה של הנתונים ב-SharedPreferences בעת לחיצה על כפתור השמירה
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // קבלת הערכים שהוזנו
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                int age = 0;
                try {
                    age = Integer.parseInt(ageEditText.getText().toString());
                } catch (NumberFormatException e) {
                    // אם אין קלט או שהגיל לא חוקי
                    Toast.makeText(SharedpreferanceActivity.this, "אנא הכנס גיל תקני", Toast.LENGTH_SHORT).show();
                    return;
                }

                // שמירת הערכים ב-SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("first_name", firstName);  // שמירת שם פרטי
                editor.putString("last_name", lastName);    // שמירת שם משפחה
                editor.putInt("age", age);                  // שמירת גיל
                editor.apply();

                // הודעה למשתמש שהנתונים נשמרו
                Toast.makeText(SharedpreferanceActivity.this,
                        "הנתונים נשמרו!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // קריאה של הנתונים ששמנו ב-SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String savedFirstName = sharedPreferences.getString("first_name", "לא הוזן");
        String savedLastName = sharedPreferences.getString("last_name", "לא הוזן");
        int savedAge = sharedPreferences.getInt("age", 0);

        // הצגת הנתונים שנשמרו בלוג (או בכל אמצעי אחר שתבחר)
        System.out.println("First Name: " + savedFirstName);
        System.out.println("Last Name: " + savedLastName);
        System.out.println("Age: " + savedAge);
    }


    }
