package com.inplanesight.data;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FlightsService extends Service {
    public FlightsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}