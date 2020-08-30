package com.internstep.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.internstep.user.Fragments.ActivityFragment;
import com.internstep.user.Fragments.ProfileFragment;
import com.internstep.user.Fragments.SearchFragment;
import com.internstep.user.Models.User;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    //private BaseFragment _currentFragment;
    private final static String TAG ="SEARCH";
    private final static String TAG1 = "BEAT";
    private final static String TAG2 = "PROFILE";
    private static int next,history;
    BottomNavigationView bottomNav;
    String insta_link,dribble_link,linked_link;
    FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();
         bottomNav = findViewById(R.id.main_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setItemIconTintList(null);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if (b == null) {
            /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new SearchFragment()).commit();*/
            loadFragment(new SearchFragment(),TAG);
        }
        else{
            /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).commit();*/
            loadFragment(new ProfileFragment(),TAG2);
            bottomNav.setSelectedItemId(R.id.profile);
        }
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                insta_link = user.getInstagram();
                dribble_link = user.getDribble();
                linked_link = user.getLinkedin();



                //} catch (Exception e) {
                //  e.printStackTrace();

                // }
                //mProgress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //mProgress.dismiss();
            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;


                    switch (item.getItemId()){
                        case R.id.search:
                            /*if(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.beat)).getId()==R.id.beat)
                                history= R.id.beat;
                            else if(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.profile)).getId()==R.id.profile)
                                history= R.id.profile;*/
                            //String search;
                            selectedFragment = new SearchFragment();
                            //FragmentManager fm = getSupportFragmentManager();
                            loadFragment(selectedFragment,TAG);
                            break;
                        case R.id.beat:
                           /* next = R.id.beat;
                            if(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.search)).getId()==R.id.search){
                                history = R.id.search;
                            }
                            else {
                                history = R.id.profile;
                            }*/

                            //current =R.id.beat;
                            //history = R.id.search;
                            selectedFragment = new ActivityFragment();
                            loadFragment(selectedFragment,TAG1);
                            break;
                        case R.id.profile:
                            /*next = R.id.profile;
                            if(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.search)).getId()==R.id.search){
                                history = R.id.search;
                            }
                            else {
                                history = R.id.beat;
                            }*/

                            //current = R.id.profile;
                            //history = R.id.search;
                            selectedFragment = new ProfileFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("insta",insta_link);
                            bundle.putString("linkedin",linked_link);
                            bundle.putString("dribble",dribble_link);
                            selectedFragment.setArguments(bundle);
                            loadFragment(selectedFragment,TAG2);
                            break;

                    }


                    /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();*/

                    //history = Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.fragment_container)).getId();
                        //current = item.getItemId();
                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation,menu);
        return true;
    }

    @Override
    public void onBackPressed(){
        //BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_nav);
        int selectedItemId = bottomNav.getSelectedItemId();

        Fragment f = fm.findFragmentByTag("search");
        Fragment f1 = fm.findFragmentByTag(TAG2);
        String currentFragmentTag = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName();
        //fm.executePendingTransactions();

        assert f1 != null;
        if(fm.getBackStackEntryCount() > 0){
            fm.popBackStack();
            assert f1.getTag() != null;
            assert currentFragmentTag != null;
            Log.i("fragment",currentFragmentTag);
            if(currentFragmentTag.equals(TAG2)){
                bottomNav.setSelectedItemId(R.id.beat);
            }
            else if(currentFragmentTag.equals(TAG1)){
                bottomNav.setSelectedItemId(R.id.search);
            }
            else{
                bottomNav.setSelectedItemId(R.id.search);
            }

            //if(bottomNav.getSe)

        } else {
            bottomNav.setSelectedItemId(R.id.search);
            finish();
            super.onBackPressed();


        }




        /*(if(history ==R.id.search){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                   new SearchFragment()).commit();

        }else if(history==R.id.beat){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ActivityFragment()).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).commit();
        }*/

        //int count = getSupportFragmentManager().getBackStackEntryCount();
        /*FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }*/

            //super.onBackPressed();
            //additional code
            /*Intent intent = new Intent(MainActivity.this,InitialProfileActivity.class);
            startActivity(intent);
            finish();*/


    }

    private void loadFragment(Fragment fragment ,String fragment1){
         FragmentManager fm = MainActivity.this.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment,fragment1);
        if(!fragment1.equals(TAG)) {
            transaction.addToBackStack(fragment1);
        }
        transaction.commit();
        //fm.executePendingTransactions();
        //getSupportFragmentManager().executePendingTransactions();
        //transaction.executePending
        Log.i("stack", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));

    }


}
