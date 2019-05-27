package com.kmu.bangbang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class SettingFragment extends Fragment {
    private ListView listView;
    static final String[] settings = {"로그인 정보","기기 등록"} ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, null) ;

        listView = (ListView) view.findViewById(R.id.listView_setting);
        listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, settings));
        listView.setOnItemClickListener(onitemClickListener);

        return view;
    }


    private AdapterView.OnItemClickListener onitemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position == 0){
                Intent intent = new Intent(getActivity(), LoginInfoActivity.class);
                startActivity(intent);
            }
            if(position == 1){
                Intent intent = new Intent(getActivity(), ConnectActivity.class);
                startActivity(intent);
            }
        }
    };
}
