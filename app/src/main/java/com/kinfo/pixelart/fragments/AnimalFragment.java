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
import com.kinfo.pixelart.adapter.AnimalAdapter;
import com.kinfo.pixelart.model.ImagesData;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnimalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnimalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AnimalFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AnimalFragment newInstance(String param1, String param2) {
        AnimalFragment fragment = new AnimalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private RecyclerView animal_recycler_view;

    private AnimalAdapter animalAdapter;
    private ArrayList<ImagesData> imagesDataList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_animal,container,false);


        animal_recycler_view = view.findViewById(R.id.animal_recycler_view);


        imagesDataList = fill_with_data();


        animalAdapter=new AnimalAdapter(getActivity(), imagesDataList);

        LinearLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        animal_recycler_view.setLayoutManager(gridLayoutManager);
        animal_recycler_view.setAdapter(animalAdapter);

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
