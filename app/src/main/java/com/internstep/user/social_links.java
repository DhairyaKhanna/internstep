package com.internstep.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class social_links extends AppCompatActivity {
    EditText dribble,instagram,linkedIn;
    Button submit,back;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_media_links);
        dribble = findViewById(R.id.dribble_text);
        instagram = findViewById(R.id.instagram_text);
        linkedIn = findViewById(R.id.linkedin_text);
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.toolbar_back_social);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(social_links.this, my_documents.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dribble_link = dribble.getText().toString();
                String insta_link = instagram.getText().toString();
                String linkedin_link = linkedIn.getText().toString();
                /*if(!dribble_link.isEmpty() && !dribble_link.startsWith("https://www")){

                }*/

                if(dribble_link.isEmpty() && insta_link.isEmpty() && linkedin_link.isEmpty()){
                    Toast.makeText(social_links.this,"Atleast write one social link",Toast.LENGTH_SHORT).show();
                }
                else if(!dribble_link.isEmpty() && (!dribble_link.startsWith("https://www") || !Patterns.WEB_URL.matcher(dribble_link).matches() || !dribble_link.contains("dribble"))){
                    //if( )
                    Toast.makeText(social_links.this,"Write the correct dribble link",Toast.LENGTH_SHORT).show();

                }
                else if(!insta_link.isEmpty() && (!insta_link.startsWith("https://www") || !Patterns.WEB_URL.matcher(insta_link).matches() || !insta_link.contains("instagram") )){
                    Toast.makeText(social_links.this,"Write the correct insta link",Toast.LENGTH_SHORT).show();

                }else if(!linkedin_link.isEmpty() && (!linkedin_link.startsWith("https://www") && !Patterns.WEB_URL.matcher(linkedin_link).matches() || !linkedin_link.contains("linkedin") )){
                    Toast.makeText(social_links.this,"Write the correct linkedin link",Toast.LENGTH_SHORT).show();
                }
                else {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    assert firebaseUser != null;
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                    Map<String, Object> hashmap = new HashMap<>();
                    if (!TextUtils.isEmpty(dribble_link))
                        hashmap.put("dribble", dribble_link);
                    if (!TextUtils.isEmpty(insta_link))
                        hashmap.put("instagram", insta_link);
                    if (!TextUtils.isEmpty(linkedin_link))
                        hashmap.put("linkedin", linkedin_link);
                    reference.updateChildren(hashmap).addOnSuccessListener(
                            new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(social_links.this, "Success", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );

                    Intent intent = new Intent(social_links.this, InitialProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }


            }
        });


    }

}
