package ru.romanbrazhnikov.userformsender.editor.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.StringRes;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.romanbrazhnikov.userformsender.R;
import ru.romanbrazhnikov.userformsender.application.model.UserForm;
import ru.romanbrazhnikov.userformsender.utils.ValidationUtils;
import ru.romanbrazhnikov.userformsender.viewer.view.UserFormViewerActivity;

public class UserFormEditorActivity extends AppCompatActivity {

    // CONSTANTS
    private static final int CAMERA_REQUEST = 0;
    private static final String STATE_EMAIL = "STATE_EMAIL";
    private static final String STATE_PHONE = "STATE_PHONE";
    private static final String STATE_PASSWORD = "STATE_PASSWORD";
    private static final String STATE_MODEL = "STATE_MODEL";
    private static final String STATE_FILE = "STATE_FILE";

    // WIDGETS
    @BindView(R.id.img_take_picture)
    SimpleDraweeView imgTakePicture;

    @BindView(R.id.img_picture_holder)
    ImageView imgPictureHolder;

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

    @BindView(R.id.b_view)
    Button bView;

    @BindView(R.id.ll_root)
    LinearLayout ll_root;

    // FIELDS
    UserForm mUserForm = new UserForm();
    File mPhotoFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form_editor);

        initWidgets();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                mUserForm.setFile(mPhotoFile);
                updateImgHolder();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(STATE_MODEL, mUserForm);
        outState.putSerializable(STATE_FILE, mPhotoFile);
        outState.putString(STATE_EMAIL, etEmail.getText().toString());
        outState.putString(STATE_PHONE, etPhone.getText().toString());
        outState.putString(STATE_PASSWORD, etPassword.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // FILE
        mPhotoFile = (File) savedInstanceState.getSerializable(STATE_FILE);

        // MODEL
        mUserForm = (UserForm) savedInstanceState.getSerializable(STATE_MODEL);

        if (mUserForm != null) {
            // setting image
            if (mUserForm.getFile() == null || !mUserForm.getFile().exists()) {
                imgPictureHolder.setVisibility(View.GONE);
                imgTakePicture.setVisibility(View.VISIBLE);
            } else {
                updateImgHolder();
            }
        }

        // TEXT FIELDS
        etEmail.setText(savedInstanceState.getString(STATE_EMAIL));
        etPhone.setText(savedInstanceState.getString(STATE_PHONE));
        etPassword.setText(savedInstanceState.getString(STATE_PASSWORD));
    }

    private void updateImgHolder() {
        if (mUserForm.getFile() == null || !mUserForm.getFile().exists()) {
            imgPictureHolder.setImageDrawable(null);
        } else {
            imgTakePicture.setVisibility(View.GONE);
            imgPictureHolder.setVisibility(View.VISIBLE);
            imgPictureHolder.setImageURI(Uri.fromFile(mUserForm.getFile()));
        }
    }

    private void initWidgets() {
        ButterKnife.bind(this);

        TakePictureListener listener = new TakePictureListener();
        imgTakePicture.setOnClickListener(listener);
        imgPictureHolder.setOnClickListener(listener);

        etEmail.setOnFocusChangeListener(new EmailFocusChangeListener());
        etPhone.setOnFocusChangeListener(new PhoneFocusChangeListener());
        // TODO: set custom listener
        etPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        etPassword.setOnFocusChangeListener(new PasswordFocusChangeListener());

        bView.setOnClickListener(new ViewButtonClick());
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

    class TakePictureListener implements View.OnClickListener {

        // from BNRG

        private String getPhotoFilename() {
            return "IMG_" + System.currentTimeMillis() + ".jpg";
        }

        private File getPhotoFile() {

            ////////
            String state = Environment.getExternalStorageState();
            File filesDir;

            // Make sure it's available
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                // We can read and write the media
                filesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            } else {
                // Load another directory, probably local memory
                filesDir = getFilesDir();
            }

            if (filesDir == null) {
                return null;
            }
            return new File(filesDir, getPhotoFilename());
        }

        @Override
        public void onClick(View view) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            mPhotoFile = getPhotoFile();
            Uri uri = Uri.fromFile(mPhotoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }
}
