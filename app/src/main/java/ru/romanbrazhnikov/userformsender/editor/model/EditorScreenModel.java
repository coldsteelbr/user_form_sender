package ru.romanbrazhnikov.userformsender.editor.model;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.romanbrazhnikov.userformsender.R;
import ru.romanbrazhnikov.userformsender.application.model.UserForm;
import ru.romanbrazhnikov.userformsender.editor.view.UserFormEditorActivity;

/**
 * Created by roman on 22.10.17.
 * <p>
 * Represents Edit Screen with its picture button and text fields
 */


public class EditorScreenModel implements ScreenModel {

    // CONSTANTS
    private static final String KEY_EMAIL = "KEY_EMAIL";
    private static final String KEY_PHONE = "KEY_PHONE";
    private static final String KEY_PASSWORD = "KEY_PASSWORD";
    private static final String STATE_MODEL = "STATE_MODEL";

    // WIDGETS
    @BindView(R.id.img_take_picture)
    SimpleDraweeView imgTakePicture;
    @BindView(R.id.img_picture_holder)
    SimpleDraweeView imgPictureHolder;

    // MODELS
    private Map<String, TextFieldModel> mTextFieldModels = new HashMap<>();
    private UserForm mUserForm = new UserForm();

    // FIELDS
    private Context mContext;
    private boolean hasPicture = false;

    public EditorScreenModel(LinearLayout ll_root, Context context) {
        mContext = context;

        // Binding widgets
        ButterKnife.bind(this, ll_root);

        // INIT EMAIL
        TextInputLayout tilEmail = ll_root.findViewById(R.id.til_email);
        EditText etEmail = ll_root.findViewById(R.id.et_email);
        mTextFieldModels.put(KEY_EMAIL, new EmailTextFieldModel(tilEmail, etEmail, context));


        // INIT PHONE
        TextInputLayout tilPhone = ll_root.findViewById(R.id.til_phone);
        EditText etPhone = ll_root.findViewById(R.id.et_phone);
        mTextFieldModels.put(KEY_PHONE, new PhoneTextFieldModel(tilPhone, etPhone, context));

        // INIT PASSWORD
        TextInputLayout tilPassword = ll_root.findViewById(R.id.til_password);
        EditText etPassword = ll_root.findViewById(R.id.et_password);
        mTextFieldModels.put(KEY_PASSWORD, new PasswordTextFieldModel(tilPassword, etPassword, context));

    }

    /**
     * Sets TakePictureListener for both TakePicture & PictureHolder
     */
    public void setTakePictureListener(UserFormEditorActivity.TakePictureListener listener) {
        imgTakePicture.setOnClickListener(listener);
        imgPictureHolder.setOnClickListener(listener);
    }

    /**
     * Sets model's file and loads it to the picture holder
     */
    public void setFile(File photoFile) {
        mUserForm.setFile(photoFile);
        updateImgHolder();
    }

    @Override
    public void saveState(Bundle outState) {
        outState.putSerializable(STATE_MODEL, mUserForm);

        for (ScreenModel currentModel :
                mTextFieldModels.values()) {
            currentModel.saveState(outState);
        }
    }

    @Override
    public void restoreState(Bundle savedInstanceState) {
        // MODEL
        mUserForm = (UserForm) savedInstanceState.getSerializable(STATE_MODEL);

        // TEXT FIELDS
        for (ScreenModel currentModel :
                mTextFieldModels.values()) {
            currentModel.restoreState(savedInstanceState);
        }
    }

    /**
     * Checks if picture is taken and highlights TakePicture's border in case of error
     */
    private void validatePicture() {
        if (mUserForm.getFile() == null || !mUserForm.getFile().exists()) {

            RoundingParams roundingParams = imgTakePicture.getHierarchy().getRoundingParams();
            roundingParams.setBorderColor(mContext.getResources().getColor(R.color.colorError));
            imgTakePicture.getHierarchy().setRoundingParams(roundingParams);

            hasPicture = false;
        } else {
            hasPicture = true;
        }
    }

    /**
     * Validate all the fields and shows errors if any
     */
    @Override
    public boolean isValid() {
        // validating nested screen models
        for (ScreenModel currentModel :
                mTextFieldModels.values()) {
            currentModel.isValid();
        }

        // validating if picture is taken
        validatePicture();
        if (!hasPicture) {
            return false;
        }

        // checking for error after the validation
        // and empty fileds
        for (TextFieldModel currentModel :
                mTextFieldModels.values()) {
            if (currentModel.isErrorOrEmpty()) {
                return false;
            }
        }

        // VALIDATION PASSED
        return true;
    }

    /**
     * Populates UserForm model with data from the screen model
     */
    public UserForm getPopulatedUserForm() {
        mUserForm.setEmail(mTextFieldModels.get(KEY_EMAIL).getValue());
        mUserForm.setPhone(mTextFieldModels.get(KEY_PHONE).getValue());
        mUserForm.setPassword(mTextFieldModels.get(KEY_PASSWORD).getValue());
        return mUserForm;
    }

    /**
     * Loads taken picture to the PictureHolder and hides the TakePicture
     */
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
