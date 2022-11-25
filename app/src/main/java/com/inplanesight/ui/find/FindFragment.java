package com.inplanesight.ui.find;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.inplanesight.R;
import com.inplanesight.data.GameViewModel;
import com.inplanesight.models.Coordinates;
import com.inplanesight.models.Hunt;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class FindFragment extends Fragment {
    int huntIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        GameViewModel game = new ViewModelProvider(requireActivity()).get(GameViewModel.class);

        /** TODO: Event Handler for when hunt[] changes (i.e., timeStampFound changes) */
        /** TODO: Retrieve from GameViewModel */
//        Hunt[] testHunts = {
//        new Hunt("test1", "test1", "https://static.wikia.nocookie.net/phineasandferb/images/5/5d/Doofenshmirtz_Portrait.jpg", new Coordinates(), null),
//        new Hunt("test1", "test1", "https://static.wikia.nocookie.net/phineasandferb/images/1/12/Perry_in_the_backyard.jpg", new Coordinates(), Date.from(Instant.now())),
//        new Hunt("test1", "test1", "https://static.wikia.nocookie.net/phineasandferb/images/6/66/Profile_-_Perry_the_Platypus.PNG", new Coordinates(), null)};

        Button foundBtn = requireView().findViewById(R.id.foundHuntBtn);

//        ImageView findImg = requireView().findViewById(R.id.findImg);
//        findImg.setImageBitmap(testHunts[0].getImgAsBitmap());

        foundBtn.setOnClickListener(v -> {
            /** TODO: Call appropriate method in HuntViewModel that checks the user location */
        });
    }
}