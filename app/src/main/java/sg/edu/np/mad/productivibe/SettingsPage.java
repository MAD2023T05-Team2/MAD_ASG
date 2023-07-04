package sg.edu.np.mad.productivibe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

// In the settings page, users can edit their personal information, ++
public class SettingsPage extends AppCompatActivity {
    String TITLE = "Settings Page";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);

    }
    @Override
    protected void onStart(){
        super.onStart();

        // Initialize button
        Button logOutButton = findViewById(R.id.logOutButton);

        // Event handler for the log out button
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update remember option to "False" during sign out
                SharedPreferences rememberUserData = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor RUDeditor = rememberUserData.edit();
                RUDeditor.putString("Remember", "False");
                RUDeditor.apply();

                // Back to login page
                Intent signingOut = new Intent(SettingsPage.this, LoginPage.class);
                startActivity(signingOut);
                Log.v(TITLE, "Logging out");
            }
        });
    }
}
