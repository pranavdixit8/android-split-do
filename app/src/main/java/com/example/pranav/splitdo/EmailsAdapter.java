package com.example.pranav.splitdo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class EmailsAdapter extends RecyclerView.Adapter<EmailsAdapter.EmailViewHolder> {


    private ArrayList<UserObject> mEmails = new ArrayList<>();

    @NonNull
    @Override
    public EmailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.email_list_item,parent,false);

        return new EmailViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull EmailViewHolder holder, int position) {

        UserObject obj = mEmails.get(position);

        String email = obj.getEmail();

        holder.mEmailText.setText(email);

    }

    @Override
    public int getItemCount() {
        if(mEmails == null) return 0;

        return mEmails.size();
    }

//    public void addEmail(String email) {
//        mEmails.add(email);
//    }

    public void addObject(UserObject object) {

        mEmails.add(object);
    }

    public class EmailViewHolder extends RecyclerView.ViewHolder {

        TextView mEmailText;

        public EmailViewHolder(View itemView) {
            super(itemView);
            mEmailText = itemView.findViewById(R.id.tv_email_text);

        }
    }
}
