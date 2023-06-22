package sg.edu.np.mad.producti_vibe;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PicturesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PictureAdapter adapter;
    private List<Integer> pictureList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pictures);

        recyclerView = findViewById(R.id.recyclerView);
        pictureList = new ArrayList<>();

        // Add the picture resource IDs to the list
        pictureList.add(R.drawable.fail);
        pictureList.add(R.drawable.grades);
        // Add more pictures as needed

        // Create and set the adapter
        adapter = new PictureAdapter(pictureList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
