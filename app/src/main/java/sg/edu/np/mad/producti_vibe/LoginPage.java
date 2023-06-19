package sg.edu.np.mad.producti_vibe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

    }

    @Override
    public void onStart() {
        super.onStart();

        TextView signup = findViewById(R.id.signup);
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginToHome = new Intent(LoginPage.this, HomePage.class);
                loginToHome.putExtra("Username", username.getText().toString());
                startActivity(loginToHome);
                Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
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
}