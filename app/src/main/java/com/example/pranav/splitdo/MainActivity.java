package com.example.pranav.splitdo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String ANONYMOUS = "anonymous";
    private static final int RC_SIGN_IN =1;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static UserObject mUser;
    private static String mUid;

    private String mUsername;

    private FloatingActionButton mGroupFab;
    private FloatingActionButton mTasksFab;

    private PersonalTasksFragment mFragment1;
    private GroupsFragment mFragment2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mFirebaseAuth = FirebaseAuth.getInstance();
       

        mFragment1 = new PersonalTasksFragment();
        mFragment2 = new GroupsFragment();


        mGroupFab = (FloatingActionButton) findViewById(R.id.fab_add_group);

        mGroupFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTaskIntent = new Intent(MainActivity.this, AddGroupActivity.class);
                startActivity(addTaskIntent);
            }
        });


        mTasksFab = (FloatingActionButton) findViewById(R.id.fab_add_task);

        mTasksFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTaskIntent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(addTaskIntent);
            }
        });

//        ViewPager viewPager = (ViewPager) findViewById(R.id.home_viewpager);
//        TabsPagerAdapter tabAdapter = new TabsPagerAdapter(getSupportFragmentManager());
//        tabAdapter.setFragments(mFragment1,mFragment2);
//
//        PagerAdapter pagerAdapter = tabAdapter;
//
//        viewPager.setAdapter(pagerAdapter);
//
//        viewPager.addOnPageChangeListener(this);



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();


                if(user!=null){
                    mUid = user.getUid();
                    mUser = new UserObject(user.getDisplayName(),user.getEmail());
                    Toast.makeText(MainActivity.this,"You are signed in", Toast.LENGTH_SHORT);
                    onSignInStart(user.getDisplayName());
                }else {
                    onSignOutEnd();
                    startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false).
                                    setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build())).build(), RC_SIGN_IN);
                }
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Welcome, Hi from split.do", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_signout){
            AuthUI.getInstance().signOut(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }
    private void onSignInStart(String displayName) {
        mUsername = displayName;
        attachViewPagerFragments();
    }


    private void onSignOutEnd() {

        mUsername = ANONYMOUS;
        mFragment1.clearAdapterData();


    }






    private void attachViewPagerFragments() {

        ViewPager viewPager = (ViewPager) findViewById(R.id.home_viewpager);
        TabsPagerAdapter tabAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        tabAdapter.setFragments(mFragment1,mFragment2);

        PagerAdapter pagerAdapter = tabAdapter;

        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mGroupFab.setVisibility(View.INVISIBLE);
                return;
            case 1:
                mGroupFab.setVisibility(View.VISIBLE);
                return;

            default:
                return;

        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
