package com.internstep.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.internstep.user.Models.Companies;
import com.internstep.user.Models.Jobs;
import com.internstep.user.Models.User;
import com.internstep.user.R;

import java.io.ByteArrayOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ResumeActivity extends AppCompatActivity {
    ImageView image;
    Button submit,back;
    private Uri filePath;
    private Bitmap bitmap;
    private final int PICK_IMAGE_REQUEST = 22;
    final static int PICK_PDF_CODE = 2342;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseStorage storage;
    StorageReference storageReference;
    String full_name,company_name,job_role;
    ProgressDialog progressDialog;

    private StorageTask uploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart_application_2);

        image = findViewById(R.id.folder_sa);
        submit = findViewById(R.id.continue_sa);
        back = findViewById(R.id.toolbar_back_smart_application_2);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Intent iin= getIntent();

        Bundle b = iin.getExtras();
        if(b!=null)
        {
            company_name =(String) b.get("company_name");
            job_role = (String)b.get("job_position");


        }
        assert job_role != null;


        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User users = dataSnapshot.getValue(User.class);
                assert users != null;
                full_name =users.getName();
                /*for(DataSnapshot companySnapshot: dataSnapshot.child("companies").getChildren()){
                    Companies companies = companySnapshot.getValue(Companies.class);
                    assert companies != null;
                    //company_name = companies.getCompany_name();



                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 //uploadImage();
                if (filePath == null) {
                    Toast.makeText(ResumeActivity.this,"Please submit your resume",Toast.LENGTH_SHORT).show();

                }else {
                    Intent intent = new Intent(ResumeActivity.this, ApplyActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("company_name", company_name);
                    intent.putExtra("job_position", job_role);
                    startActivity(intent);
                    //finish();
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResumeActivity.this,SoftwareListActivity.class);
                intent.putExtra("company_name",company_name);
                intent.putExtra("job_position",job_role);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        //this.finish();
        //Intent intent = new Intent(Register.this, StartActivity.class);
        //intent.putExtra("user_data",user);
        //startActivity(intent);

        super.onBackPressed();
        finish();
    }



    // Select Image method
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select PDF from here..."),
                PICK_PDF_CODE);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_PDF_CODE
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {


            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            // Get the Uri of data
            try {
                filePath = data.getData();
                uploadImage();
                progressDialog.dismiss();

            }
            catch (IOError error) {
                // Log the exception
                error.printStackTrace();
                progressDialog.dismiss();
            }
            /*try {

                // Setting image on image view using Bitmap
                /*bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);*/
                //imageView.setImageBitmap(bitmap);*/
            //}

           /* catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }*/
        }
    }

    // UploadImage method
    private void uploadImage() {
        final String[] muri = new String[1];
        if (filePath != null) {
            //DataSnapshot dataSnapshot = null;
            //User user1 = dataSnapshot.getValue(User.class);
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            //firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            // Code for showing progressDialog while uploading





            // Defining the child of storageReference
            final StorageReference ref
                    = storageReference
                    .child(
                            "resume/").child(full_name).child(company_name).child(job_role).child(uid + ".pdf");

            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //progressDialog.dismiss();
                            muri[0] = uri.toString();
                            //muri[0] = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getMetadata()).getReference()).getDownloadUrl().toString();
                            Log.i("url",muri[0]);
                            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
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
                                                if(jobs.getJob_name().equals(job_role)) {


                                                    //String str = "Yes";
                                                    //Map<String,Object> hashMap= new HashMap<>();
                                                    //hashMap.put("software",soft_list);
                                                    if(!muri[0].isEmpty())
                                                        //reference.child("").updateChildren(hashMap);
                                                        jobSnapshot.getRef().child("resume").setValue(muri[0]);


                                                }
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    });

                }



            });

            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);

            // adding listeners on upload
            // or failure of image


        }



    }

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( progressDialog!=null && progressDialog.isShowing())
        {
            progressDialog.cancel();
        }
    }*/






}
