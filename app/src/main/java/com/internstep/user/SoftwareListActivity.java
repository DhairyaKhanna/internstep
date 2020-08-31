package com.internstep.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.internstep.user.Models.Companies;
import com.internstep.user.Models.Jobs;

import java.util.HashMap;
import java.util.Map;

public class SoftwareListActivity extends AppCompatActivity {
    EditText software;
    Button submit,back;
    String job_name,company_name,soft_list;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    boolean applied = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart_application);

        software = findViewById(R.id.edittext);
        submit = findViewById(R.id.button_continue_a1);
        back = findViewById(R.id.toolbar_back_smart_application);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            company_name =(String) b.get("company_name");
            job_name = (String)b.get("job_position");


        }
        assert job_name != null;
        //Log.i("job3",job_name);
        //Log.i("company2",company_name);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SoftwareListActivity.this,JobsDescription.class);
                intent.putExtra("company_name",company_name);
                intent.putExtra("job_position",job_name);
                startActivity(intent);
                finish();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applied = true;
                 soft_list = software.getText().toString();
                //Log.i("soft",soft_list);
                reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("companies");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot companySnapshot:snapshot.getChildren()){
                            Companies companies = companySnapshot.getValue(Companies.class);
                            assert companies != null;
                            if(companies.getCompany_name().equals(company_name)){
                                //String key1 = companySnapshot.getKey();

                                for(DataSnapshot jobSnapshot:companySnapshot.child("job_roles").getChildren()){
                                    Jobs jobs = jobSnapshot.getValue(Jobs.class);
                                    assert jobs != null;
                                    if(jobs.getJob_name().equals(job_name)) {
                                        jobSnapshot.getRef().child("software").setValue(soft_list);
                                        jobs.setSoftware(soft_list);
                                        //String str = "Yes";
                                        //Map<String,Object> hashMap= new HashMap<>();
                                        //hashMap.put("software",soft_list);



                                    }

                                }

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if(soft_list.isEmpty()){
                    Toast.makeText(SoftwareListActivity.this,"Please answer the question",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(SoftwareListActivity.this, ResumeActivity.class);
                    intent.putExtra("company_name", company_name);
                    intent.putExtra("job_position", job_name);
                    startActivity(intent);
                }


            }
        });


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }

}
