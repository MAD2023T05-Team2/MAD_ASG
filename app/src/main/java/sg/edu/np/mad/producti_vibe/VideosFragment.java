package sg.edu.np.mad.producti_vibe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

public class VideosFragment extends Fragment {

    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    private List<Integer> videoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videos, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        videoList = new ArrayList<>();

        // Add the video resource IDs to the list
        videoList.add(R.raw.raining_trees);
        videoList.add(R.raw.bird_chirping);
        videoList.add(R.raw.raining_street);
        // Add more videos as needed

        // Create and set the adapter
        videoAdapter = new VideoAdapter(videoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(videoAdapter);

        return view;
    }
}
