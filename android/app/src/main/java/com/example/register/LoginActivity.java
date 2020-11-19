package com.example.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextView tv_register,tv_forgot_password;
    TextInputEditText email,password;
    Button btn_login;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty()){
                    email.setError("enter your email");
                }else if(Utils.isValidEmailId(email.getText().toString())==false){
                    email.setError("email is not valid");
                }else if (password.getText().toString().isEmpty()){
                    password.setError("enter your password");
                }else {
                    Login(email.getText().toString(),password.getText().toString());
                }
            }
        });



    }

    public void init(){
        email=findViewById(R.id.edittext_email);
        password=findViewById(R.id.edittext_password);
        tv_register=findViewById(R.id.tv_register);
        btn_login=findViewById(R.id.btn_login);
        tv_forgot_password=findViewById(R.id.tv_forgot_password);
        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ForgotPasswordActivity.class));
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
            }
        });
    }

    public void Login(final String Email, final String pass){
        // progressWheel.setVisibility(View.VISIBLE);
        String url= Utils.MAIN_URL+"login.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progressWheel.setVisibility(View.GONE);
                //                    JSONObject jsonObject=new JSONObject(response);
//                    String check=jsonObject.getString("status");
                if(response.contains("successful"))
                {
                    MDToast.makeText(getApplicationContext(),getResources().getString(R.string.successful), Toast.LENGTH_SHORT,MDToast.TYPE_SUCCESS).show();

                        sessionManager=new SessionManager(getApplicationContext());
                        sessionManager.setLoggedIn(true);
                        sessionManager.setUserInfo(Email,pass);
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();


                }else if (response.contains("failed"))
                {
                    MDToast.makeText(getApplicationContext(),getResources().getString(R.string.username_or_password_wrong),Toast.LENGTH_SHORT,MDToast.TYPE_WARNING).show();
                }else {
                    MDToast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT,MDToast.TYPE_ERROR).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressWheel.setVisibility(View.GONE);
                MDToast.makeText(getApplicationContext(),error+"",Toast.LENGTH_SHORT,MDToast.TYPE_ERROR).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("email",Email);
                hashMap.put("password",pass);
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }
}