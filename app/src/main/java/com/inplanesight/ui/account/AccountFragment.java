package com.inplanesight.ui.account;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.inplanesight.R;
import com.inplanesight.api.FirebaseAPI;
import com.inplanesight.models.Users;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //Fragment internal variables
    private String nickname;
    private String email;
    private final FirebaseAPI mAuth = new FirebaseAPI();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mAuth.getmAuth().getCurrentUser() == null) {
            Navigation.findNavController(getView()).navigate(R.id.action_accountFragment_to_signInFragment);
        } else {
            // Initialize Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            //Get user data
            db.collection("users")
                    .whereEqualTo("id", mAuth.getmAuth().getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Users users = document.toObject(Users.class);

                                //Assign queried data to the nickname and email strings
                                nickname = users.getNickname();
                                email = users.getEmail();

                                //Add the queried data to the UI
                                TextView nicknameUI = getView().findViewById(R.id.nicknameData);
                                TextView emailUI = getView().findViewById(R.id.emailData);

                                nicknameUI.setText(nickname);
                                emailUI.setText(email);

                                break;
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        }

        //Create listener for sign out and verifying email
        Button signOutBtn = getView().findViewById(R.id.signoutBtn);
        signOutBtn.setOnClickListener(this);

        Button verifyEmail = getView().findViewById(R.id.verify_email_button);
        verifyEmail.setOnClickListener(this);
    }

    private void sendVerification() {
        // Disable button
        getView().findViewById(R.id.verify_email_button).setEnabled(false);

        // Send verification email
        final FirebaseUser user = mAuth.getmAuth().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    // Re-enable button
                    getView().findViewById(R.id.verify_email_button).setEnabled(true);

                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(),
                                "Verification email sent to " + user.getEmail(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "sendEmailVerification", task.getException());
                        Toast.makeText(getActivity(),
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signOut() {
        //Log user out
        mAuth.getmAuth().signOut();
        Navigation.findNavController(getView()).navigate(R.id.action_accountFragment_to_signInFragment);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        if (i == R.id.verify_email_button) {
            sendVerification();
            return;
        }

        signOut();
    }
}