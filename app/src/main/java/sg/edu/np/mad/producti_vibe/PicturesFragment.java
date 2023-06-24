package sg.edu.np.mad.producti_vibe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PicturesFragment extends Fragment {

    private RecyclerView recyclerView;
    private PictureAdapter adapter;
    private List<Integer> pictureList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pictures, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        pictureList = new ArrayList<>();

        // Add the picture resource IDs to the list
        pictureList.add(R.drawable.fail);
        pictureList.add(R.drawable.grades);
        pictureList.add(R.drawable.sleep);
        pictureList.add(R.drawable.study);
        pictureList.add(R.drawable.waiting);
        pictureList.add(R.drawable.working);
        pictureList.add(R.drawable.guilty);
        // Add more pictures as needed

        // Create and set the adapter
        adapter = new PictureAdapter(pictureList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}


