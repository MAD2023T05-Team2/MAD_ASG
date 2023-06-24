package sg.edu.np.mad.producti_vibe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfilePage extends AppCompatActivity {

    String TITLE = "Profile Page";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        // Bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_profile) {
                return true;
            } else if (itemId == R.id.bottom_home) {
                startActivity(new Intent(ProfilePage.this, HomePage.class));
                return true;
            } else if (itemId == R.id.bottom_calendar) {
                startActivity(new Intent(ProfilePage.this, CalendarPage.class));
                return true;
            } else if (itemId == R.id.bottom_tasks) {
                startActivity(new Intent(ProfilePage.this, TaskActivity.class));
                return true;
            } else if (itemId == R.id.bottom_timer) {
                startActivity(new Intent(ProfilePage.this, DestressPage.class));
                return true;
            }
            return false;
        });
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
                Intent signingOut = new Intent(ProfilePage.this, LoginPage.class);
                startActivity(signingOut);
                Log.v(TITLE, "Logging out");
            }
        });
    }
}
