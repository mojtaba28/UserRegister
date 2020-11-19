package com.example.register;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class ForgotPasswordActivity extends AppCompatActivity {

    TextInputEditText email;
    Button btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        init();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty()){
                    email.setError("enter yor email");

                }else if(!Utils.isValidEmailId(email.getText().toString())){
                    email.setError("enter a valid address");
                }else {
                    forgotPassword(email.getText().toString());
                }
            }
        });

    }
    public void init(){
        email=findViewById(R.id.edittext_email);
        btn_send=findViewById(R.id.btn_send);
    }

    public void forgotPassword(final String Email){
        // progressWheel.setVisibility(View.VISIBLE);
        String url= Utils.MAIN_URL+"forgot_password.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progressWheel.setVisibility(View.GONE);
                //                    JSONObject jsonObject=new JSONObject(response);
//                    String check=jsonObject.getString("status");
                if(response.contains("successful"))
                {
                    MDToast.makeText(getApplicationContext(),getResources().getString(R.string.email_sent), Toast.LENGTH_SHORT,MDToast.TYPE_SUCCESS).show();

                }
                else {
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
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }
}