package ru.romanbrazhnikov.userformsender.editor.view;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import ru.romanbrazhnikov.userformsender.R;
import ru.romanbrazhnikov.userformsender.application.model.UserForm;
import ru.romanbrazhnikov.userformsender.utils.ValidationUtils;
import ru.romanbrazhnikov.userformsender.viewer.view.UserFormViewerActivity;

public class UserFormEditorActivity extends AppCompatActivity {

    // WIDGETS
    private EditText etEmail;
    private EditText etPhone;
    private EditText etPassword;

    private TextInputLayout tilEmail;
    private TextInputLayout tilPhone;
    private TextInputLayout tilPassword;

    private Button bView;

    private LinearLayout ll_root;

    // FIELDS
    UserForm mUserForm = new UserForm();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form_editor);

        initWidgets();
        if (savedInstanceState != null) {
            restoreState();
        }
    }

    private void initWidgets() {
        ll_root = (LinearLayout) findViewById(R.id.ll_root);

        etEmail = (EditText) findViewById(R.id.et_email);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etPassword = (EditText) findViewById(R.id.et_password);

        tilEmail = (TextInputLayout) findViewById(R.id.til_email);
        tilPhone = (TextInputLayout) findViewById(R.id.til_phone);
        tilPassword = (TextInputLayout) findViewById(R.id.til_password);


        etEmail.setOnFocusChangeListener(new EmailFocusChangeListener());
        etPhone.setOnFocusChangeListener(new PhoneFocusChangeListener());
        etPassword.setOnFocusChangeListener(new PasswordFocusChangeListener());


        bView = (Button) findViewById(R.id.b_view);
        bView.setOnClickListener(new ViewButtonClick());
    }

    private void restoreState() {
        // TODO: restore state
    }

    private boolean isValid() {

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

    private void populateModel() {
        mUserForm.setEmail(etEmail.getText().toString());
        mUserForm.setPhone(etPhone.getText().toString());
        mUserForm.setPassword(etPassword.getText().toString());
    }

    private void validateEmail() {
        if (etEmail.getText().toString().length() > 0) {
            if (ValidationUtils.isValidEmail(etEmail.getText().toString())) {
                tilEmail.setErrorEnabled(false);
            } else {
                tilEmail.setErrorEnabled(true);
                tilEmail.setError(getString(R.string.err_email_is_wrong));
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

                tilPassword.setError(getString(errMessageId));
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
                tilPhone.setError(getString(R.string.err_phone_is_wrong));
            }
        } else {
            tilPhone.setErrorEnabled(false);
        }
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

    class ViewButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (isValid()) {
                populateModel();
                UserFormViewerActivity.showActivityInstance(UserFormEditorActivity.this, mUserForm);
            } else {
                // TODO: proper validation
                Snackbar.make(ll_root, R.string.err_fill_fields_correctly, BaseTransientBottomBar.LENGTH_LONG)
                        .show();
            }
        }
    }

}
