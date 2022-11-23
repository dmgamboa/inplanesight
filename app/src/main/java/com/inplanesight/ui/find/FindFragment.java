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
        Hunt[] testHunts = {
        new Hunt("test1", "test1", "https://static.wikia.nocookie.net/phineasandferb/images/5/5d/Doofenshmirtz_Portrait.jpg", new Coordinates(), null),
        new Hunt("test1", "test1", "https://static.wikia.nocookie.net/phineasandferb/images/1/12/Perry_in_the_backyard.jpg", new Coordinates(), Date.from(Instant.now())),
        new Hunt("test1", "test1", "https://static.wikia.nocookie.net/phineasandferb/images/6/66/Profile_-_Perry_the_Platypus.PNG", new Coordinates(), null)};

//        try {
//            ViewPager2 carousel = getView().findViewById(R.id.findImgCarousel);
//            FindRecyclerViewAdapter adapter = new FindRecyclerViewAdapter(getContext(), testHunts);
//            carousel.setAdapter(adapter);
//        } catch (Exception e) {
//            Log.d("In Plane Sight", e.getMessage());
//        }
//
//        Button[] carouselIndicators = { requireView().findViewById(R.id.findImgBtn1), requireView().findViewById(R.id.findImgBtn2), requireView().findViewById(R.id.findImgBtn3)};
//        Button foundBtn = requireView().findViewById(R.id.foundHuntBtn);

//        carousel.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                for (int i = 0; i < carouselIndicators.length; i++) {
//                    if (i != position) {
//                        carouselIndicators[i].setBackgroundColor(getResources().getColor(R.color.grey, null));
//                    } else {
//                        carouselIndicators[i].setBackgroundColor(getResources().getColor(R.color.aqua, null));
//                    }
//                }
//
//                /** TODO: Change testHunts from ViewModel */
//                if (testHunts[position].getTimestampFound() != null) {
//                    foundBtn.setEnabled(false);
//                } else {
//                    foundBtn.setEnabled(true);
//                }
//            }
//        });

//        foundBtn.setOnClickListener(v -> {
//            /** TODO: Call appropriate method in HuntViewModel that checks the user location */
//        });
    }
}