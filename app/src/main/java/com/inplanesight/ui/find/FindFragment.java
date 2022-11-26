package com.inplanesight.ui.find;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.inplanesight.api.FirebaseAPI;
import com.inplanesight.data.GameViewModel;
import com.inplanesight.data.StateViewModel;
import com.inplanesight.models.Airport;
import com.inplanesight.models.Coordinates;
import com.inplanesight.models.Hunt;

import java.io.IOException;
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
        ViewPager2 viewPager = requireView().findViewById(R.id.findImgCarousel);
        ArrayList<ViewPagerItem> viewPagerItems = new ArrayList<>();
        ArrayList<String> imageRefs = new ArrayList<>();

        GameViewModel gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        gameViewModel.getGame().observe(getViewLifecycleOwner(), game -> {
            if (game != null) {
                if (viewPagerItems.isEmpty()) {
                    for (Hunt hunt: game.getScavengerHunt()) {
                        imageRefs.add(hunt.getImageRef());
                    }

                    for(int i=0; i<imageRefs.size(); i++) {
                        int finalI = i;
                        FirebaseAPI.downloadPhotoFromStorage(imageRefs.get(i), bytes -> {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            ViewPagerItem viewPagerItem = new ViewPagerItem(bitmap);
                            viewPagerItems.add(viewPagerItem);
                            if (finalI == imageRefs.size() - 1) {
                                MyPagerAdapter myPagerAdapter = new MyPagerAdapter(viewPagerItems);
                                viewPager.setAdapter(myPagerAdapter);
                            }
                        });
                    }
                }
            }
        });

        Button foundBtn = requireView().findViewById(R.id.foundHuntBtn);

        foundBtn.setOnClickListener(v -> {
            /** TODO: Call appropriate method in HuntViewModel that checks the user location */
        });
    }
}