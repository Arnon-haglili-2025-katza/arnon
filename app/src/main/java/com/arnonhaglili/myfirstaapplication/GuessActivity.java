package com.arnonhaglili.myfirstaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class GuessActivity extends AppCompatActivity {
    EditText etGuess;
    Button btnSend, btnGuess;
    boolean bIsCorrect;
    TextView tvResult;
    int randomNumber;
    int numberOfGuesses;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);

        bIsCorrect = false;
        numberOfGuesses = 0;
        randomNumber = 0;

        initViews();
    }
    private void checkGuess(int guess) {
        if (guess == randomNumber) {
            Toast.makeText(this, "Correct! You guessed the number in " + numberOfGuesses + " attempts!", Toast.LENGTH_SHORT).show();


        }
    }


    private void initViews() {
        etGuess = findViewById(R.id.etGuess);
        tvResult = findViewById(R.id.tvResult);

        btnSend = findViewById(R.id.btnSend);
        radioGroup = findViewById(R.id.radioGroup);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int min1 = 0;
                int max1 = 0;

                // בדוק איזה RadioButton נבחר
                RadioButton selectedRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());

                if (selectedRadioButton != null) {
                    switch (selectedRadioButton.getId()) {
                        case R.id.rbRange1:
                            min1 = 1;
                            max1 = 10;
                            break;
                        case R.id.rbRange2:
                            min1 = 11;
                            max1 = 50;
                            break;
                        case R.id.rbRange3:
                            min1 = 51;
                            max1 = 100;
                            break;
                        case R.id.rbRange4:
                            min1 = 101;
                            max1 = 200;
                            break;
                    }

                    // יצירת מספר אקראי בטווח הנבחר
                    randomNumber = (int) (Math.random() * (max1 - min1 + 1)) + min1;
                    Log.d("AAA", "randomNumber=" + randomNumber);

                    // השבתת ה-RadioGroup אחרי שבוצע בחירת טווח
                    radioGroup.setEnabled(false); // מבטל את היכולת לשנות את הטווח

                    // שינוי צבע הטקסט של ה-RadioButtons ל-grey כדי להמחיש שהטווח לא ניתן לשינוי
                    for (int i = 0; i < radioGroup.getChildCount(); i++) {
                        RadioButton rb = (RadioButton) radioGroup.getChildAt(i);
                        rb.setTextColor(getResources().getColor(android.R.color.darker_gray)); // צבע אפור
                    }

                    // הצגת הודעה שהטווח נקבע
                    tvResult.setText("Range set: " + min1 + " to " + max1);
                } else {
                    // אם לא נבחר טווח
                    tvResult.setText("נא לבחור טווח");
                }
            }
        });

        btnGuess = findViewById(R.id.btnGuess);
        btnGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOfGuesses++;

                String strtGuess = etGuess.getText().toString();
                int guessedNumber = Integer.parseInt(strtGuess);

                if (guessedNumber > randomNumber) {
                    tvResult.setText("bigger");
                    Log.d("AAA", "bigger");
                }
                if (guessedNumber < randomNumber) {
                    tvResult.setText("smaller");
                    Log.d("AAA", "smaller");
                }
                if (guessedNumber == randomNumber) {
                    tvResult.setText("equal, steps=" + numberOfGuesses);
                    Log.d("AAA", "equal");

                    // יצירת Intent עם התוצאות
                    Intent result = new Intent();
                    result.putExtra("num_guesses", numberOfGuesses); // מספר הניחושים
                    result.putExtra("user_name", "bbb"); // שם המשתמש, יש להחליף בשם המשתמש המתאים
                    setResult(RESULT_OK, result); // מחזיר את התוצאה
                    finish(); // מסיים את הפעולה
                }
            }
        });
    }
}