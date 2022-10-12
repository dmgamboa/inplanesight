package com.inplanesight.ui.flight_info;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.inplanesight.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FlightInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FlightInfoFragment extends Fragment {
    public int counter = 60;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FlightInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FlightInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FlightInfoFragment newInstance(String param1, String param2) {
        FlightInfoFragment fragment = new FlightInfoFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flight_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = this.getView().findViewById(R.id.tvTimer);

        new CountDownTimer(60000, 1000){
            public void onTick(long millisUntilFinished){
                textView.setText(String.valueOf(counter--) + "s");
            }
            public void onFinish(){
                textView.setText("YOU'RE LATE!!");
            }
        }.start();

        Button button = this.getView().findViewById(R.id.btnRefresh);

        
    }
}