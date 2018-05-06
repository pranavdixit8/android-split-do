package com.example.pranav.splitdo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private static final String TAG = MainActivity.class.getSimpleName();



    private FloatingActionButton mGroupFab;
    private FloatingActionButton mTasksFab;

    private PersonalTasksFragment mFragment1;
    private PersonalTasksFragment mFragment2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragment1 = new PersonalTasksFragment();
        mFragment2 = new PersonalTasksFragment();//placeholder for the time being


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
