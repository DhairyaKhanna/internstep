package com.internstep.user;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class signed_in extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(myToolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    // ...
}
