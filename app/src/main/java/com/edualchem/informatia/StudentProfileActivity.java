package com.edualchem.informatia;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.edualchem.informatia.Adapters.ViewPagerAdapter;
import com.edualchem.informatia.Fragments.AnalysisFragment;
import com.edualchem.informatia.Fragments.MarkFragment;
import com.edualchem.informatia.Fragments.ProfileFragment;
import com.edualchem.informatia.R;
import java.util.ArrayList;
import java.util.List;



public class StudentProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Profile");
        //setSupportActionBar(toolbar);

        List<Fragment> fragmentList = new ArrayList<>();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        MarkFragment fragment =  MarkFragment.newInstance(getIntent().getStringExtra("no"),getIntent().getStringExtra("name"),getIntent().getStringExtra("class"),getIntent().getStringExtra("div"));
        adapter.addFragment(fragment,"Marks");
        adapter.addFragment(ProfileFragment.newInstance(getApplicationContext()),"Badges");
        adapter.addFragment(AnalysisFragment.newInstance(getIntent().getStringExtra("no"),getIntent().getStringExtra("name"),getIntent().getStringExtra("class"),getIntent().getStringExtra("div")),"Analysis");
        //fragmentList.add(fragment);
        //fragmentList.add(ProfileFragment.newInstance(getApplicationContext()));
        //fragmentList.add(new AnalysisFragment());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


}
