package sg.edu.np.mad.producti_vibe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class Pictures extends Fragment {
    private RecyclerView DIrecyclerView;
    private RecyclerView.Adapter DIAdapter;
    int[] destressImages = {R.drawable.grades, R.drawable.guilty, R.drawable.sleep, R.drawable.study, R.drawable.hotdog};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize Recycler View & the Adapter
        DIrecyclerView = DIrecyclerView.findViewById(R.id.pictureRecyclerView);
        DIrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DIrecyclerView.setHasFixedSize(false); // Size of recycler view changes with content
        DIAdapter = new DIAdpater(getActivity(), destressImages);
        DIrecyclerView.setAdapter(DIAdapter);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.di_page, container, false);
    }
}