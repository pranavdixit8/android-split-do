package com.example.pranav.splitdo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static com.example.pranav.splitdo.R.id.ll_main_content;

public class PersonalTasksAdapter extends RecyclerView.Adapter<PersonalTasksAdapter.TaskViewHolder> {

    public static final String TAG = PersonalTasksAdapter.class.getSimpleName();

    private ArrayList<TaskObject> mTasks = new ArrayList<>();
    private Context mContext;
    private float mX, mY;
    private int mPosition =999;
    private boolean mClicked = false;
    private boolean mLongPressed = false;
    private boolean mDeletePressed = false;
    private GeoDataClient mClient;

    private long downTime =0;
    private long upTime =0 ;

    private OnLocationClickListener mClickListener;

    private OnLongPressedUpdate mLongPressedUpdate;

    private final String mUid = MainActivity.getUid();
    private final String mUsername = MainActivity.getUser().getName();

    interface OnLocationClickListener{

        void onclick(LatLng latLng, String name, String address);
    }

    public void setGoogleAPIClient(GeoDataClient client){

        mClient = client;

    }
    interface OnLongPressedUpdate{

        void onLongPressed(TaskObject obj);
    }

    public PersonalTasksAdapter(Context mContext, OnLocationClickListener clickListener, OnLongPressedUpdate onLongPressListener) {
        this.mContext = mContext;
        this.mClickListener = clickListener;
        this.mLongPressedUpdate = onLongPressListener;

    }

    public void clearData(){
        mTasks.clear();
        notifyDataSetChanged();

    }

    public void addObject(TaskObject obj) {
        if (obj == null) {
            return;
        }
        mTasks.add(obj);
        this.notifyDataSetChanged();

    }



    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.task_list_item, parent, false);

        return new TaskViewHolder(view);

    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {

        TaskObject obj = mTasks.get(position);
        String description = obj.getText();
        String date = obj.getCreation_time();
        String location = obj.getLocation();
        String createdBy = obj.getCreator();
        String creatorId = obj.getCreatorID();
        String completedBy = obj.getCompletor();
        String status = obj.getStatus();

        if(position ==mPosition && mDeletePressed){

            holder.mTaskView.setVisibility(GONE);
            mDeletePressed =false;
            return;
        }

        if(status.equals("deleted")){
            holder.mTaskView.setVisibility(GONE);
            return;
        }


        holder.locationView.setTag(location);

        holder.mCreatedView.setText(createdBy);
        if(completedBy!= null)
            holder.mCompletedView.setText(completedBy);

        //Set values
        holder.itemView.setTag(obj.getCompletorID());

        holder.mTaskDescriptionView.setText(description);
        holder.mDateView.setText(date);





        if (position == mPosition && holder.mTaskDescriptionView.getPaintFlags() != Paint.STRIKE_THRU_TEXT_FLAG && mLongPressed ) {
            holder.mTaskDescriptionView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.mDeleteTask.setVisibility(View.VISIBLE);
            mLongPressed =false;
        }else if (position == mPosition && mLongPressed) {
            holder.mTaskDescriptionView.setPaintFlags(holder.mTaskDescriptionView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.mDeleteTask.setVisibility(GONE);
            mLongPressed =false;
        }else if(position == mPosition && mClicked && holder.mDetailView.getVisibility() == GONE){

            holder.mDetailView.setVisibility(View.VISIBLE);
            mClicked = false;
        }
        else if ( position == mPosition && mClicked && holder.mDetailView.getVisibility() == View.VISIBLE){

            holder.mDetailView.setVisibility(GONE);
            mClicked =false;
        }

        if(status.equals("completed") && creatorId.equals(mUid)){

            holder.mTaskDescriptionView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.mDeleteTask.setVisibility(View.VISIBLE);
        }else if(status.equals("completed")) {

            holder.mTaskDescriptionView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

    }

    @Override
    public int getItemCount() {
        if (mTasks == null) {
            return 0;
        }
        return mTasks.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mTaskView;
        LinearLayout mGeneralView;
        LinearLayout mDetailView;

        TextView mTaskDescriptionView;
        TextView mDateView;
        ImageView locationView;
        TextView mCreatedView;
        TextView mCompletedView;
        ImageButton mDeleteTask;


        public TaskViewHolder(View itemView) {
            super(itemView);

            mTaskView =(LinearLayout) itemView.findViewById(R.id.ll_main_content);

            mGeneralView = (LinearLayout) itemView.findViewById(R.id.ll_general_description);
            mTaskDescriptionView = (TextView) itemView.findViewById(R.id.taskDescription);
            mDeleteTask = (ImageButton) itemView.findViewById(R.id.ib_delete_task);

            mDateView = (TextView) itemView.findViewById(R.id.tv_date);

            mDetailView = (LinearLayout) itemView.findViewById(R.id.ll_detail);
            locationView = (ImageView) itemView.findViewById(R.id.tv_location);
            mCreatedView = (TextView) itemView.findViewById(R.id.tv_created);
            mCompletedView = (TextView) itemView.findViewById(R.id.tv_completed);


            MultiOnClickListener multiListeners = new MultiOnClickListener();

            mGeneralView.setOnTouchListener(multiListeners);

            multiListeners.addOnClickListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    view.performClick();

                    int pos = getAdapterPosition();
                    TaskObject obj = mTasks.get(pos);

                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN :
                            downTime=System.currentTimeMillis();
                            break;

                        case MotionEvent.ACTION_UP :
                            upTime=System.currentTimeMillis();
                            if(upTime-downTime>500) {
                                Log.d(TAG, "onTouch: " + (upTime - downTime));
                                mLongPressed =true;
                                mPosition = pos;
                                if(obj.getStatus().equals("created")) {
                                    obj.setCompletorID(mUid);
                                    obj.setCompletor(mUsername);
                                    obj.setStatus("completed");
                                    mLongPressedUpdate.onLongPressed(obj);
                                }
                                else{
                                    obj.setCompletorID(null);
                                    obj.setCompletor("-");
                                    obj.setStatus("created");
                                    mLongPressedUpdate.onLongPressed(obj);

                                }
                                notifyDataSetChanged();
                            }
                            else{
//                                Toast.makeText(view.getContext(), "just a touch", Toast.LENGTH_SHORT).show();
                                mClicked =true;
                                mPosition = pos;
                                notifyDataSetChanged();
                            }

                            return true;
                    }
                    return false;
                }
            });

            mDeleteTask.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    view.performClick();

                    int pos = getAdapterPosition();
                    TaskObject obj = mTasks.get(pos);

                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN :
                            downTime=System.currentTimeMillis();
                            break;

                        case MotionEvent.ACTION_UP :
                            upTime=System.currentTimeMillis();
                            if(upTime-downTime>200) {

//                                mDeletePressed =true;
                                if(obj.getStatus().equals("completed")&&obj.getCreatorID().equals(mUid)) {
                                    mDeletePressed =true;
                                    mPosition = pos;
                                    obj.setStatus("deleted");
                                    mLongPressedUpdate.onLongPressed(obj);

                                    notifyDataSetChanged();
                                }


                            }

                            return true;
                    }


                    return false;
                }
            });




            locationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = (String) view.getTag();
                    if(id == null){
                        Toast.makeText(view.getContext(), view.getContext().getString(R.string.no_location), Toast.LENGTH_SHORT).show();
                        return;}
                    mClient.getPlaceById(id).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                            if (task.isSuccessful()) {
                                PlaceBufferResponse places = task.getResult();
                                Place myPlace = places.get(0);
                                LatLng latLng = myPlace.getLatLng();
                                String name = myPlace.getName().toString();
                                String address = myPlace.getAddress().toString();
                                mClickListener.onclick(latLng,name,address);
                                Log.i(TAG, "Place found: " + myPlace.getName());
                                places.release();
                            } else {
                                Log.e(TAG, "Place not found.");
                            }
                        }
                    });

                }
            });

    }

    }

    private class MultiOnClickListener implements View.OnTouchListener{

        List<View.OnTouchListener> mListeners;

        public MultiOnClickListener(){
            mListeners = new ArrayList<View.OnTouchListener>();
        }

        public void addOnClickListener(View.OnTouchListener listener){
            mListeners.add(listener);
        }


        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            for(View.OnTouchListener listener : mListeners){
                listener.onTouch(view, motionEvent);
            }
            return true;
        }
    }

}
