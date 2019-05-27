package com.kmu.bangbang;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.kmu.bangbang.ValidateRequest;

import org.json.JSONObject;


public class RegisterActivity extends Activity {

    private ArrayAdapter adapter;
    private Spinner spinner;
    private String id;
    private String pw;
    private String ip;
    private String m_ip;
    private AlertDialog dialog;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);

        TextView backButton = (TextView)findViewById(R.id.backButton);

        //버튼이 눌리면 LoginActivity로 돌아감
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(loginIntent);
            }
        });


        //회원가입시 아이디가 사용가능한지 검증하는 부분
        final Button validateButton = (Button) findViewById(R.id.validateButton);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = idText.getText().toString();
                if (validate) {
                    return;//검증 완료
                }
                //ID 값을 입력하지 않았다면
                if (id.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("아이디를 입력해주세요.")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }


                //검증시작
                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();

                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {//사용할 수 있는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다.")
                                        .setNegativeButton("OK", null)
                                        .create();
                                dialog.show();
                                idText.setEnabled(false);//아이디값을 바꿀 수 없도록 함
                                validate = true;//검증완료
                                idText.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                validateButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                            } else {//사용할 수 없는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("이미 존재하는 아이디입니다.")
                                        .setNegativeButton("OK", null)
                                        .create();
                                dialog.show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };//Response.Listener 완료

                //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
                ValidateRequest validateRequest = new ValidateRequest(id, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);
            }
        });


        //회원 가입 버튼이 눌렸을때
        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final String id = idText.getText().toString();
                String pw = passwordText.getText().toString();
                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                String token = pref.getString("token", "");

                //ID 중복체크를 했는지 확인함
                if (!validate) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("아이디 중복확인을 해주세요.")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }

                //한칸이라도 빠뜨렸을 경우
                if (id.equals("") || pw.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("빈칸을 모두 채워주세요.")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }

                //회원가입 시작
                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {//사용할 수 있는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("등록에 성공하였습니다.")
                                        .setNegativeButton("OK", null)
                                        .create();
                                dialog.show();
                                Intent intent = new Intent(RegisterActivity.this,CameraActivity.class);
                                intent.putExtra("text",String.valueOf("User"));
                                intent.putExtra("value",String.valueOf("User"));
                                intent.putExtra("id",String.valueOf(id));
                                startActivity(intent);
                                //finish();//액티비티를 종료시킴(회원등록 창을 닫음)
                            } else {//사용할 수 없는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("등록에 실패하였습니다.")
                                        .setNegativeButton("OK", null)
                                        .create();
                                dialog.show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };//Response.Listener 완료

                //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
                RegisterRequest registerRequest = new RegisterRequest(id, pw, token, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);


            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}