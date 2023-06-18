package sg.edu.np.mad.productivibe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DestressMessage extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_destress_message, container, false);
        return view;
    }

    /*@Override View onCreate(Bundle saved instance)

    ImageView gif = findViewById(R.id.gif);

    // Adding the gif here using glide library
        Glide.with(this).load(R.drawable.frog).into(gif);
*/
}