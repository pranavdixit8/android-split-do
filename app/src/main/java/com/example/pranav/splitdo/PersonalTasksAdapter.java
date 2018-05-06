package com.example.pranav.splitdo;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PersonalTasksAdapter extends RecyclerView.Adapter<PersonalTasksAdapter.TaskViewHolder> {

    public static final String TAG = PersonalTasksAdapter.class.getSimpleName();

    private ArrayList<TaskObject> mTasks = new ArrayList<>();
    private Context mContext;
    private float mX, mY;
    private int mPosition =999;
    private boolean mClicked = false;
    private boolean mMovePressed = false;


    public PersonalTasksAdapter(Context mContext) {
        this.mContext = mContext;
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
        String creeatedBy = obj.getCreator();
        String completedBy = obj.getCompletor();


        holder.locationView.setTag(location);

        holder.mCreatedView.setText(creeatedBy);
        if(completedBy!= null)
            holder.mCompletedView.setText(completedBy);

        //Set values
        holder.itemView.setTag(obj.getCompletorID());

        holder.mTaskDescriptionView.setText(description);
        holder.mDateView.setText(date);



        if (position == mPosition && holder.mTaskDescriptionView.getPaintFlags() != Paint.STRIKE_THRU_TEXT_FLAG && mMovePressed) {
            holder.mTaskDescriptionView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            Log.d(TAG, "onBindViewHolder: " + holder.mTaskDescriptionView.getPaintFlags());
            holder.mDeleteTask.setVisibility(View.VISIBLE);
            mMovePressed =false;
        }else if (position == mPosition && mMovePressed) {
            holder.mTaskDescriptionView.setPaintFlags(holder.mTaskDescriptionView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.mDeleteTask.setVisibility(View.GONE);
            mMovePressed =false;
        }else if(position == mPosition && mClicked && holder.mDetailView.getVisibility() == View.GONE){

            Log.d(TAG, "onBindViewHolder: " +" making visible");
            holder.mDetailView.setVisibility(View.VISIBLE);
            mClicked = false;
        }
        else if ( position == mPosition && mClicked && holder.mDetailView.getVisibility() == View.VISIBLE){
            Log.d(TAG, "onBindViewHolder: " + "maing invisible");
            holder.mDetailView.setVisibility(View.GONE);
            mClicked =false;
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

            mGeneralView = (LinearLayout) itemView.findViewById(R.id.ll_general_description);
            mTaskDescriptionView = (TextView) itemView.findViewById(R.id.taskDescription);
            mDeleteTask = (ImageButton) itemView.findViewById(R.id.ib_delete_task);

            mDateView = (TextView) itemView.findViewById(R.id.tv_date);

            mDetailView = (LinearLayout) itemView.findViewById(R.id.ll_detail);
            locationView = (ImageView) itemView.findViewById(R.id.tv_location);
            mCreatedView = (TextView) itemView.findViewById(R.id.tv_created);
            mCompletedView = (TextView) itemView.findViewById(R.id.tv_completed);

            mGeneralView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();
                    Log.d(TAG, "onClick: " + mClicked);
                    mClicked =true;
                    Log.d(TAG, "onClick: " + mClicked);
                    mPosition = pos;
                    notifyDataSetChanged();
                }
            });


    }
    }
}
