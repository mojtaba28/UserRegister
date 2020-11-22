package com.example.register;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextView tv_register,tv_forgot_password;
    TextInputEditText email,password;
    Button btn_login;
    SessionManager sessionManager;
    SignInButton signInButton;
    GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 12;

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

        //sign in with google account

        GoogleSignInOptions googleSignInOptions=
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient= GoogleSignIn.getClient(this,googleSignInOptions);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });



    }


    public void init(){
        email=findViewById(R.id.edittext_email);
        password=findViewById(R.id.edittext_password);
        tv_register=findViewById(R.id.tv_register);
        btn_login=findViewById(R.id.btn_login);
        signInButton=findViewById(R.id.btn_google_login);
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

    private void googleSignIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
            sessionManager=new SessionManager(getApplicationContext());
            sessionManager.setLoggedIn(true);
            sessionManager.setUserInfo(account.getEmail(),"");
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("signInResult", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(getApplicationContext(), e.getStatusCode(), Toast.LENGTH_LONG).show();
            //updateUI(null);
        }
    }
}