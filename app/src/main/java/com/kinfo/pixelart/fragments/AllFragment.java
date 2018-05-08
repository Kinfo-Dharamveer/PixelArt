package com.kinfo.pixelart.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kinfo.pixelart.R;
import com.kinfo.pixelart.adapter.AllAdapter;
import com.kinfo.pixelart.model.ImagesData;

import java.util.ArrayList;


public class AllFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AllFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private RecyclerView all_recycler_view;
    private AllAdapter allAdapter;
    private ArrayList<ImagesData> imagesDataList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_all,container,false);


        all_recycler_view = view.findViewById(R.id.all_recycler_view);


        imagesDataList = fill_with_data();


        allAdapter=new AllAdapter(getActivity(), imagesDataList);

        LinearLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        all_recycler_view.setLayoutManager(gridLayoutManager);
        all_recycler_view.setAdapter(allAdapter);

        return view;

    }

    private ArrayList<ImagesData> fill_with_data() {


        ArrayList<ImagesData> data = new ArrayList<>();

        data.add(new ImagesData(R.drawable.animal));
        data.add(new ImagesData(R.drawable.animal));
        data.add(new ImagesData(R.drawable.animal));
        data.add(new ImagesData(R.drawable.animal));
        data.add(new ImagesData(R.drawable.animal));
        data.add(new ImagesData(R.drawable.animal));
        data.add(new ImagesData(R.drawable.animal));
        data.add(new ImagesData(R.drawable.animal));
        data.add(new ImagesData(R.drawable.animal));


        return data;
    }


}
