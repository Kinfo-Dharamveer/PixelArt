package com.kinfo.pixelart.tabs.gallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kinfo.pixelart.R;

/**
 * Created by kinfo on 4/17/2018.
 */

public class SubscribeFragment extends Fragment {

    ViewGroup rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.gallery, container, false);

        //findIds();
        return rootView;
    }
}

