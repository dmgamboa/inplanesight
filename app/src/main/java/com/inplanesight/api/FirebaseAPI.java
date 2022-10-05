package com.inplanesight.api;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FirebaseAPI extends Service {
    public FirebaseAPI() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}