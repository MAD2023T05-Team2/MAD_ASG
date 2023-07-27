package sg.edu.np.mad.productivibe_;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

// PunchFragment is a fragment under the Destress Page which lets users to release stress and frustration by
// tapping on the punching bag. They can also select a photo of their choice to tap on to release their stress
public class PunchFragment extends Fragment {

    private Button selectButton;
    private TextView counterText;
    // constant to compare the activity result code
    int SELECT_PICTURE = 200;
    int counter = 0;
    ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_punch, container, false);

        selectButton = view.findViewById(R.id.photo);
        image = view.findViewById(R.id.punching);
        counterText = view.findViewById(R.id.count);

        // handle the select image button to trigger the image chooser function
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // increase the counter everytime the image is tapped
                counter ++;
                // show and update the counter number
                counterText.setText(Integer.toString(counter));

                // if user tapped more than 50 times, they must be feeling very frustrated.
                // there is a popup to express concern about the user and hope that they
                // feel better
                if (counter == 50){
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setMessage("Hope that you are feeling better!");
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }

        });


        return view;
    }
    @Override
    public void onResume() { // Make sure that the counter resets to 0 everytime user navigates to the fragment
        super.onResume();
        counter = 0; // Reset the counter to 0 when the fragment is resumed
        counterText.setText(Integer.toString(counter)); // Update the counter text view
    }

    // function when the select photo button is clicked
    void imageChooser() {

        // create an instance of the intent of the type image to open the device's file chooser for an image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // Starts the image chooser activity to allow the user to select an image from the device's files
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // function for when user select the image from the image chooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) { // Checks if the results of the activity is successful, otherwise,
                                       // the method will not perform any further actions

            if (requestCode == SELECT_PICTURE) { //checking if the activity result received is related to the image chooser activity
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    image.setImageURI(selectedImageUri);
                }
            }
        }
    }
}