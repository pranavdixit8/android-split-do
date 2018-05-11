package com.example.pranav.splitdo;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.pranav.splitdo.MainActivity.getUid;
import static com.example.pranav.splitdo.MainActivity.getUser;

public class SplitdoIntentService extends IntentService {

    public static final String TAG = SplitdoIntentService.class.getSimpleName();

    public static final String ACTION_GET_PENDING_TASKS_COUNT = "get pending tasks count";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserTasksDatabaseRef;
    private String mUid;
    private UserObject mUser;

    private ArrayList<TaskObject> mTasks =  new ArrayList<>();

    private ChildEventListener mChildEventListener;

    public SplitdoIntentService() {
        super("SplitdoIntentService");
    }




    public static void updateWithCount(Context context) {
        Intent intent = new Intent(context, SplitdoIntentService.class);
        intent.setAction(ACTION_GET_PENDING_TASKS_COUNT);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(intent!=null) {

            mUid = getUid();
            mUser = getUser();
            mFirebaseDatabase = FirebaseDatabase.getInstance();

            if (mUid != null) {
                mUserTasksDatabaseRef = mFirebaseDatabase.getReference().child("users").child(mUid).child("tasks");
                String action = intent.getAction();
                if (ACTION_GET_PENDING_TASKS_COUNT.equals(action)) {

                    mChildEventListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            TaskObject object = dataSnapshot.getValue(TaskObject.class);
                            mTasks.add(object);

                            makeCountUpdate();
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {


                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    mUserTasksDatabaseRef.addChildEventListener(mChildEventListener);

                }
            }

        }

    }

    private int getPendingTaskCount() {
        int count = 0;
        for(TaskObject obj :mTasks){
            String status = obj.getStatus();
            if(status.equals("created")){
                count++;
            }
        }
        return count;
    }

    void makeCountUpdate(){

        int count = getPendingTaskCount();


        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getBaseContext(), SplitdoAppWidget.class));

        SplitdoAppWidget.updateTasksWidgets(getBaseContext(), appWidgetManager, count, appWidgetIds);

    }
}
