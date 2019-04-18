package com.kmu.bangbang;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


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

                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
                if(permissionCheck== PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},0);
                }else{

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,1);
                    Intent intent2 = new Intent( getActivity(),CameraActivity.class);
                    startActivity(intent2);
                }
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                startActivity(intent);


            }
        });

        return view ;
    }

    @Override //왜 안돌아가냐...
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode==0){
            if(grantResults[0]==0){
                Toast.makeText(getActivity(),"카메라 권한이 승인됨",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(),"카메라 권한이 거절 되었습니다. 카메라를 ㅇ용하려면 권한을 승낙하여야 합니다. ",Toast.LENGTH_SHORT).show();
            }
        }
    }

}


