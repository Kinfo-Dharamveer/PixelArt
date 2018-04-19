package com.kinfo.pixelart.tabs.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kinfo.pixelart.R;
import com.kinfo.pixelart.adapter.CustomAdapter;
import com.kinfo.pixelart.model.ImageModel;

import java.util.ArrayList;

/**
 * Created by kinfo on 4/13/2018.
 */

public class HomeFragment extends Fragment {

    ViewGroup rootView;
    ArrayList<ImageModel> list,animal_list,fashion_list,kids_list,pleasure_list,food_list,people_list,game_list,holiday_list,nature_list;
    ImageModel model;
    RecyclerView recyclerView_animals,recyclerView_fashion,recyclerView_kids_time,recyclerView_pleasure,recyclerView_food,recyclerView_people,recyclerView_game,recyclerView_holiday,recyclerView_nature;
    int[] imgArrayAnimals = new int[] {R.drawable.doggy,R.drawable.kitty,R.drawable.puppy,R.drawable.animal};
    int[] imgArrayFashion = new int[] {R.drawable.woman,R.drawable.girly};
    int[] imgArrayKids = new int[] {R.drawable.cartoon,R.drawable.wolf,R.drawable.kitty};
    int[] imgArrayPleasure = new int[] {R.drawable.home};
    int[] imgArrayFood = new int[] {R.drawable.ice_cream};
    int[] imgArrayPeople = new int[] {R.drawable.boy,R.drawable.girly};
    int[] imgArrayGame = new int[] {R.drawable.star_img};
    int[] imgArrayHoliday = new int[] {R.drawable.bunny};
    int[] imgArrayNature = new int[] {R.drawable.flowers};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.home, container, false);

        recyclerView_animals = (RecyclerView) rootView.findViewById(R.id.animals);
        recyclerView_fashion = (RecyclerView) rootView.findViewById(R.id.fashion);
        recyclerView_kids_time = (RecyclerView) rootView.findViewById(R.id.kids_time);
        recyclerView_pleasure = (RecyclerView) rootView.findViewById(R.id.pleasure);
        recyclerView_food = (RecyclerView) rootView.findViewById(R.id.food);
        recyclerView_people = (RecyclerView) rootView.findViewById(R.id.people);
        recyclerView_game = (RecyclerView) rootView.findViewById(R.id.game);
        recyclerView_holiday = (RecyclerView) rootView.findViewById(R.id.holiday);
        recyclerView_nature = (RecyclerView) rootView.findViewById(R.id.nature);

        recyclerView_animals.setHasFixedSize(true);
        recyclerView_fashion.setHasFixedSize(true);
        recyclerView_kids_time.setHasFixedSize(true);
        recyclerView_pleasure.setHasFixedSize(true);
        recyclerView_food.setHasFixedSize(true);
        recyclerView_people.setHasFixedSize(true);
        recyclerView_game.setHasFixedSize(true);
        recyclerView_holiday.setHasFixedSize(true);
        recyclerView_nature.setHasFixedSize(true);

        animal_list = new ArrayList<ImageModel>();
        fashion_list = new ArrayList<ImageModel>();
        kids_list = new ArrayList<ImageModel>();
        pleasure_list = new ArrayList<ImageModel>();
        food_list = new ArrayList<ImageModel>();
        people_list = new ArrayList<ImageModel>();
        game_list = new ArrayList<ImageModel>();
        holiday_list = new ArrayList<ImageModel>();
        nature_list = new ArrayList<ImageModel>();

        for(int i=0; i<imgArrayAnimals.length; i++){
            model = new ImageModel();
            model.setImage(imgArrayAnimals[i]);
            model.setAd_status(0);
            model.setSubscription_status(1);

            animal_list.add(model);
        }

        for(int i=0; i<imgArrayFashion.length; i++){
            model = new ImageModel();
            model.setImage(imgArrayFashion[i]);
            model.setAd_status(0);
            model.setSubscription_status(1);

            fashion_list.add(model);
        }

        for(int i=0; i<imgArrayKids.length; i++){
            model = new ImageModel();
            model.setImage(imgArrayKids[i]);
            model.setAd_status(0);
            model.setSubscription_status(1);

            kids_list.add(model);
        }

        for(int i=0; i<imgArrayPleasure.length; i++){
            model = new ImageModel();
            model.setImage(imgArrayPleasure[i]);
            model.setAd_status(0);
            model.setSubscription_status(1);

            pleasure_list.add(model);
        }

        for(int i=0; i<imgArrayFood.length; i++){
            model = new ImageModel();
            model.setImage(imgArrayFood[i]);
            model.setAd_status(0);
            model.setSubscription_status(1);

            food_list.add(model);
        }

        for(int i=0; i<imgArrayPeople.length; i++){
            model = new ImageModel();
            model.setImage(imgArrayPeople[i]);
            model.setAd_status(0);
            model.setSubscription_status(1);

            people_list.add(model);
        }

        for(int i=0; i<imgArrayGame.length; i++){
            model = new ImageModel();
            model.setImage(imgArrayGame[i]);
            model.setAd_status(0);
            model.setSubscription_status(1);

            game_list.add(model);
        }

        for(int i=0; i<imgArrayHoliday.length; i++){
            model = new ImageModel();
            model.setImage(imgArrayHoliday[i]);
            model.setAd_status(0);
            model.setSubscription_status(1);

            holiday_list.add(model);
        }

        for(int i=0; i<imgArrayNature.length; i++){
            model = new ImageModel();
            model.setImage(imgArrayNature[i]);
            model.setAd_status(0);
            model.setSubscription_status(1);

            nature_list.add(model);
        }


        //if (list.size() > 0 & recyclerView_animals != null) {
            recyclerView_animals.setAdapter(new CustomAdapter(animal_list,getActivity()));
            recyclerView_fashion.setAdapter(new CustomAdapter(fashion_list,getActivity()));
            recyclerView_kids_time.setAdapter(new CustomAdapter(kids_list,getActivity()));
            recyclerView_pleasure.setAdapter(new CustomAdapter(pleasure_list,getActivity()));
            recyclerView_food.setAdapter(new CustomAdapter(food_list,getActivity()));
            recyclerView_people.setAdapter(new CustomAdapter(people_list,getActivity()));
            recyclerView_game.setAdapter(new CustomAdapter(game_list,getActivity()));
            recyclerView_holiday.setAdapter(new CustomAdapter(holiday_list,getActivity()));
            recyclerView_nature.setAdapter(new CustomAdapter(nature_list,getActivity()));
        //}

        LinearLayoutManager MyLayoutManager_animals = new LinearLayoutManager(getActivity());
        MyLayoutManager_animals.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager MyLayoutManager_fashion = new LinearLayoutManager(getActivity());
        MyLayoutManager_fashion.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager MyLayoutManager_kids_time = new LinearLayoutManager(getActivity());
        MyLayoutManager_kids_time.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager MyLayoutManager_pleasure = new LinearLayoutManager(getActivity());
        MyLayoutManager_pleasure.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager MyLayoutManager_food = new LinearLayoutManager(getActivity());
        MyLayoutManager_food.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager MyLayoutManager_people = new LinearLayoutManager(getActivity());
        MyLayoutManager_people.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager MyLayoutManager_game = new LinearLayoutManager(getActivity());
        MyLayoutManager_game.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager MyLayoutManager_holiday = new LinearLayoutManager(getActivity());
        MyLayoutManager_holiday.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager MyLayoutManager_nature = new LinearLayoutManager(getActivity());
        MyLayoutManager_nature.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView_animals.setLayoutManager(MyLayoutManager_animals);
        recyclerView_fashion.setLayoutManager(MyLayoutManager_fashion);
        recyclerView_kids_time.setLayoutManager(MyLayoutManager_kids_time);
        recyclerView_pleasure.setLayoutManager(MyLayoutManager_pleasure);
        recyclerView_food.setLayoutManager(MyLayoutManager_food);
        recyclerView_people.setLayoutManager(MyLayoutManager_people);
        recyclerView_game.setLayoutManager(MyLayoutManager_game);
        recyclerView_holiday.setLayoutManager(MyLayoutManager_holiday);
        recyclerView_nature.setLayoutManager(MyLayoutManager_nature);

        //findIds();
        return rootView;
    }
}
