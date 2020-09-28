package com.example.server;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    public MyService() {

    }

    private static final String TAG="myServiceTag";
    private LocalBinder myBinder=new LocalBinder();
    public class LocalBinder extends Binder{
        MyService getService(){
            return  MyService.this;
        }
    }
    public int add(int x,int y){
        return x+y;
    }
    public int sub(int x,int y){
        return x-y;
    }
    public int mutil(int x,int y){
        return x*y;
    }
    public int divi(int x,int y){
        return x/y;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       // throw new UnsupportedOperationException("Not yet implemented");
        Log.v(TAG,"onBinder()");
        return myBinder;
    }
    public boolean onUnbind(Intent intent){
        Log.v(TAG,"onUnbind()");
        return super.onUnbind(intent);
    }
}
