package com.kmu.bangbang;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class EnrollFragment extends Fragment {
    static final String[] LIST_MENU = {"얼굴 등록하기"} ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_enroll, null) ;

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU) ;

        ListView listview = (ListView) view.findViewById(R.id.open_camera) ;
        listview.setAdapter(adapter) ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

//                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
//                if(permissionCheck== PackageManager.PERMISSION_DENIED){
//                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},0);
//                }else{
//                    Intent intent = new Intent(view.getContext(),CameraActivity.class);
                    Intent intent = new Intent(getActivity(),AddActivity.class);
                    startActivity(intent);
//                }
            }
        });

        return view ;
    }
}


