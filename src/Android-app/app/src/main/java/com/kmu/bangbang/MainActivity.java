package com.kmu.bangbang;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    Fragment selectedFragment = new HomeFragment();
    Toolbar toolbar;
    ImageButton setting_button;
    BottomNavigationView navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(BottomNavigationSelectedListener);

        setting_button = (ImageButton) findViewById(R.id.setting);
        setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigation.setSelected(false);

                navigation.invalidate();

                selectedFragment = new SettingFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
        });


    }

    private BottomNavigationView.OnNavigationItemSelectedListener BottomNavigationSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();

                    break;
                case R.id.navigation_enroll:
                    selectedFragment = new EnrollFragment();
                    break;
                case R.id.navigation_manage:
                    selectedFragment = new ManageFragment();
                    break;
                case R.id.navigation_record:
                    selectedFragment = new RecordFragment();
                    break;
                case R.id.navigation_streaming:
                    selectedFragment = new StreamingFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

}
