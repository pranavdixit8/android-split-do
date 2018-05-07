package com.example.pranav.splitdo;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class AddMembersActivity extends AppCompatActivity {

    public static final String TAG = AddMembersActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS =100 ;

    private boolean flag=true;


    EditText mContactNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);

        mContactNameEditText = findViewById(R.id.et_contact_name);

        mContactNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: " );

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.d(TAG, "onTextChanged: ");
                String searchString = s.toString().trim();
                setUpContacts(searchString);

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged:  " + s);


            }
        });




    }

    private void setUpContacts(String searchString) {


        ContactsFragment fragment = new ContactsFragment();
        fragment.setSearchString(searchString);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if(flag) {

            fragmentManager.beginTransaction()
                    .add(R.id.contacts_container, fragment)
                    .commit();
            flag =false;
            Log.d(TAG, "setUpContacts: 1");
        }else{
            fragmentManager.beginTransaction()
                    .replace(R.id.contacts_container, fragment)
                    .commit();
            Log.d(TAG, "setUpContacts: 2");
        }
    }
}
