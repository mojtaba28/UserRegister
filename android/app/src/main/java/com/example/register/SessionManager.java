package com.example.register;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    Context context;
    SharedPreferences sharedPreferences;
    Boolean loggedIn=false;

    public SessionManager(Context context){
        this.context=context;
        sharedPreferences=context.getSharedPreferences("register",Context.MODE_PRIVATE);
    }

    public Boolean getLoggedIn(){
        return sharedPreferences.getBoolean("login",false);
    }

    public void setLoggedIn(Boolean loggedIn){
        sharedPreferences.edit().putBoolean("login",loggedIn).apply();
    }

    public void setUserInfo(String email,String password){
        sharedPreferences.edit().putString("password",password).apply();
        sharedPreferences.edit().putString("email",email).apply();

    }

    public String getUserEmail(){
        return sharedPreferences.getString("email","");
    }

    public String getUserPassword(){
        return sharedPreferences.getString("password","");
    }

}
