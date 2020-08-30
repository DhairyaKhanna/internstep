package com.internstep.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
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
import com.internstep.user.Models.User;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class my_documents extends AppCompatActivity {
    ImageView image;
    Button submit,back;
    private Uri filePath,documentUri;
    private String pictureFilePath;
    private Bitmap bitmap;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    String full_name,imgurl,imgurl_string;
    private Uri mCurrentPhotoPath;
    FirebaseStorage storage;
    StorageReference storageReference;
    private StorageTask uploadTask;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    ProgressDialog progressDialog;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_documents);
        image = findViewById(R.id.folder);
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.toolbar_back_mydocs);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(my_documents.this, about_me.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User users = dataSnapshot.getValue(User.class);
                //assert users != null;
                full_name =users.getName();
                imgurl = users.getIdcard();
                if(!imgurl.equals("default")){

                        Glide.with(getApplicationContext()).load(imgurl).into(image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //selectedImage(my_documents.this);
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(my_documents.this);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User users = dataSnapshot.getValue(User.class);
                        //assert users != null;
                        full_name =users.getName();
                        imgurl = users.getIdcard();
                        if(imgurl.equals("default")){
                            Toast.makeText(my_documents.this,"Please upload the document",Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Intent intent = new Intent(my_documents.this, social_links.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            //finish();
                        }
                        /*if(!imgurl.equals("default")){

                            Glide.with(getApplicationContext()).load(imgurl).into(image);
                        }*/
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //selectedImage(my_documents.this);



            }
        });



    }



    // Select Image method


        private void selectedImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    Intent takePicture = null;
                    if(ContextCompat.checkSelfPermission(my_documents.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(my_documents.this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
                    }else {
                        //dialog.dismiss();
                        takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        takePicture.putExtra( MediaStore.EXTRA_FINISH_ON_COMPLETION, true);

                            // Create the File where the photo should go
                            //File photoFile = null;

                                //photoFile = createImageFile();
                                //photoFile =  new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

                            // Continue only if the File was successfully created
                                //filePath = Uri.fromFile(photoFile);
                                //takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));

                        if (takePicture.resolveActivity(getPackageManager()) != null) {


                            File pictureFile = null;
                            try{
                                pictureFile = getPictureFile();
                            } catch (IOException ex) {
                                Toast.makeText(my_documents.this, "Photo file can't be created, please try again", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (pictureFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(my_documents.this,
                                        BuildConfig.APPLICATION_ID + ".fileprovider",
                                        pictureFile);
                                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(takePicture, 0);
                            }
                        }



                    }



                    //dialog.dismiss();

                } else if (options[item].equals("Choose from Gallery")) {
                    dialog.dismiss();
                    Intent gallery = new Intent();
                    gallery.setType("image/*");
                    gallery.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(gallery, "Select Picture"),1);
                    //Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //startActivityForResult(pickPhoto , 1);
                    //dialog.dismiss();

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        alertDialog = builder.create();
            if(my_documents.this!= null && !my_documents.this.isFinishing())
            alertDialog.show();




        //finish();

    }

    private File getPictureFile() throws IOException {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "INTERNSTEP_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile,  ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }


   /* @Override
    public void onDestroy(){
        super.onDestroy();
        if(alertDialog!=null && alertDialog.isShowing())
            alertDialog.dismiss();


    }*/

    /*private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );
        Log.i("file",String.valueOf(filePath));
        // Save a file: path for use with ACTION_VIEW intents
        filePath = Uri.parse("file:" + image.getAbsolutePath());

        return image;
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + "Download");
        if (!wallpaperDirectory.exists()) {  // have the object build the directory structure, if needed.
            wallpaperDirectory.mkdirs();
        }



        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }*/


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

                    CropImage.ActivityResult result=null;
                   if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                       result = CropImage.getActivityResult(data);
                       if (resultCode == RESULT_OK && data != null) {
                           //imageuri = data.getData();
                           documentUri = result.getUri();
                           Bitmap selectedImage = null;
                           //Uri contentURI = data.getData();
                           //Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        /*try {
                            selectedImage= MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                           ;
                           //filePath = data.getExtras().get("data");
                        /*try {
                            selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/

                           //File imgFile = new  File(pictureFilePath);

                           //filePath = Uri.parse(saveImage(selectedImage));
                        /*if(imgFile.exists())            {
                            filePath = Uri.fromFile(imgFile);
                            image.setImageURI(Uri.fromFile(imgFile));
                        }
                        selectedImage = BitmapFactory.decodeFile(String.valueOf(imgFile));*/
                           //imgFile.
                           //selectedImage.
                           //image.setImageBitmap(selectedImage);

                           try {

                               // Setting image on image view using Bitmap
                               selectedImage = MediaStore
                                       .Images
                                       .Media
                                       .getBitmap(
                                               getContentResolver(),
                                               documentUri );
                               // handleUpload(bitmap);



                           } catch (IOException e) {
                               // Log the exception
                               e.printStackTrace();
                           }


                           assert selectedImage != null;
                           image.setImageBitmap(selectedImage);
                           uploadImage(selectedImage);

                       }
                   }



                   /* if (resultCode == RESULT_OK && data != null) {
                        //Uri selectedImage = data.getData();
                        //String[] filePathColumn = {MediaStore.Images.Media.DATA};


                        Bitmap bitmap = null;
                        filePath = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        image.setImageBitmap(bitmap);
                        assert bitmap != null;
                        uploadImage(bitmap);



                        /*if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                profile_picture.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }*/

                   // }

            }



    // UploadImage method
    private void uploadImage(Bitmap bitmap)
    {
        if (documentUri != null) {
            progressDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
            progressDialog.setTitle("Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
            //DataSnapshot dataSnapshot = null;
            //User user1 = dataSnapshot.getValue(User.class);
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            //firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            // Code for showing progressDialog while uploading
            /*final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();*/

            // Defining the child of storageReference
            final StorageReference ref
                    = storageReference
                    .child(
                            "images/").child(full_name).child(uid +".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,75,baos);

            // adding listeners on upload
            // or failure of image
            uploadTask = ref.putBytes(baos.toByteArray())
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    imgurl_string =  ref.getDownloadUrl().toString();
                                    Log.d("imgurl",imgurl_string);

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

                        Uri downloadUri = task.getResult();
                        String muri = downloadUri.toString();

                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        Map<String, Object> hashmap = new HashMap<>();
                        hashmap.put("idcard", muri);
                        reference.updateChildren(hashmap).addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(my_documents.this, "Success", Toast.LENGTH_SHORT).show();
                                    }

                                });

                    } else {
                        // Handle failures
                        // ...
                        progressDialog.dismiss();
                        Toast.makeText(my_documents.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(progressDialog!=null  && progressDialog.isShowing())
            this.progressDialog.dismiss();
    }



}
