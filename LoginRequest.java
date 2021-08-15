package com.finalproject3.amir.testgooglemaps;

/**
 * Created by amir on 4/7/2016.
 */
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by amir on 4/6/2016.
 */
public class LoginRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL = "http://amirzadok1234.esy.es/finalp/reg/login.php";
    private Map<String , String > params;

    public LoginRequest( String username , String password , Response.Listener<String> listener) {
        super(Method.POST , LOGIN_REQUEST_URL , listener , null);
        params = new HashMap<>();
        params.put("username",username);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
