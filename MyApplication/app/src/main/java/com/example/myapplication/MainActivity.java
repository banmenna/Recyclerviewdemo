package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MyActivity:","onCreate");

       Button button=(Button)findViewById(R.id.button_first);
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(MainActivity.this,MainActivity2.class);
               intent.putExtra("name","zhang");
               intent.putExtra("age",55);
               startActivityForResult(intent,0);
           }
       });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent
            data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==0&&resultCode==0) {
            String str = data.getStringExtra("result");
            Toast.makeText(this, str, Toast.LENGTH_LONG).show();
        }
    }

    @Override

    protected void onRestart() {

        super.onRestart();

        Log.d("MyActivity:","onRestart");

    }



    @Override

    protected void onStart() {

        super.onStart();


        Log.d("MyActivity:","onStart");

    }



    @Override

    protected void onResume() {

        super.onResume();

        Log.d("MyActivity:","onResume");

    }



    @Override

    protected void onPause() {

        super.onPause();

        Log.d("MyActivity:","onPause");


    }



    @Override

    protected void onStop() {

        super.onStop();

        Log.d("MyActivity:","onStop");


    }



    @Override

    protected void onDestroy() {

        super.onDestroy();

        Log.d("MyActivity:","onDestroy");

    }

    @Override

    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        if (outState != null){

            outState.putString("TEST","test");

            Log.d("MyActivity:","onSaveInstanceState");

        }



    }
}