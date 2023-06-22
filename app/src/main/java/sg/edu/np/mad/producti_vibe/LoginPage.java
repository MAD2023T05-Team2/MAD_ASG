package sg.edu.np.mad.producti_vibe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    private TaskDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        // Dual colored textview
        TextView memberYN = findViewById(R.id.signup);
        Spannable spannable = new SpannableString("Don't have an account? Sign up");
        spannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.BLUE), 23, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        memberYN.setText(spannable);

    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", 0);
        String remember = sharedPreferences.getString("Remember", "");

        if (remember.equals("True")) {
            Intent intent = new Intent(LoginPage.this, HomePage.class);
            startActivity(intent);
        }


        TextView signup = findViewById(R.id.signup);
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        CheckBox rememberMe = findViewById(R.id.rememberMe);


        // Initialize the database
        db = TaskDatabase.getInstance(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = username.getText().toString();
                String pass = password.getText().toString();

                User userData = db.findUserData(userName);

                if (userName.equals("") || pass.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                }

                else if (userData != null){ //Username exists in database
                    if (userData.getPassWord().equals(pass)){ //Check if the password entered is the same
                        // Successful Login
                        Intent loginToHome = new Intent(LoginPage.this, HomePage.class);
                        startActivity(loginToHome);

                        //Remember the name so that it will be displayed on the home page
                        SharedPreferences rememberName = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor RNeditor = rememberName.edit();
                        RNeditor.putString("Name", userData.getName());
                        RNeditor.apply();

                        boolean isChecked = rememberMe.isChecked();
                        SharedPreferences rememberUserData = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor RUDeditor = rememberUserData.edit();

                        if (isChecked) {
                            RUDeditor.putString("Remember", "True");
                            Toast.makeText(getApplicationContext(), "Login Successful! Login Credentials Remembered.", Toast.LENGTH_SHORT).show();
                        } else {
                            RUDeditor.putString("Remember", "False");
                            Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                        }

                        RUDeditor.apply();

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Password is incorrect. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
//
                    Toast.makeText(getApplicationContext(), "Username is incorrect. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent loginToSignUp = new Intent(LoginPage.this, SignUpPage.class);
                startActivity(loginToSignUp);
            }
        });
    }

//    private void checkBox() {
//        EditText username = findViewById(R.id.username);
//        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//        String check = sharedPreferences.getString("Name", "");
//
//        if (check.equals("True")) {
//            Intent loginToHome = new Intent(LoginPage.this, HomePage.class);
//            loginToHome.putExtra("Username", username.getText().toString());
//            startActivity(loginToHome);
//            Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//    }
}