package com.mj1666.providers3;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    public ViewPager viewPager;
    public TabLayout tabLayout;
    public TabItem elec, cook, plumber;
    public PageAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout)findViewById(R.id.Tablayout1);
        elec = (TabItem) findViewById(R.id.elec1);
        cook = (TabItem) findViewById(R.id.cook1);
        plumber = (TabItem)findViewById(R.id.plumber1);
        viewPager = (ViewPager) findViewById(R.id.pg);

        pagerAdapter = new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.pg, new electrician()).commit();}


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0){
                    pagerAdapter.notifyDataSetChanged();}
                else if(tab.getPosition()==1){
                    pagerAdapter.notifyDataSetChanged();}

                else if(tab.getPosition()==2){
                    pagerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}
