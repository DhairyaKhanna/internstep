package com.internstep.user.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.internstep.user.HomeActivity;
import com.internstep.user.JobsActivity;
import com.internstep.user.Models.Companies;
import com.internstep.user.Models.Jobs;
import com.internstep.user.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder>  {
    private Context mcontext;
    //private List<Companies> mcompanies;
    private String screen;
    private List<Jobs> mjobs;
    private String job_Status,job_name;

    public ActivityAdapter(Context mcontext, List<Jobs> mjobs, String screen) {
        this.mcontext = mcontext;
        this.mjobs = mjobs;
        this.screen = screen;


    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Jobs job_roles = mjobs.get(i);
        Log.i("size",String.valueOf(mjobs.size()));
        /*Map<String, Jobs> map = new HashMap<>();
        map = company.getJob_roles();
        Jobs job =new Jobs();
        for(Map.Entry<String,Jobs> entry : map.entrySet()){

             job_Status = entry.getValue().getJob_status();
             if(!job_Status.equals("default"))
                 job_name = entry.getValue().getJob_name();
        }*/
        //DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("")
        Glide.with(mcontext).load(job_roles.getCompany_logo()).into(viewHolder.company_logo);
        viewHolder.user_name.setText(job_roles.getJob_name());
        viewHolder.job_status.setText(job_roles.getJob_status());
        if(job_roles.getJob_status().equals("Confirmed")) {
            String confirm ="Confirmed your internship";
            viewHolder.job_status.setText(confirm);
            viewHolder.job_status.setTextColor(Color.parseColor("#4E74F4"));
        }
        else if (job_roles.getJob_status().equals("Rejected")){
            String reject ="Rejected your application";
            viewHolder.job_status.setText(reject);
            viewHolder.job_status.setTextColor(Color.parseColor("#F44E4E"));
        }
        else{
            viewHolder.job_status.setText(job_roles.getJob_status());
        }
        Log.i("name1",screen);

        /*viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, HomeActivity.class);
                //intent.putExtra("company_name", company.getCompany_name());
                mcontext.startActivity(intent);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mjobs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView user_name;
        public TextView job_status;
        public ImageView company_logo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.name_of_company_heads);
            job_status = itemView.findViewById(R.id.application_process);
            company_logo = itemView.findViewById(R.id.logo);
        }
    }
}
