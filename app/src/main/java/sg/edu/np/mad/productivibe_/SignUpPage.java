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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// The sign up page allows users to sign up for a new account with a unique username
public class SignUpPage extends AppCompatActivity {
    private DatabaseReference dbr;
    private FirebaseDatabase fdb;
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
    }

    @Override
    public void onStart() {
        super.onStart();


        // Initialize UI elements
        TextView toLogin = findViewById(R.id.toLogin);
        EditText signUpName = findViewById(R.id.signUpName);
        EditText signUpUsername = findViewById(R.id.signUpUsername);
        EditText signUpPassword = findViewById(R.id.signUpPassword);
        EditText cfmPassword = findViewById(R.id.confirmSignUpPassword);
        Button createAccountButton = findViewById(R.id.createAccountButton);

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
                                                           dbr.orderByChild("userName").equalTo(signUpUser).addListenerForSingleValueEvent(new ValueEventListener() {
                                                               @Override
                                                               public void onDataChange(DataSnapshot dataSnapshot) {
                                                                   if (dataSnapshot.exists()) {
                                                                       //username exists in database
                                                                       Toast.makeText(getApplicationContext(), "Username already exists! Please choose another username!", Toast.LENGTH_SHORT).show();
                                                                       Log.v(TITLE, "username exists");
                                                                   } else {
                                                                       // username does not exist
                                                                       // can create and write into database
                                                                       // Check if password and confirmed password matches
                                                                       if (cfmPass.equals(signUpPass)) // Check if password and confirmed password matches
                                                                       {
                                                                           User userData = new User();
                                                                           userData.setName(signUpN);
                                                                           userData.setUserName(signUpUser);
                                                                           userData.setPassWord(signUpPass);
                                                                           // add into firebase database
                                                                           dbr.child(signUpUser).setValue(userData);
                                                                           Log.d(TITLE,"added into firebase database");

                                                                           // Remember the UserId (Value obtained after registration)
                                                                           SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                                                           SharedPreferences.Editor UNeditor = sharedPreferences.edit();
                                                                           UNeditor.putString("UserId", userData.getUserId());
                                                                           UNeditor.apply();

                                                                           // Bring to login page to login after successful sign up
                                                                           Intent signupToLogin = new Intent(SignUpPage.this, LoginPage.class);
                                                                           startActivity(signupToLogin);
                                                                           Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_SHORT).show();
                                                                           Log.v(TITLE, "Account creation successful");
                                                                       }
                                                                       else{
                                                                           // confirm password do not match
                                                                           Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                                                                           Log.v(TITLE, "Password: " + signUpPass + " Confirmed password: "+ cfmPass);
                                                                       }
                                                                   }
                                                               }

                                                               @Override
                                                               public void onCancelled(@NonNull DatabaseError error) {
                                                                   // if it doesnt work
                                                                   Toast.makeText(getApplicationContext(), "You're offline :( Cannot reach the database", Toast.LENGTH_SHORT).show();
                                                                   Log.d(TITLE, error.getMessage());

                                                               }
                                                           });
                                                       }
                                                   }
                                               });

                /*
                // using sqllitehelper

                if (signUpN.equals("") || signUpUser.equals("") || signUpPass.equals("") || cfmPass.equals("")) { // Check if all fields are filled
                    Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                    Log.v(TITLE, "Empty Fields");
                }
                else if (db.findUserData(signUpUser) == null) { // No existing username, allow for account creation
                    if (cfmPass.equals(signUpPass)) // Check if password and confirmed password matches
                    {
                        User userData = new User();
                        userData.setName(signUpN);
                        userData.setUserName(signUpUser);
                        userData.setPassWord(signUpPass);
                        db.addUser(userData);
                        // add into firebase database
                        dbr.child(signUpUser).setValue(userData);
                        Log.d(TITLE,"added into firebase database");

                        // Remember the UserId (Value obtained after registration)
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor UNeditor = sharedPreferences.edit();
                        UNeditor.putString("UserId", userData.getUserId());
                        UNeditor.apply();
                        Intent toDB = new Intent(SignUpPage.this, Database.class);
                        toDB.putExtra("userId", userData.getUserId());
                        startActivity(toDB);

                        // Bring to login page to login after successful sign up
                        Intent signupToLogin = new Intent(SignUpPage.this, LoginPage.class);
                        startActivity(signupToLogin);
                        Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_SHORT).show();
                        Log.v(TITLE, "Account creation successful");
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                        Log.v(TITLE, "Password: " + signUpPass + " Confirmed password: "+ cfmPass);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Username already exists! Please choose another username!", Toast.LENGTH_SHORT).show();
                    Log.v(TITLE, "username exists");
                }
            }
        });
                 */

        // Event handler for the login text view
        toLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent signupToLogin = new Intent(SignUpPage.this, LoginPage.class); // Bring to login page
                startActivity(signupToLogin);
            }
        });
}}
