package com.internstep.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.internstep.user.Models.Companies;
import com.internstep.user.Models.Jobs;
import com.internstep.user.Models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.security.AccessController.getContext;

public class Password extends AppCompatActivity {
    EditText password,confirm_password;
    boolean val= true;
    Button register,back;
    FirebaseAuth mAuth;
    TextView forgot_password;
    private ProgressDialog mProgress;
    DatabaseReference reference;
    private List<Companies> companies;
    private List<Jobs> job_roles;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_screen);

        mAuth = FirebaseAuth.getInstance();
        password = findViewById(R.id.pass);
        confirm_password = findViewById(R.id.confirm_pass);
        register = findViewById(R.id.submit);
        forgot_password = findViewById(R.id.click_here);
        //final User user = new User();
        Intent i = getIntent();
        final User user  = (User)i.getSerializableExtra("user_data");
        companies = new ArrayList<>();
        job_roles = new ArrayList<>();
        mProgress = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        mProgress.setTitle("Registering...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.show();
                if (val) {

                        String fullName = user.getName();
                    String uid = user.getUID();
                    String email = user.getEmailAddress();
                    String phone = user.getPhoneNumber();
                    String gender = user.getGender();
                    String dob = user.getDob();
                    //String u_email = email.getText().toString();
                    String u_password = password.getText().toString();
                    String c_password = confirm_password.getText().toString();


                    if (TextUtils.isEmpty(u_password) || TextUtils.isEmpty(c_password)) {
                        Toast.makeText(Password.this, "All credentials are required!", Toast.LENGTH_SHORT).show();
                    } else if (u_password.length() < 6) {
                        Toast.makeText(Password.this, "Password must contain atleast 6 characters", Toast.LENGTH_SHORT).show();
                    } else if (!(u_password.equals(c_password))) {
                        Toast.makeText(Password.this, "Password must be equal", Toast.LENGTH_SHORT).show();
                    } else {
                        //register(fullName, email, phone, gender, dob, u_password);


                        mAuth.createUserWithEmailAndPassword(email,u_password).addOnCompleteListener(Password.this,new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.i("spark","creat");
                                if (task.isSuccessful()){

                                    Log.i("spark","creating");
                                    //final User users = new Users(firstName,email,lastName, "0");
                                    final FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    assert firebaseUser != null;
                                    user.setUid(firebaseUser.getUid());
                                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            DatabaseReference companyref = FirebaseDatabase.getInstance().getReference("Companies");
                                            final DatabaseReference userref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("companies");


                                            companyref.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    companies.clear();

                                                    for(DataSnapshot companySnapshot:dataSnapshot.getChildren()){
                                                        Companies company = companySnapshot.getValue(Companies.class);
                                                        /*for(DataSnapshot jobsSnapshot:dataSnapshot.getChildren()){
                                                            Companies.Jobs jobs = jobsSnapshot.getValue(Companies.Jobs.class);
                                                        }*/
                                                        //job_roles.clear();
                                                        /*int count= 0;
                                                        for(DataSnapshot jobsSnapshot:companySnapshot.child("job_roles").getChildren()){
                                                            //Jobs jobs = jobsSnapshot.getValue(Jobs.class);
                                                            Object job_name = jobsSnapshot.child("job_name").getValue();
                                                            Object applied = jobsSnapshot.child("applied").getValue();
                                                            Jobs jobs1 = new Jobs();
                                                            jobs1.setJob_name(job_name.toString());
                                                            jobs1.setApplied(applied.toString());
                                                            job_roles.add(jobs1);

                                                            Log.d("count", String.valueOf(count++));
                                                            Log.i("jobs", jobs1.getJob_name());

                                                        }

                                                        boolean x = companySnapshot.child("job_roles").exists();
                                                        //Log.i("job1", String.valueOf(x));

                                                        Object company_name = companySnapshot.child("company_name").getValue();
                                                        Object company_logo = companySnapshot.child("company_logo").getValue();
                                                        Object job_applied = companySnapshot.child("job_applied").getValue();
                                                        Object job_description = companySnapshot.child("job_description").getValue();
                                                        Object job_status = companySnapshot.child("job_status").getValue();
                                                        Object location = companySnapshot.child("location").getValue();
                                                        Object positions = companySnapshot.child("open_positions").getValue();
                                                        Object more_info = companySnapshot.child("more_information").getValue();
                                                        Companies company = new Companies();
                                                        company.setCompany_name(company_name.toString());
                                                        company.setCompany_logo(company_logo.toString());
                                                        company.setJob_applied(job_applied.toString());
                                                        company.setJob_description(job_description.toString());
                                                        company.setJobs((ArrayList<Jobs>) job_roles);
                                                        company.setJob_status(job_status.toString());
                                                        company.setLocation(location.toString());
                                                        company.setOpen_positions(((Long) positions).intValue());
                                                        company.setMore_information(more_info.toString());*/

                                                        //userref.setValue(companySnapshot.getChildren());
                                                        //Map<String, Companies.Jobs> jobsMap = new Map<String, Companies.Jobs>();
                                                        assert company != null;
                                                        Log.i("job1", String.valueOf(company.getJob_roles().size()));
                                                        companies.add(company);
                                                        //job_roles.clear();
                                                    }

                                                    //Log.i("size", String.valueOf(companies.size()));
                                                    userref.setValue(companies);
                                        /*if(count[0]==companies.size())
                                        userref.setValue(companies);
                                        else{
                                            //companies.add(company1);
                                            userref.setValue(companies1);
                                        }*/
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                            Log.i("sparky",user.getEmailAddress());
                                            Toast.makeText(Password.this, "Success", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Password.this,InitialProfileActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }else{
                                    mProgress.dismiss();
                                    Toast.makeText(Password.this, "Not Registered", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                } else {

                    register.setEnabled(false);
                    Toast.makeText(Password.this, "button is not working ", Toast.LENGTH_SHORT).show();
                }
            }
        });





    }

    public void register(final String fullName,  String email,final String phone,final String gender,final String dob, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    assert firebaseUser != null;
                    String userUId = firebaseUser.getUid();
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userUId);
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("id",userUId);
                    hashMap.put("fullname",fullName);
                    //hashMap.put("email",email);
                    hashMap.put("phone",phone);
                    hashMap.put("dob",dob);
                    hashMap.put("gender",gender);
                    //hashMap.put("search",username.toLowerCase());

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(Password.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }else{
                    mProgress.dismiss();
                    Toast.makeText(Password.this, "Unable to imgUrl with this username and password"+fullName, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.mProgress.cancel();
    }
}
