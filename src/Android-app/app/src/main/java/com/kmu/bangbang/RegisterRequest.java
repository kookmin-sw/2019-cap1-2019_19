package com.kmu.bangbang;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class RegisterRequest extends StringRequest{
    final static private String URL = "http://52.78.219.61/UserRegister.php";
    private Map<String, String> parameters;

    public RegisterRequest(String id, String pw, String token, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("pw", pw);
        parameters.put("token", token);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
