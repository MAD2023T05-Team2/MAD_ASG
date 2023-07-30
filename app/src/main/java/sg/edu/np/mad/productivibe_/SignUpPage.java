package sg.edu.np.mad.productivibe_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// The sign up page allows users to sign up for a new account with a unique username
public class SignUpPage extends AppCompatActivity {
    private DatabaseReference dbr;
    private FirebaseDatabase fdb;
    private FirebaseAuth uAuth;
    String TITLE = "Sign Up Page";
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

        // Initialise Firebase Auth
        uAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onStart() {
        super.onStart();

        // check if user is signed-in
        FirebaseUser currentUser = uAuth.getCurrentUser();
        if (currentUser != null) {
            // signed-in ; move activity to login
            Intent toLoginPage = new Intent(SignUpPage.this, LoginPage.class);
            startActivity(toLoginPage);

        }


        // Initialize UI elements
        TextView toLogin = findViewById(R.id.toLogin);

        EditText signUpName = findViewById(R.id.signUpName);
        EditText signUpUsername = findViewById(R.id.signUpUsername);
        EditText signUpPassword = findViewById(R.id.signUpPassword);
        EditText cfmPassword = findViewById(R.id.confirmSignUpPassword);
        Button createAccountButton = findViewById(R.id.createAccountButton);

        TextView toGuest = findViewById(R.id.toGuest);

        // Initialize the database
        fdb = FirebaseDatabase.getInstance();
        dbr = fdb.getReference("users");

        // Event handler for the create account button
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String signUpN = signUpName.getText().toString();
                String signUpUser = signUpUsername.getText().toString();
                String signUpPass = signUpPassword.getText().toString();
                String cfmPass = cfmPassword.getText().toString();

                // rewrite of previous checks so that it relies on firebase
                if (signUpN.equals("") || signUpUser.equals("") || signUpPass.equals("") || cfmPass.equals("")) { // Check if all fields are filled
                    Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                    Log.v(TITLE, "Empty Fields");
                } else {
                    // account should be able to be created
                    // Firebase Auth
                    String Email = signUpUser + "@vibe.com";

                    if (cfmPass.equals(signUpPass)) // Check if password and confirmed password matches
                    {
                        uAuth.createUserWithEmailAndPassword(Email,signUpPass).addOnCompleteListener(SignUpPage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    // Creation successful
                                    Log.d(TITLE, "createUserWithEmail:success");
                                    FirebaseUser user = uAuth.getCurrentUser();
                                    // update display name
                                    UserProfileChangeRequest setDisplayName = new UserProfileChangeRequest.Builder().setDisplayName(signUpN).build();
                                    user.updateProfile(setDisplayName);
                                    // username does not exist
                                    // can create and write into database
                                    // add into firebase database
                                    dbr.child(user.getUid()).child("userName").setValue(signUpUser);
                                    dbr.child(user.getUid()).child("name").setValue(signUpN);
                                    Log.d(TITLE, "added into firebase database");

                                    // Bring to login page to login after successful sign up
                                    Intent signupToLogin = new Intent(SignUpPage.this, LoginPage.class);
                                    startActivity(signupToLogin);
                                    Toast.makeText(SignUpPage.this, "Account creation successful",
                                            Toast.LENGTH_SHORT).show();
                                    Log.v(TITLE, "Account creation successful");
                                }
                                else{
                                    // Unsuccessful
                                    // Handle Errors
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                        // Occurs when userName already exists
                                        Toast.makeText(SignUpPage.this, "Username taken :(",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else if (task.getException() instanceof FirebaseAuthWeakPasswordException){
                                        // When password is too weak
                                        Toast.makeText(SignUpPage.this, "Password too weak! Aim for at least a length of 6 ^^",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        // for everything else
                                        Log.w(TITLE, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignUpPage.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                    else{ // doesn't match
                        // confirm password do not match
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                        Log.v(TITLE, "Password: " + signUpPass + " Confirmed password: " + cfmPass);
                    }
                }
            }
        });

        // Event handler for the login text view
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupToLogin = new Intent(SignUpPage.this, LoginPage.class); // Bring to login page
                startActivity(signupToLogin);
            }
        });

        // Event handler for logging - in as a guest
        toGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sign-in anonymously
                uAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // sign in successful, take it as remember me ALWAYS
                            FirebaseUser user = uAuth.getCurrentUser();
                            // set name as Guest
                            UserProfileChangeRequest toGuest = new UserProfileChangeRequest.Builder().setDisplayName("Guest").build();
                            user.updateProfile(toGuest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.i(TITLE,"Became a guest!");
                                    Log.i(TITLE,user.getUid());
                                    // move into HomePage
                                    // Successful Login and bring user to the homepage
                                    Intent loginToHome = new Intent(SignUpPage.this, HomePage.class);
                                    startActivity(loginToHome);

                                    // Remember the name so that it will be displayed on the home page
                                    SharedPreferences rememberName = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                    // Remember whether the user chose the app to remember their login details
                                    SharedPreferences rememberUserData = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor RUDeditor = rememberUserData.edit();
                                    RUDeditor.putString("Remember", "True");
                                    Toast.makeText(getApplicationContext(), "Login Successful! Login Credentials Remembered.", Toast.LENGTH_SHORT).show();
                                    Log.v("Remember", "True");
                                    RUDeditor.apply();
                                }
                            });
                        }
                        else{
                            // unsuccessful
                            Log.w(TITLE, "signInAnonymously:failure", task.getException());
                            Toast.makeText(SignUpPage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}

