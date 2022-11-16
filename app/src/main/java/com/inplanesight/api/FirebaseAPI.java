package com.inplanesight.api;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.google.firebase.auth.FirebaseAuth;
import com.inplanesight.R;
import com.inplanesight.models.Users;
import com.inplanesight.ui.account.AccountFragment;

public class FirebaseAPI extends Service {

    private final FirebaseAuth mAuth;

    public FirebaseAPI() {
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}