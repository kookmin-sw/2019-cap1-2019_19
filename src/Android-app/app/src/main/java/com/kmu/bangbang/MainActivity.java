package com.kmu.bangbang;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    Fragment selectedFragment = new HomeFragment();
    Toolbar toolbar;
    ImageButton setting_button;
    ImageButton logout_button;
    BottomNavigationView navigation;
    String notice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setSelectedItemId(R.id.navigation_home);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        navigation.setOnNavigationItemSelectedListener(BottomNavigationSelectedListener);

        SharedPreferences pref = getSharedPreferences("alarm", MODE_PRIVATE);
        notice = pref.getString("alarm", "off");

        if(notice.equals("on")){

            SharedPreferences.Editor editor = pref.edit();
            //editor.clear();
            editor.putString("alarm", "off");
            editor.commit();

            selectedFragment = new StreamingFragment();
            navigation.setSelectedItemId(R.id.navigation_streaming);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }

        logout_button = (ImageButton) findViewById(R.id.logout);
        logout_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();

                editor.clear();
                editor.commit();
                Toast.makeText(MainActivity.this, "자동 로그인 해제", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

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

        FirebaseMessaging.getInstance().subscribeToTopic("new");
        FirebaseInstanceId.getInstance().getToken();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("정말로 종료하시겠습니까?");
        builder.setTitle("종료 알림창")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("종료 알림창");
        alert.show();

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
                case R.id.navigation_acquaintance:
                    selectedFragment = new AcquaintanceFragment();
                    break;
                case R.id.navigation_record:
                    selectedFragment = new RecordCategoryFragment();
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
