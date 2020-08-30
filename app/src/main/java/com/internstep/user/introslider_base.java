package com.internstep.user;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

class introslider_base extends AppCompatActivity {

    Button Skip;
    private Button Next;
    private ViewPager viewPager;
    private IntroPref introPref;
    private int[] layouts;
    MyViewPager myViewPager;
    private ViewPager.OnPageChangeListener onPageChangeListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introslider_base);

        if(!introPref.isFirstTimeLaunch()){
            launchHomeActivity();
            finish();
        }

        viewPager = findViewById(R.id.viewpager);
        Skip = findViewById(R.id.skipforNow);
        Next = findViewById(R.id.next);
        myViewPager = new MyViewPager();
        viewPager.setAdapter(myViewPager);
        viewPager.addOnPageChangeListener(onPageChangeListener);

        layouts = new int[]{
                R.layout.introslider_screen1,
                R.layout.introslider_screen2,
                R.layout.introslider_screen3,
                R.layout.introslider_screen4,
        };

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getitem(+1);
                if(current < layouts.length){
                    viewPager.setCurrentItem(current);
                }

                else{
                    launchHomeActivity();
                }
            }
        });

        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(introslider_base.this,StartActivity.class);
            }
        });

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == layouts.length -1){
                    Next.setText("Take Me In");
                }
                else{
                    Next.setText("Next");
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

    }

    public class MyViewPager extends PagerAdapter{

        LayoutInflater inflater;

        public MyViewPager(){};

        @Override
        public int getCount() {
            return layouts.length;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(layouts[position],container,false);
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View)object;
            container.removeView(view);
        }
    }

    private int getitem(int i){
        return viewPager.getCurrentItem() + 1;

    }
    private void launchHomeActivity() {
        introPref.setIsfirstTimeLaunch(false);
        startActivity(new Intent(introslider_base.this,StartActivity.class));
        finish();
    }
}
