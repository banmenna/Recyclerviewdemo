package com.example.server;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;

import java.security.Provider;

public class MainActivity extends AppCompatActivity {
MyService myService=null;
    private static final String TAG="Tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ServiceConnection serviceConnection=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.v(TAG,"onServiceConnected");
                myService=((MyService.LocalBinder)service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

                Log.v(TAG,"onServiceDisconnected");
            }
        };
        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this,MyService.class);
                bindService(intent,serviceConnection, Service.BIND_AUTO_CREATE);
            }
        });
        Button button2=(Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myService!=null){
                    Log.v(TAG,"Using Service add:"+myService.add(1,2));
                    Log.v(TAG,"Using Service sub:"+myService.sub(1,2));
                    Log.v(TAG,"Using Service mutil:"+myService.mutil(1,2));
                    Log.v(TAG,"Using Service divi:"+myService.divi(1,2));

                }
            }
        });
        Button button3=(Button)findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(serviceConnection);
            }
        });
    }
}