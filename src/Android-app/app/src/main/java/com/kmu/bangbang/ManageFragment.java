package com.kmu.bangbang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ManageFragment extends Fragment {

    // 각 리스트 누르면 해당 게스트의 정보 페이지로 넘어갈 것
    static final String[] LIST_MENU = {"엄마(가족)","아빠(가족)", "엄정호(친구)", "황승애(친구)"} ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage, null) ;

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU) ;

        ListView listview = (ListView) view.findViewById(R.id.not_stranger_list) ;
        listview.setAdapter(adapter) ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), ManageSubActivity.class);
                startActivity(intent);
            }
        });

        return view ;
    }

}
