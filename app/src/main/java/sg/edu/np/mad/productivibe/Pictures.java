package sg.edu.np.mad.productivibe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Pictures extends Fragment {

    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.di_main, container, false);
        return view;
    }
//    imageList = new ArrayList<>();
//    RecyclerView recyclerView = findViewById(R.id.recyclerview);
//    CustomAdapter custAdapter = new CustomAdapter(myList);
//    LinearLayoutManager mylayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(mylayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(custAdapter);
}