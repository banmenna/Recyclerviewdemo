package com.example.handle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static String TAG="tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView=(TextView)findViewById(R.id.txtContent);

        //Handler handler=new Handler();

        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {

                textView.setText(msg.arg1+"");
            }
        };
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView.setText("123");
            }
        });


        final Runnable myWorker=new Runnable() {
            @Override
            public void run() {
                int progress=0;
                while(progress<=100){
                    Message msg=new Message();
                    msg.arg1=progress;
                    handler.sendMessage(msg);
                    progress+=1;
                    Log.v(TAG,progress+su(progress)+"素数");

                    try{
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Message msg=handler.obtainMessage();
                msg.arg1=-1;
                handler.sendMessage(msg);
            }
        };
        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread workThread=new Thread(null,myWorker,"WorkThread");
                workThread.start();
            }
        });
    }

    private String su(int k) {
        String y = null;
        //String n = "不是";
        int i;
        int j = (int) k / 2;
        for (i = 2; i <= j; i++) {
            if (k % i == 0) {
                 y="是";
                break;
            }
        }
        if (i > j) {
            y="不是";
            
        }
        return y;
    }
}




