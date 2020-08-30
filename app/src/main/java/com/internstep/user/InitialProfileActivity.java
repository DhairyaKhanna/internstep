package com.internstep.user;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.internstep.user.Models.User;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class InitialProfileActivity extends AppCompatActivity {
    String uid;
    StorageReference ref;
    ImageView add;
    Button about_me, my_documents, social_links, submit;
    private CircleImageView profile_picture;
    private static final int picture = 1;
    Uri imageuri,uri;
    private final int PICK_IMAGE_REQUEST = 22;
    String imgurl,short_description,education,job_title,insta,dribble,linkedin,imgurl_string,id_card;
    private StorageTask uploadTask;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    String full_name;
    TextView profile_name;
    ProgressDialog progressDialog;
    private String pictureFilePath;
    private Bitmap bitmap;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    int x =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_details);
        about_me = findViewById(R.id.about_me);
        my_documents = findViewById(R.id.my_documents);
        social_links = findViewById(R.id.my_social_links);
        submit = findViewById(R.id.done);
        add = findViewById(R.id.add);
        profile_picture = findViewById(R.id.account_picture);
        profile_name = findViewById(R.id.profile_name);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                //user.setImgUrl(imgurl);
                full_name = user.getName();
                short_description = user.getShort_description();
                education = user.getEducation();
                job_title = user.getJob_title();
                insta = user.getInstagram();
                dribble = user.getDribble();
                linkedin = user.getLinkedin();
                id_card = user.getIdcard();
                imgurl_string = user.getImgUrl();
                profile_name.setText(full_name);
                if(education.equals("default") || job_title.equals("default") || short_description.equals("default")  || (insta.equals("default") && linkedin.equals("default")
                        && dribble.equals("default")) || id_card.equals("default")  ){
                    //x =1;

                }
                //if()

                if(imgurl_string.equals("default")){
                    profile_picture.setImageResource(R.mipmap.ic_launcher);
                }
                else{
                    add.setVisibility(View.INVISIBLE);
                    Glide.with(getApplicationContext()).load(imgurl_string).into(profile_picture);
                }


                //} catch (Exception e) {
                //  e.printStackTrace();

                // }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        about_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InitialProfileActivity.this, about_me.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();


            }


        });

        my_documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InitialProfileActivity.this, my_documents.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();


            }


        });

        social_links.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InitialProfileActivity.this, social_links.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();


            }


        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        //user.setImgUrl(imgurl);
                        full_name = user.getName();
                        short_description = user.getShort_description();
                        education = user.getEducation();
                        job_title = user.getJob_title();
                        insta = user.getInstagram();
                        dribble = user.getDribble();
                        linkedin = user.getLinkedin();
                        id_card = user.getIdcard();
                        imgurl_string = user.getImgUrl();
                        profile_name.setText(full_name);
                        if(education.equals("default") || job_title.equals("default") || short_description.equals("default")  ||   id_card.equals("default")  || (insta.equals("default") && linkedin.equals("default")
                                && dribble.equals("default")) ){
                            x=1;
                            /*if((insta.equals("default") && linkedin.equals("default")
                                    && dribble.equals("default")))*/
                            Toast.makeText(InitialProfileActivity.this, "All credentials are required!", Toast.LENGTH_SHORT).show();

                        }

                        else {
                            Intent intent = new Intent(InitialProfileActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        Log.i("value",String.valueOf(x));
                        //if()

                        /*if(imgurl_string.equals("default")){
                            profile_picture.setImageResource(R.mipmap.ic_launcher);
                        }
                        else{
                            add.setVisibility(View.INVISIBLE);
                            Glide.with(getApplicationContext()).load(imgurl_string).into(profile_picture);
                        }*/


                        //} catch (Exception e) {
                        //  e.printStackTrace();

                        // }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





            }


        });


        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //selectedImage(InitialProfileActivity.this);
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(InitialProfileActivity.this);

                //CropImage.startPickImageActivity(InitialProfileActivity.this);
            }
        });





        /*portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Intent intent = new Intent(InitialProfileActivity.this, portfolio.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();


            }


        });*/


    }

    /*private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else {
            dispatchTakePictureIntent();
        }

    }*/

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //dispatchTakePictureIntent();
                Intent takePicture = null;
                takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);

            }else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }*/
    /*private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
               // ".jpg",         /* suffix */
               // storageDir      /* directory */
      //  );

        // Save a file: path for use with ACTION_VIEW intents
        //currentPhotoPath = image.getAbsolutePath();
        //return image;
    //}*/

    /*private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.internstep.user.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }*/


    private void selectedImage(Context context) {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        /*if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
        ) {
            imageuri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON).
                    setAspectRatio(1, 1)
                    .start(this);
        }*/CropImage.ActivityResult result=null;
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
             result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                imageuri = result.getUri();


                try {

                    // Setting image on image view using Bitmap
                    bitmap = MediaStore
                            .Images
                            .Media
                            .getBitmap(
                                    getContentResolver(),
                                    imageuri);
                    // handleUpload(bitmap);
                    profile_picture.setImageBitmap(bitmap);

                    handleUpload(bitmap);

                } catch (IOException e) {
                    // Log the exception
                    e.printStackTrace();
                }
            }
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Exception error = result.getError();
        }
    }





    private void startCrop(Uri imageuri){
        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    private void handleUpload(Bitmap bitmap) {
        int currSize;
        if (imageuri != null) {
             progressDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
            progressDialog.setTitle("Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
            currSize = baos.toByteArray().length;
            Log.i("size",String.valueOf(currSize));
            final StorageReference ref = FirebaseStorage.getInstance().getReference()
                    .child(
                            "profile_images/").child(full_name).child(uid + ".jpeg");
            uploadTask = ref.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //progressDialog.dismiss();
                    imgurl = ref.getDownloadUrl().toString();
                }
            });

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        //progressDialog.dismiss();
                        Uri downloadUri = task.getResult();
                        String muri = downloadUri.toString();

                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        Map<String, Object> hashmap = new HashMap<>();
                        hashmap.put("imgUrl", muri);
                        reference.updateChildren(hashmap).addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(InitialProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    }

                                });

                    } else {
                        // Handle failures
                        // ...
                        progressDialog.dismiss();
                        Toast.makeText(InitialProfileActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });



                /*reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user1 = dataSnapshot.getValue(User.class);
                        user1.setImgUrl(imgurl);
                        //} catch (Exception e) {
                        //  e.printStackTrace();

                        // }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/


        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(progressDialog!=null  && progressDialog.isShowing())
        this.progressDialog.dismiss();
    }

}


