package com.internstep.user;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.internstep.user.Fragments.ProfileFragment;
import com.internstep.user.Models.Companies;
import com.internstep.user.Models.Jobs;

public class ApplyActivity extends AppCompatActivity {
    EditText ques1,ques2;
    Button submit,back;
    String company_name,job_name;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart_application_3);

        ques1 = findViewById(R.id.edittext_1);
        ques2 = findViewById(R.id.edittext_2);
        submit = findViewById(R.id.submit_application);
        back = findViewById(R.id.toolbar_back_smart_application_3);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            company_name =(String) b.get("company_name");
            job_name = (String)b.get("job_position");


        }

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplyActivity.this,ResumeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String u_ques_1 = ques1.getText().toString();
                final String u_ques_2 = ques2.getText().toString();
                final String applied = "Applied";
                final String yes1 = "Yes";


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

                                        jobs.setQues1(u_ques_1);
                                        jobs.setQues2(u_ques_2);
                                        //jobs.setJob_status("applied");
                                        //String str = "Yes";
                                        //Map<String,Object> hashMap= new HashMap<>();
                                        //hashMap.put("software",soft_list);
                                        if(!u_ques_1.isEmpty() && !u_ques_2.isEmpty()) {
                                            //reference.child("").updateChildren(hashMap);
                                            jobSnapshot.getRef().child("ques1").setValue(u_ques_1);
                                            jobSnapshot.getRef().child("ques2").setValue(u_ques_2);
                                            jobSnapshot.getRef().child("applied").setValue(yes1);
                                            jobSnapshot.getRef().child("job_status").setValue(applied);
                                            //ProfileFragment profileFragment = new ProfileFragment();
                                            Intent intent = new Intent(ApplyActivity.this,MainActivity.class);
                                            Bundle bundle = new Bundle();
                                            String s1 = "hello";
                                            bundle.putString("key", s1);
                                            intent.putExtra("key",s1);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else{
                                            Toast.makeText(ApplyActivity.this,"Please complete the questions",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }
        });


    }
}
