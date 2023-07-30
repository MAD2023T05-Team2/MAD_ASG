package sg.edu.np.mad.productivibe_;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfilePage extends AppCompatActivity {
    final String TITLE = "EditProfile";
    private FirebaseAuth uAuth;
    private TextView accDetails ;
    private EditText EditName ;
    private EditText EditUserName;
    private EditText EditPassword;
    private EditText confirmEditPassword;
    private Button editAccountButton;
    private Button deleteAccountButton;
    private DialogInterface.OnClickListener deleteAccFR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // for bottom navigation page
        Log.v(TITLE, "Navigation Buttons");

        // Setting the navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                return true;
            } else if (itemId == R.id.bottom_tasks) {
                startActivity(new Intent(EditProfilePage.this, TaskActivity.class));
                return true;
            } else if (itemId == R.id.bottom_timer) {
                startActivity(new Intent(EditProfilePage.this, TaskTimerPage.class));
                return true;
            } else if (itemId == R.id.bottom_destress) {
                startActivity(new Intent(EditProfilePage.this, DestressPage.class));
                return true;
            } else if (itemId == R.id.bottom_statistics) {
                startActivity(new Intent(EditProfilePage.this, StatisticsPage.class));
                return true;
            }
            return false;
        });

        // Initialize UI elements
        accDetails = findViewById(R.id.accDetails);
        EditName = findViewById(R.id.EditName);
        EditUserName = findViewById(R.id.EditUserName);
        EditPassword = findViewById(R.id.EditPassword);
        confirmEditPassword = findViewById(R.id.confirmEditPassword);
        editAccountButton = findViewById(R.id.editAccountButton);
        deleteAccountButton = findViewById(R.id.deleteProfile);

        // Initialize Firebase
        uAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart(){
        super.onStart();

        // retrieve Firebase Auth details and display
        FirebaseUser currentUser = uAuth.getCurrentUser();
        if (currentUser == null){
            // logged-out
            uAuth.signOut();
            startActivity(new Intent(EditProfilePage.this, LoginPage.class));
        }

        String currentName = currentUser.getDisplayName();
        String currentUserName = currentUser.getEmail();
        String displayUserName = currentUserName.replace("@vibe.com","");
        String toDisplay = "Current Name: " + currentName + "\n" + "Current UserName: " + displayUserName;
        accDetails.setText(toDisplay);

        // listener on Edit Button
        // only fields that are filled up will be affected
        editAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // take input values
                String changedName = EditName.getText().toString().trim();
                String changedUser = EditUserName.getText().toString().trim();
                String changedPass = EditPassword.getText().toString().trim();
                String confrmdPass = confirmEditPassword.getText().toString().trim();

                // change counter
                int counter = 0;
                // give chance boolean
                boolean giveChance = false;

                // update changedName if got
                if (!changedName.equals("")){
                    counter ++;
                    UserProfileChangeRequest nameUpdate = new UserProfileChangeRequest.Builder().setDisplayName(changedName).build();
                    currentUser.updateProfile(nameUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // completed update
                            Toast.makeText(getApplicationContext(),"Name Updated!",Toast.LENGTH_SHORT);
                            Log.i(TITLE,"Completed Name Update");
                            Log.i(TITLE,changedName);
                        }
                    });
                }

                if (!changedUser.equals("")){
                    // make into an email
                    counter ++;
                    String Email = changedUser + "@vibe.com";
                    currentUser.updateEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // completed update to email
                            Toast.makeText(getApplicationContext(),"Username Updated!",Toast.LENGTH_SHORT).show();
                            Log.i(TITLE, "Completed Email Update");
                            Log.i(TITLE,Email);
                        }
                    });
                }

                if (!changedPass.equals("") && !confrmdPass.equals("")){
                    counter ++;
                    if (changedPass == confrmdPass){
                        // change password
                        currentUser.updatePassword(changedPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(),"Password Updated!",Toast.LENGTH_SHORT).show();
                                Log.i(TITLE, "Completed Password Update");
                            }
                        });
                    }
                    else{
                        // passwords dont match
                        Toast.makeText(getApplicationContext(),"Passwords don't match :(",Toast.LENGTH_SHORT).show();
                        giveChance = true;

                    }
                } else if (!changedPass.equals("") || !changedPass.equals("")) {
                    // is not filled in
                    Toast.makeText(getApplicationContext(),"Fill in the other password box too >:(",Toast.LENGTH_SHORT).show();
                    giveChance = true;

                }

                // exit this page and go to HomePage
                if (!giveChance){
                    Intent toHome = new Intent(EditProfilePage.this,HomePage.class);
                    if (counter == 0){
                        Toast.makeText(getApplicationContext(),"Nothing changed!?", Toast.LENGTH_LONG).show();
                    }
                    startActivity(toHome);
                }
            }
        });

        // listener on Delete Button
        deleteAccFR = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // they do want to delete LLLL
                        // save the uId for deletion in realtime storage
                        String uId = currentUser.getUid();
                        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
                        DatabaseReference forTasks = fdb.getReference("tasks/" + uId );
                        forTasks.removeValue();
                        DatabaseReference forMoods = fdb.getReference("moods/" + uId);
                        forMoods.removeValue();
                        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // delete from firebase
                                if (task.isSuccessful()){ // ensures Firebase Auth is dealt with
                                    uAuth.signOut();
                                    // To log out
                                    SharedPreferences rememberUserData = getSharedPreferences("MyPrefs", MODE_PRIVATE); // Updates remember option to "False"
                                    SharedPreferences.Editor RUDeditor = rememberUserData.edit();
                                    RUDeditor.putString("Remember", "False");
                                    RUDeditor.apply();
                                    // Mark that the app back to launching for the first time
                                    SharedPreferences sharedPreferences2 = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences2.edit();
                                    editor.putBoolean("IsFirstLaunch", true);
                                    editor.apply();

                                    startActivity(new Intent(EditProfilePage.this, LoginPage.class));
                                }
                                else{
                                    Log.w(TITLE,"delete failed", task.getException());
                                    Toast.makeText(EditProfilePage.this, "Deletion Failed!", Toast.LENGTH_LONG);
                                }

                            }
                        });
                        break;
                    // for our negative button.
                    case DialogInterface.BUTTON_NEGATIVE:
                        // on below line we are dismissing our dialog box.
                        dialog.dismiss();
                        Toast.makeText(EditProfilePage.this,":D", Toast.LENGTH_LONG);
                }

            }
        };
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open up dialog to make sure!!
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfilePage.this);
                builder.setTitle("Hold on!");
                builder.setMessage("Are you sure you want to delete your profile? All your tasks, moods and data will be permanently deleted !!")
                        .setPositiveButton("Yes I do!",deleteAccFR)
                        .setNegativeButton("NO!",deleteAccFR)
                        .show();
            }
        });

    }
}
