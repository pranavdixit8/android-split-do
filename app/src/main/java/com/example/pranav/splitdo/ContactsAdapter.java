package com.example.pranav.splitdo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>{

    private ArrayList<UserObject> mContacts = new ArrayList<>();

    private OnClickContactSelect mOnCLickListener;

    interface OnClickContactSelect{

        void onContactClick(int postion);
    }

    public ContactsAdapter(){}

    public ContactsAdapter(OnClickContactSelect onClickContactSelect){
        mOnCLickListener = onClickContactSelect;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_list_item,parent,false);

        return new ContactsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {

        UserObject obj = mContacts.get(position);
        String name = obj.getName();
        String email = obj.getEmail();
        String uid = obj.getUid();
        holder.mNameText.setText(name);
        holder.mEmailText.setText(email);
        holder.itemView.setTag(uid);

    }

    @Override
    public int getItemCount() {
        if(mContacts == null) return 0;

        return mContacts.size();
    }


    public void addObject(UserObject object) {

        mContacts.add(object);
        notifyDataSetChanged();
    }

     class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

         TextView mNameText;
        TextView mEmailText;


           ContactsViewHolder(View itemView) {
            super(itemView);
            mNameText = itemView.findViewById(R.id.tv_name_text);
            mEmailText = itemView.findViewById(R.id.tv_email_text);
            itemView.setOnClickListener(this);

        }

         @Override
         public void onClick(View view) {
               int pos = getAdapterPosition();
               if(mOnCLickListener!=null) {
                   mOnCLickListener.onContactClick(pos);
               }
         }
     }
}
