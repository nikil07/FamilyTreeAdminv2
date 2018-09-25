package com.androidworks.familytreeadmin.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.androidworks.familytreeadmin.data.model.Member;
import com.androidworks.familytreeadmin.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Nikhil on 06-Jul-18.
 */

public class DataStore {

    static Gson gson = new Gson();
    static ArrayList<Member> members;
    private static DataStore instance;
    private static SharedPreferences sharedPreferences;

    private DataStore() {
        // private constructor to enforce singleton
    }

    public static DataStore getInstance(Context context) {
        if (instance == null) {
            instance = new DataStore();
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            members = new ArrayList<>();
        }
        return instance;
    }

    /**
     * to store location logs
     *
     * @param member
     */
    public void storeMembers(Member member) {

        members = gson.fromJson(sharedPreferences.getString(Constants.SHARED_PREF_MEMBERS, members.toString()), new TypeToken<ArrayList<Member>>() {
        }.getType());
        members.add(member);
        sharedPreferences.edit().putString(Constants.SHARED_PREF_MEMBERS, gson.toJson(members)).apply();
    }

    /**
     * to store location logs
     *
     * @param member
     */
    public void storeMembers(Member member, int position) {

        members = gson.fromJson(sharedPreferences.getString(Constants.SHARED_PREF_MEMBERS, members.toString()), new TypeToken<ArrayList<Member>>() {
        }.getType());
        members.add(position,member);
        sharedPreferences.edit().putString(Constants.SHARED_PREF_MEMBERS, gson.toJson(members)).apply();
    }

    /**
     * to get location logs
     *
     * @return
     */
    public String getMembersJSON() {
        return sharedPreferences.getString(Constants.SHARED_PREF_MEMBERS, members.toString());
    }

    /**
     * to get location logs
     *
     * @return
     */
    public void setMembersJSON(String JSON) {
        sharedPreferences.edit().putString(Constants.SHARED_PREF_MEMBERS, JSON).apply();
    }

}
