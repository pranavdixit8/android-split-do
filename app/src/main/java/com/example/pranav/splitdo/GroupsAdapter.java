package com.example.pranav.splitdo;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupViewHolder> {

    public static final String GROUP_NAME_TOKEN = "name";
    public static final String GROUP_ID_TOKEN = "groupId";

    private ArrayList<GroupObject> mGroups = new ArrayList<>();

    public void clearData(){
        mGroups.clear();
        notifyDataSetChanged();

    }


    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_list_item,parent,false);

        GroupViewHolder viewHolder = new GroupViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {

        if(mGroups!=null) {

            GroupObject obj = mGroups.get(position);
            String name = obj.getText();
            holder.mGroupNameTextView.setText(name);
        }
    }

    @Override
    public int getItemCount() {
        if(mGroups==null)return 0;
        return mGroups.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mGroupNameTextView ;

        public GroupViewHolder(View itemView) {
            super(itemView);
            mGroupNameTextView = (TextView) itemView.findViewById(R.id.tv_group_name);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String name =  mGroups.get(position).getText();
            String groupId = mGroups.get(position).getGroupID();
            Intent intent = new Intent(v.getContext(), GroupTasksActivity.class);

            intent.putExtra(GROUP_NAME_TOKEN, name);
            intent.putExtra(GROUP_ID_TOKEN, groupId);
            v.getContext().startActivity(intent);
        }
    }

    public void addObject(GroupObject obj) {
        if (obj == null) {
            return;
        }
        mGroups.add(obj);
        this.notifyDataSetChanged();

    }
}
