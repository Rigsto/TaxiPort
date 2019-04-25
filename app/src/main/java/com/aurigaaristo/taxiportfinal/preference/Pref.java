package com.aurigaaristo.taxiportfinal.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
    private static final String PREFS = "app_user";

    private String nameKey = "nameKey";
    private String emailKey = "emailKey";
    private String passwordKey = "passKey";
    private String phoneKey = "phoneKey";
    private String langKey = "langKey";
    private String imageKey = "imageKey";
    private String statKey = "statKey";

    private Context context;
    private SharedPreferences sp;

    public Pref(Context context){
        this.context = context;
    }

    public void setNamePreference(String value){
        sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(nameKey, value);
        editor.apply();
    }

    public String getNamePreference(){
        sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sp.getString(nameKey, "");
    }

    public void setEmailPreference(String value){
        sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(emailKey, value);
        editor.apply();
    }

    public String getEmailPreference(){
        sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sp.getString(emailKey, "");
    }

    public void setPasswordPreference(String value){
        sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(passwordKey, value);
        editor.apply();
    }

    public String getPasswordPreference(){
        sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sp.getString(passwordKey, "");
    }

    public void setPhonePreference(String value){
        sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(phoneKey, value);
        editor.apply();
    }

    public String getPhonePreference(){
        sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sp.getString(phoneKey, "");
    }

    public void setLangPreference(int value){
        sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(langKey, value);
        editor.apply();
    }

    public int getLangPreference(){
        sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sp.getInt(langKey, 0);
    }

    public void setImagePreference(String value){
        sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(imageKey, value);
        editor.apply();
    }

    public String getImagePreference(){
        sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sp.getString(imageKey, "");
    }

    public void setStatPreference(int value){
        sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(statKey, value);
        editor.apply();
    }

    public int getStatPreference(){
        sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sp.getInt(statKey, 0);
    }

    public void resetPreference(){
        setNamePreference("");
        setPhonePreference("");
        setImagePreference("");
        setEmailPreference("");
        setLangPreference(0);
        setPasswordPreference("");
        setStatPreference(0);
    }
}
