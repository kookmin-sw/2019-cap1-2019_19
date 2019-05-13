package com.kmu.bangbang;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    Button intent_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        final EditText editText = (EditText)findViewById(R.id.edittext);
        intent_btn = findViewById(R.id.button);

        intent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddActivity.this,CameraActivity.class);
                intent.putExtra("text",String.valueOf(editText.getText()));
                intent.putExtra("value",String.valueOf("Other"));
                startActivity(intent);
            }
        });

    }

}
