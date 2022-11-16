package com.inplanesight.ui.common;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.inplanesight.R;
import com.inplanesight.api.FirebaseAPI;
import com.inplanesight.models.Users;
import com.inplanesight.ui.account.AccountFragment;

import java.util.concurrent.Executor;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "EmailPassword";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final FirebaseAPI mAuth = new FirebaseAPI();
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mNicknameField;

    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
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
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Views
        mStatusTextView = getView().findViewById(R.id.status);
        mDetailTextView = getView().findViewById(R.id.detail);
        mEmailField = getView().findViewById(R.id.field_email);
        mPasswordField = getView().findViewById(R.id.field_password);
        mNicknameField = getView().findViewById(R.id.field_nickname);

        // Buttons
        getView().findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        getView().findViewById(R.id.email_create_account_button).setOnClickListener(this);
    }

    public void signInController(FirebaseUser user) {
        if (user != null) {
            Navigation.findNavController(getView()).navigate(R.id.action_signInFragment_to_accountFragment);
        } else {
            mStatusTextView.setText("Signed out");
            mDetailTextView.setText(null);

            getView().findViewById(R.id.email_create_account_button).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.email_sign_in_button).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.field_nickname).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.field_password).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getmAuth().getCurrentUser();
        signInController(currentUser);
    }

    public void addUser(Users users) {

        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Add data to firestore
        db.collection("users").document().set(users)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Navigation.findNavController(getView()).navigate(R.id.action_signInFragment_to_accountFragment);
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        // If sign in fails, display a message to the user.
                        Toast.makeText(getActivity(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createAccount(String nickname, String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        EditText passwordView = getView().findViewById(R.id.field_password);

        //Minimum length for password
        final int MIN_LENGTH = 6;

        //If password length is less than 6 characters, return error
        if (passwordView.length() < MIN_LENGTH) {
            Toast.makeText(getActivity(), "Password must be of length 6 or more",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        //Create the account
        mAuth.getmAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            //Create new User object
                            Users users = new Users(nickname, email, mAuth.getmAuth().getCurrentUser().getUid());

                            //Add data to firestore
                            addUser(users);
                        } else {
                            Log.d(TAG, task.getException().getMessage());
                        }
                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.getmAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, go to account fragment
                            Navigation.findNavController(getView()).navigate(R.id.action_signInFragment_to_accountFragment);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            mStatusTextView.setText("Auth failed");
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_create_account_button) {
            createAccount(mNicknameField.getText().toString(), mEmailField.getText().toString(),
                    mPasswordField.getText().toString());
        } else if (i == R.id.email_sign_in_button) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }
}