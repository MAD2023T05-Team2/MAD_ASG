package sg.edu.np.mad.productivibe_;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseRTS {
    // based on ReadAndWriteSnippets found in their documentation
    // RTS stands for Realtime storage

    private String TITLE = "Firebase RTS";

    private DatabaseReference DBR;

    public FirebaseRTS(DatabaseReference DBR){
        DBR = FirebaseDatabase.getInstance("https://np-mad-asg--23-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    public void writeNewUser(User user) {
        String userId = user.getUserId();
        // [START rtdb_write_new_user_task]
        DBR.child("users").child(userId).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Log.d(TITLE, "Added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Log.d(TITLE, "User Write Failed");
                    }
                });}

    private void addPostEventListener(DatabaseReference DBR) {
            // [START post_value_event_listener]
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    User post = dataSnapshot.getValue(User.class);
                    Log.v(TITLE,post.getUserId());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TITLE, "loadPost:onCancelled", databaseError.toException());
                }
            };
            DBR.addValueEventListener(postListener);
            // [END post_value_event_listener]
        }
    }
