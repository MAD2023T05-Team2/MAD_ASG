package sg.edu.np.mad.productivibe_;

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

// Login page allows users to login with an existing account
// Remember me feature is available, it will not prompt the user for login details the next time they enter the app unless they had chose to log out previously
public class LoginPage extends AppCompatActivity {
    private Database db;
    String TITLE = "Login Page";

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

        // Check if the user has chosen the option to remember their login in the app
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", 0);
        String remember = sharedPreferences.getString("Remember", "");

        if (remember.equals("True")) { // If the value is "True", redirect the user to the home page by creating an intent and starting the activity
            Intent intent = new Intent(LoginPage.this, HomePage.class);
            startActivity(intent);
        }
        Log.v(TITLE, remember);

        // Initialize UI elements
        TextView signup = findViewById(R.id.signup);
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        CheckBox rememberMe = findViewById(R.id.rememberMe);

        // Initialize the database
        db = Database.getInstance(this);

        // Event handler for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = username.getText().toString();
                String pass = password.getText().toString();

                User userData = db.findUserData(userName);

                if (userName.equals("") || pass.equals("")) { // Check if all fields are entered
                    Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                    Log.v(TITLE, "Not all fields are entered");
                }

                else if (userData != null){ // Username exists in database
                    if (userData.getPassWord().equals(pass)){ // Check if the password entered is the same as the one in the database
                        // Successful Login and bring user to the homepage
                        Intent loginToHome = new Intent(LoginPage.this, HomePage.class);
                        startActivity(loginToHome);

                        // Remember the name so that it will be displayed on the home page
                        SharedPreferences rememberName = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor RNeditor = rememberName.edit();
                        RNeditor.putString("Name", userData.getName());
                        RNeditor.putString("Username", userData.getUserName());
                        RNeditor.putString("UserId", userData.getUserId());
                        RNeditor.apply();

                        // Remember whether the user chose the app to remember their login details
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
                        Log.v(TITLE, "Successful login");
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Password is incorrect. Please try again.", Toast.LENGTH_SHORT).show();
                        Log.v(TITLE, "Wrong password");
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "No such username found. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.v(TITLE, "No such username");
                }
            }
        });

        // Event handler for the signup text view
        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent loginToSignUp = new Intent(LoginPage.this, SignUpPage.class); // Bring user to the sign up page
                startActivity(loginToSignUp);
            }
        });
    }
}