package sg.edu.np.mad.producti_vibe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

public class Videos extends Fragment {

    View view;
    VideoView vv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_videos, container, false);
        vv = view.findViewById(R.id.videoView);
        vv.setVideoPath("android.resource://"+ requireActivity().getPackageName() +"/"+R.raw.flowers);
        MediaController med = new MediaController(requireActivity());
        vv.setMediaController(med);
        med.setAnchorView(vv);
        vv.start();
        return view;
    }
}