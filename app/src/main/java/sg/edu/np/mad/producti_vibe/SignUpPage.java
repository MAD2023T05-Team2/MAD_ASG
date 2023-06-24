package sg.edu.np.mad.producti_vibe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpPage extends AppCompatActivity {
    private TaskDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        // Dual colored textview
        TextView memberYN2 = findViewById(R.id.toLogin);
        Spannable spannable = new SpannableString("Already have an account? Log in");
        spannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.BLUE), 25, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        memberYN2.setText(spannable);
    }

    @Override
    public void onStart() {
        super.onStart();

        TextView toLogin = findViewById(R.id.toLogin);
        EditText signUpName = findViewById(R.id.signUpName);
        EditText signUpUsername = findViewById(R.id.signUpUsername);
        EditText signUpPassword = findViewById(R.id.signUpPassword);
        EditText cfmPassword = findViewById(R.id.confirmSignUpPassword);
        Button createAccountButton = findViewById(R.id.createAccountButton);
        // Initialize the database
        db = TaskDatabase.getInstance(this);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String signUpN = signUpName.getText().toString();
                String signUpUser = signUpUsername.getText().toString();
                String signUpPass = signUpPassword.getText().toString();
                String cfmPass = cfmPassword.getText().toString();

                if (signUpN.equals("") || signUpUser.equals("") || signUpPass.equals("")) { // Check if all fields are filled
                    Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                    Log.v("SignUpPage", "Empty Fields");
                }
                else if (db.findUserData(signUpUser) == null) { // No existing username, allow for account creation
                    if (cfmPass.equals(signUpPass))//check if password and confirmed password matches
                    {
                        User userData = new User();
                        userData.setName(signUpN);
                        userData.setUserName(signUpUser);
                        userData.setPassWord(signUpPass);
                        db.addUser(userData);

                        // Remember the UserId
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor UNeditor = sharedPreferences.edit();
                        UNeditor.putString("UserId", userData.getUserId()); // userId is the value you obtained after registration
                        UNeditor.apply();
                        Intent toDB = new Intent(SignUpPage.this, TaskDatabase.class);
                        toDB.putExtra("userId", userData.getUserId()); // userId is the value you obtained after registration
                        startActivity(toDB);

                        Intent signupToLogin = new Intent(SignUpPage.this, LoginPage.class);
                        startActivity(signupToLogin);
                        Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_SHORT).show();
                        Log.v("SignUpPage", "can create account");
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                        Log.v("Pass", signUpPass);
                        Log.v("CfmPass", cfmPass);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Username already exists! Please choose another username!", Toast.LENGTH_SHORT).show();
                    Log.v("SignUpPage", "username exists");
                }
            }
        });

        toLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent signupToLogin = new Intent(SignUpPage.this, LoginPage.class);
                startActivity(signupToLogin);
            }
        });
    }
}
