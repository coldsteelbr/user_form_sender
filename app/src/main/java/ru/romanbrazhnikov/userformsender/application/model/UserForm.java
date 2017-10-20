package ru.romanbrazhnikov.userformsender.application.model;

/**
 * Created by roman on 20.10.17.
 */

public class UserForm {
    private String mEmail;
    private String mPhone;
    private String mPassword;

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
}
