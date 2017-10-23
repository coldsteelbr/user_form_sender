package ru.romanbrazhnikov.userformsender.editor.model;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import ru.romanbrazhnikov.userformsender.R;
import ru.romanbrazhnikov.userformsender.utils.ValidationUtils;

/**
 * Created by roman on 23.10.17.
 * <p>
 * Represents Password text field screen model
 */

public class PasswordTextFieldModel extends TextFieldModel {

    private static final String STATE_PASSWORD = "STATE_PASSWORD";

    public PasswordTextFieldModel(TextInputLayout tilField, EditText etField, Context context) {
        super(tilField, etField, context);
    }

    @Override
    public boolean isValid() {
        @StringRes
        int errMessageId = 0;

        // finding error
        if (etField.getText().length() < 6) {
            errMessageId = R.string.err_password_must_be_longer_than_six;
        } else if (ValidationUtils.containsDigit(etField.getText().toString())) {
            errMessageId = R.string.err_password_must_contain_one_digit;
        } else if (ValidationUtils.containsLetter(etField.getText().toString())) {
            errMessageId = R.string.err_password_must_contain_one_letter;
        }

        // setting error if any
        if (errMessageId == 0) {
            tilField.setErrorEnabled(false);
            return true;
        } else {
            tilField.setErrorEnabled(true);
            tilField.setError(getContext().getString(errMessageId));
            return false;
        }
    }

    @Override
    public void saveState(Bundle outState) {
        outState.putString(STATE_PASSWORD, etField.getText().toString());
    }

    @Override
    public void restoreState(Bundle savedInstanceState) {
        etField.setText(savedInstanceState.getString(STATE_PASSWORD));
    }


}
