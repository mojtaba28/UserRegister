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

public class SignupActivity extends AppCompatActivity {

    TextView tv_login;
    TextInputEditText email,password,confirmPassword,name;
    Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
    }

    public void init(){
        email=findViewById(R.id.edittext_email);
        password=findViewById(R.id.edittext_password);
        confirmPassword=findViewById(R.id.edittext_confirmPassword);
        name=findViewById(R.id.edittext_name);
        tv_login=findViewById(R.id.tv_login);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

        btn_register=findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty()){
                    email.setError("enter your email");

                }else if (!Utils.isValidEmailId(email.getText().toString())){
                    email.setError("email is not valid");

                }else if (password.getText().toString().isEmpty()){
                    password.setError("enter your password");
                }else if (confirmPassword.getText().toString().isEmpty()){

                    confirmPassword.setError("confirm your password");
                }else if (!password.getText().toString().equals(confirmPassword.getText().toString())){
                    MDToast.makeText(getApplicationContext(),"your passwords are not equal!", Toast.LENGTH_LONG,MDToast.TYPE_ERROR).show();
                }else if (name.getText().toString().isEmpty()){
                    name.setError("enter your name");
                }else {

                    Signup(email.getText().toString(),password.getText().toString(),name.getText().toString());
                }
            }
        });
    }

    public void Signup(final String Email, final String pass,final String name){
        // progressWheel.setVisibility(View.VISIBLE);
        String url= Utils.MAIN_URL+"sign_up.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progressWheel.setVisibility(View.GONE);
                //                    JSONObject jsonObject=new JSONObject(response);
//                    String check=jsonObject.getString("status");
                if(response.contains("successful"))
                {
                    MDToast.makeText(getApplicationContext(),getResources().getString(R.string.successful_register), Toast.LENGTH_SHORT,MDToast.TYPE_SUCCESS).show();


                }else if (response.contains("already"))
                {
                    MDToast.makeText(getApplicationContext(),getResources().getString(R.string.user_already_exist),Toast.LENGTH_SHORT,MDToast.TYPE_WARNING).show();
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
                hashMap.put("name",name);
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }
}