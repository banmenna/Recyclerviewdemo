package com.example.contact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = "tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button= findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentResolver resolver = getContentResolver();
                Cursor cursor=resolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

                while (cursor.moveToNext()){
                    String msg;
                    String id=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    msg="id:"+id;
                    String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    msg=msg+"name:"+name;
                    Cursor phoneNumbers=resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+id,null,null);

                    while(phoneNumbers.moveToNext()){
                        String phoneNumber=phoneNumbers.getString(phoneNumbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        msg=msg+"phone:"+phoneNumber;
                    }
                    Cursor emails=resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID+"="+id,null,null);
                    while(emails.moveToNext()){
                        String email=emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        msg=msg+"email:"+email;
                    }

                    Log.v(TAG,msg);
                }
            }
        });
    }
}