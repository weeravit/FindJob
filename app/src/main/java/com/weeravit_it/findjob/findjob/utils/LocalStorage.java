package com.weeravit_it.findjob.findjob.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Member;

/**
 * Created by Weeravit on 2/9/2558.
 */
public class LocalStorage {

    private static LocalStorage localStorage;

    private final String PACKAGE_NAME = Contextor.getInstance().getMainContext().getPackageName();
    private final String MEMBER_NAME = "Member";
    private SharedPreferences sharedPreferences;

    private Member member;

    private LocalStorage() {
        sharedPreferences = Contextor.getInstance().getMainContext().getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
    }

    public static LocalStorage getInstance() {
        if (localStorage == null)
            localStorage = new LocalStorage();
        return localStorage;
    }

    public Member getMember() {
        if (member == null) {
            String json = sharedPreferences.getString(MEMBER_NAME, null);
            member = GsonManager.getInstance().getGson().fromJson(json, Member.class);
        }
        return member;
    }

    public void setMember(Member member) {
        String json = GsonManager.getInstance().getGson().toJson(member);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MEMBER_NAME, json);
        editor.commit();
        this.member = member;
    }

    public void clear() {
        sharedPreferences.edit().remove(MEMBER_NAME).commit();
        member = null;
    }

}
