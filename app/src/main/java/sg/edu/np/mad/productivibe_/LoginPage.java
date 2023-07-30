package sg.edu.np.mad.productivibe_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// Login page allows users to login with an existing account
// Remember me feature is available, it will not prompt the user for login details the next time they enter the app unless they had chose to log out previously
public class LoginPage extends AppCompatActivity {
    private DatabaseReference dbr;
    private FirebaseDatabase fdb;
    String TITLE = "Login Page";

    private FirebaseAuth uAuth;

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

        // for Firebase Auth
        uAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if the user has chosen the option to remember their login in the app
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", 0);
        String remember = sharedPreferences.getString("Remember", "");

        // Check if user is signed-in under Firebase
        FirebaseUser currentUser = uAuth.getCurrentUser();
        if (currentUser != null && remember.equals("True")){ // If the value is "True", redirect the user to the home page by creating an intent and starting the activity
            Intent intent = new Intent(LoginPage.this, HomePage.class);
            startActivity(intent);
            Log.v(TITLE, remember);
        }
        else if (currentUser != null && remember.equals("False")){
            uAuth.signOut(); // Log out FirebaseAuth
        }

        // Initialize UI elements
        TextView signup = findViewById(R.id.signup);
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        CheckBox rememberMe = findViewById(R.id.rememberMe);


        // Event handler for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = username.getText().toString();
                String pass = password.getText().toString();

                if (userName.equals("") || pass.equals("")) { // Check if all fields are entered
                    Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                    Log.v(TITLE, "Not all fields are entered");
                }
                else {
                    // use Firebase Auth
                    String Email = userName + "@vibe.com";
                    Log.d(TITLE, Email);
                    uAuth.signInWithEmailAndPassword(Email, pass).addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                        // auto-checks for us the password too
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TITLE, "signInWithEmail:success");
                                FirebaseUser user = uAuth.getCurrentUser();
                                // move into HomePage
                                // Successful Login and bring user to the homepage
                                Intent loginToHome = new Intent(LoginPage.this, HomePage.class);
                                startActivity(loginToHome);

                                // Remember the name so that it will be displayed on the home page
                                SharedPreferences rememberName = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor RNeditor = rememberName.edit();

                                String displayName = user.getEmail().replace("@vibe.com","");
                                RNeditor.putString("Name", displayName);// just in case
                                RNeditor.apply();

                                // Remember whether the user chose the app to remember their login details
                                boolean isChecked = rememberMe.isChecked();
                                Log.v("isChecked", Boolean.toString(isChecked));
                                SharedPreferences rememberUserData = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor RUDeditor = rememberUserData.edit();

                                if (isChecked == true) {
                                    RUDeditor.putString("Remember", "True");
                                    Toast.makeText(getApplicationContext(), "Login Successful! Login Credentials Remembered.", Toast.LENGTH_SHORT).show();
                                    //signInFirebase(sn.child("userName").getValue().toString(),pass);
                                    Log.v("Remember", "True");
                                } else {
                                    RUDeditor.putString("Remember", "False");
                                    Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                                    Log.v("Remember", "False");
                                }
                                RUDeditor.apply();
                            } else {
                                // If sign in fails, display a message to the user.
                                if (task.getException() instanceof FirebaseAuthInvalidUserException){
                                    // user does not exist
                                    Toast.makeText(LoginPage.this, "No such username found. Please try again.", Toast.LENGTH_SHORT).show();
                                    Log.v(TITLE, "No such username");
                                }
                                else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                    // wrong password
                                    Toast.makeText(LoginPage.this, "Password is incorrect. Please try again.", Toast.LENGTH_SHORT).show();
                                    Log.v(TITLE, "Wrong password");
                                }
                                else{
                                    Log.w(TITLE, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginPage.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    });
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