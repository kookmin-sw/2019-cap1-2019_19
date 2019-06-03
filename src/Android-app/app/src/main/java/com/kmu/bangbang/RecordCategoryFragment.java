package com.kmu.bangbang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class RecordCategoryFragment extends Fragment {
    ArrayList<String> categorys= new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_category, null) ;

        categorys.add("전체보기");
        categorys.add("가족");
        categorys.add("친구");
        categorys.add("외부인");

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, categorys) ;

        ListView listview = (ListView) view.findViewById(R.id.record_list) ;
        listview.setAdapter(adapter) ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), RecordActivity.class);
                String selected_category = categorys.get(position);
                // 선택된 카테고리 값 전달
                intent.putExtra("category", selected_category);
                Log.v("Selected Category : ", categorys.get(position));
                startActivity(intent);
            }
        });
        return view ;
    }


}
