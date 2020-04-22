package com.example.doctorsahab;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";
    String SESSION_USER_EMAIL = "session_email";


    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public void saveSession(User user){
        //save session of user whenever user is logged in
        String id = user.getId();
        String email = user.getEmail();
        editor.putString(SESSION_KEY,id).commit();
        editor.putString(SESSION_USER_EMAIL,email).commit();
    }

    public String getSession(){
        //return user id whose session is saved
        return sharedPreferences.getString(SESSION_KEY, "NOTLOGGEDIN" );
    }

    public void removeSession(){
        editor.putString(SESSION_KEY, "NOTLOGGEDIN").commit();
    }
}