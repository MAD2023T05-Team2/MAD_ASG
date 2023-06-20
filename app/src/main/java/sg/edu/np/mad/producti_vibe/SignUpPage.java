package sg.edu.np.mad.producti_vibe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
        Button createAccountButton = findViewById(R.id.createAccountButton);
        // Initialize the database
        db = TaskDatabase.getInstance(this);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String signUpN = signUpName.getText().toString();
                String signUpUser = signUpUsername.getText().toString();
                String signUpPass = signUpPassword.getText().toString();

                if (signUpN.equals("") || signUpUser.equals("") || signUpPass.equals("")) { //check if all fields are filled
                    Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
                else if (db.findUserData(signUpUser) == null){ //there is no such username
                    User userData = new User();
                    userData.setName(signUpN);
                    userData.setUserName(signUpUser);
                    userData.setPassWord(signUpPass);
                    db.addUser(userData);
                    Intent signupToLogin = new Intent(SignUpPage.this, LoginPage.class);
                    startActivity(signupToLogin);
                    Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Username already exists! Please choose another username!", Toast.LENGTH_SHORT).show();
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
