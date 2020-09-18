package com.example.recall;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    private final static String SharedPreferencesFileName = "config";
    private final static String Key_UserName="UserName";
    private final static String Key_UserNumber="UserNumber";
    private final static String Key_UserType="UserType";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private String MyFileName="fileDemo.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        preferences=getSharedPreferences(SharedPreferencesFileName,MODE_PRIVATE);
        editor=preferences.edit();
        Button btnWrite=(Button)findViewById(R.id.buttonWrite);
        Button btnRead=(Button)findViewById(R.id.buttonRead);
        Button btWrite=(Button)findViewById(R.id.btnWrite);
        Button btRead=(Button)findViewById(R.id.btnRead);
        btnWrite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


                editor.putString(Key_UserName,"黄晴");
                editor.putString(Key_UserNumber,"2018011330");

                editor.apply();
            }
                                        });

        btnRead.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String strUserName=preferences.getString(Key_UserName,null);
                String strUserNumber = preferences.getString(Key_UserNumber,null);

                if(strUserName!=null&&strUserNumber!=null)
                    Toast.makeText(MainActivity.this,"学号:"+strUserNumber+" 姓名："+strUserName
                           ,Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this,"无数据",Toast.LENGTH_LONG).show();
            }
            });
        Button button=(Button)findViewById(R.id.buttonCall);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity.this,"缺少打电话权限",Toast.LENGTH_SHORT).show();
                return;
                }
                String phonenumber="15001368850";
                String encodedPhonenumber=null;
                try{
                    encodedPhonenumber= URLEncoder.encode(phonenumber,"UTF-8");
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+encodedPhonenumber)));
            }
        });


        btWrite.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                OutputStream out = null;
                try {
                    FileOutputStream
                            fileOutputStream = openFileOutput(MyFileName, MODE_PRIVATE);
                    out = new BufferedOutputStream(fileOutputStream);
                    String content = "2018011330-huangqing";
                    try {
                        out.write(content.getBytes(StandardCharsets.UTF_8));

                    } finally {
                        if (out != null)
                        {
                            out.flush();
                            out.close();}
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

    });

        btRead.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                InputStream in=null;
                try {
                    FileInputStream fileInputStream = openFileInput(MyFileName);
                    in = new BufferedInputStream(fileInputStream);
                    int c;
                    StringBuilder stringBuilder = new StringBuilder("");
                    try {
                        while ((c = in.read()) != -1) {

                            stringBuilder.append((char) c);

                            Toast.makeText(MainActivity.this, stringBuilder.toString(), Toast.LENGTH_LONG).show();
                        }
                    }finally{
                            if (in != null)
                                in.close();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


            }
                });

}

        }
