package ru.romanbrazhnikov.userformsender.application.model;

import java.io.Serializable;

/**
 * Created by roman on 20.10.17.
 */

public class UserForm implements Serializable {
    private String mEmail;
    private String mPhone;
    private String mPassword;
    private String mPicturePath;



    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getPicturePath() {
        return mPicturePath;
    }

    public void setPicturePath(String picturePath) {
        mPicturePath = picturePath;
    }

}
