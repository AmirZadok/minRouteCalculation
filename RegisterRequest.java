package com.finalproject3.amir.testgooglemaps;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by amir on 4/6/2016.
 */
public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://amirzadok1234.esy.es/finalp/reg/register.php";
    private Map<String , String > params;

    public RegisterRequest(String name , String username , String password , Response.Listener<String> listener) {
        super(Method.POST , REGISTER_REQUEST_URL , listener , null);
        params = new HashMap<>();
        params.put("name" , name);
        params.put("username" , username);
        params.put("password" , password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
