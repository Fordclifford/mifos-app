package org.mifos.mobile.ui.activities.mpesa;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import org.mifos.mobile.ui.activities.StkPushActivity;


public class MyService extends Service {
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        scheduleNext();
        return Service.START_STICKY;
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void scheduleNext() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                MyService.this.startActivity(new Intent(MyService.this, StkPushActivity.class));
                Toast.makeText(MyService.this.getApplicationContext(), "Call to api made", Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, 1000);
            }
        }, 10000);
    }
}
