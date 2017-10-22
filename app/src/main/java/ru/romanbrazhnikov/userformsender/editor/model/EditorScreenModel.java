package ru.romanbrazhnikov.userformsender.editor.model;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.romanbrazhnikov.userformsender.R;
import ru.romanbrazhnikov.userformsender.application.model.UserForm;
import ru.romanbrazhnikov.userformsender.editor.view.UserFormEditorActivity;
import ru.romanbrazhnikov.userformsender.utils.ValidationUtils;

/**
 * Created by roman on 22.10.17.
 */

public class EditorScreenModel implements Serializable {

    // CONSTANTS
    private static final String STATE_EMAIL = "STATE_EMAIL";
    private static final String STATE_PHONE = "STATE_PHONE";
    private static final String STATE_PASSWORD = "STATE_PASSWORD";
    private static final String STATE_MODEL = "STATE_MODEL";

    // WIDGETS
    @BindView(R.id.img_take_picture)
    SimpleDraweeView imgTakePicture;

    @BindView(R.id.img_picture_holder)
    SimpleDraweeView imgPictureHolder;

    @BindView(R.id.et_email)
    EditText etEmail;

    @BindView(R.id.et_phone)
    EditText etPhone;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.til_email)
    TextInputLayout tilEmail;

    @BindView(R.id.til_phone)
    TextInputLayout tilPhone;

    @BindView(R.id.til_password)
    TextInputLayout tilPassword;

    UserForm mUserForm = new UserForm();

    private LinearLayout ll_root;
    private Context mContext;
    private boolean hasPicture = false;

    public EditorScreenModel(LinearLayout root, Context context) {
        mContext = context;
        ll_root = root;

        ButterKnife.bind(this, ll_root);

        etEmail.setOnFocusChangeListener(new EmailFocusChangeListener());
        etPhone.setOnFocusChangeListener(new PhoneFocusChangeListener());
        // TODO: set custom listener
        etPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        etPassword.setOnFocusChangeListener(new PasswordFocusChangeListener());
    }

    public void setTakePictureListener(UserFormEditorActivity.TakePictureListener listener) {
        imgTakePicture.setOnClickListener(listener);
        imgPictureHolder.setOnClickListener(listener);
    }


    public void setFile(File photoFile) {
        mUserForm.setFile(photoFile);
        updateImgHolder();
    }

    public void saveState(Bundle outState) {
        outState.putSerializable(STATE_MODEL, mUserForm);
        outState.putString(STATE_EMAIL, etEmail.getText().toString());
        outState.putString(STATE_PHONE, etPhone.getText().toString());
        outState.putString(STATE_PASSWORD, etPassword.getText().toString());
    }

    public void restoreState(Bundle savedInstanceState) {
        // MODEL
        mUserForm = (UserForm) savedInstanceState.getSerializable(STATE_MODEL);

        // TEXT FIELDS
        etEmail.setText(savedInstanceState.getString(STATE_EMAIL));
        etPhone.setText(savedInstanceState.getString(STATE_PHONE));
        etPassword.setText(savedInstanceState.getString(STATE_PASSWORD));
    }

    class EmailFocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (!hasFocus) {
                validateEmail();
            }
        }
    }

    class PhoneFocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (!hasFocus) {
                validatePhone();
            }
        }
    }

    class PasswordFocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (!hasFocus) {
                validatePassword();
            }
        }
    }

    private void validatePicture() {
        if (mUserForm.getFile() == null || !mUserForm.getFile().exists()) {

            RoundingParams roundingParams = imgTakePicture.getHierarchy().getRoundingParams();
            roundingParams.setBorderColor(mContext.getResources().getColor(R.color.colorError));
            imgTakePicture.getHierarchy().setRoundingParams(roundingParams);

            hasPicture = false;
        } else {
            RoundingParams roundingParams = imgTakePicture.getHierarchy().getRoundingParams();
            roundingParams.setBorderColor(mContext.getResources().getColor(R.color.colorAccent));
            imgTakePicture.getHierarchy().setRoundingParams(roundingParams);

            hasPicture = true;
        }
    }

    private void validateEmail() {
        if (etEmail.getText().toString().length() > 0) {
            if (ValidationUtils.isValidEmail(etEmail.getText().toString())) {
                tilEmail.setErrorEnabled(false);
            } else {
                tilEmail.setErrorEnabled(true);
                tilEmail.setError(mContext.getString(R.string.err_email_is_wrong));
            }
        } else {
            tilEmail.setErrorEnabled(false);
        }
    }

    private void validatePassword() {
        if (etPassword.getText().toString().length() > 0) {
            @StringRes
            int errMessageId = 0;
            // finding error
            if (etPassword.getText().length() < 6) {
                errMessageId = R.string.err_password_must_be_longer_than_six;
            } else if (ValidationUtils.containsDigit(etPassword.getText().toString())) {
                errMessageId = R.string.err_password_must_contain_one_digit;
            } else if (ValidationUtils.containsLetter(etPassword.getText().toString())) {
                errMessageId = R.string.err_password_must_contain_one_letter;
            }

            // setting error if any
            if (errMessageId == 0) {
                tilPassword.setErrorEnabled(false);
            } else {
                tilPassword.setErrorEnabled(true);

                tilPassword.setError(mContext.getString(errMessageId));
            }
        } else {
            tilPassword.setErrorEnabled(false);
        }
    }

    private void validatePhone() {
        if (etPhone.getText().toString().length() > 0) {
            if (ValidationUtils.isValidPhone(etPhone.getText().toString())) {
                tilPhone.setErrorEnabled(false);
            } else {
                tilPhone.setErrorEnabled(true);
                tilPhone.setError(mContext.getString(R.string.err_phone_is_wrong));
            }
        } else {
            tilPhone.setErrorEnabled(false);
        }
    }

    public boolean isValid() {

        validateEmail();
        validatePassword();
        validatePhone();
        validatePicture();

        if (!hasPicture) {
            return false;
        }

        if (tilEmail.isErrorEnabled() || etEmail.getText().length() == 0) {
            return false;
        }

        if (tilPhone.isErrorEnabled() || etPhone.getText().length() == 0) {
            return false;
        }

        if (tilPassword.isErrorEnabled() || etPassword.getText().length() == 0) {
            return false;
        }

        // VALIDATION PASSED
        return true;
    }

    public UserForm getPopulatedUserForm() {
        mUserForm.setEmail(etEmail.getText().toString());
        mUserForm.setPhone(etPhone.getText().toString());
        mUserForm.setPassword(etPassword.getText().toString());
        return mUserForm;
    }

    private void updateImgHolder() {
        if (mUserForm.getFile() == null || !mUserForm.getFile().exists()) {
            imgPictureHolder.setImageDrawable(null);
            hasPicture = false;
        } else {
            imgTakePicture.setVisibility(View.GONE);
            imgPictureHolder.setVisibility(View.VISIBLE);
            imgPictureHolder.setImageURI(Uri.fromFile(mUserForm.getFile()));
            hasPicture = true;
        }
    }
}
