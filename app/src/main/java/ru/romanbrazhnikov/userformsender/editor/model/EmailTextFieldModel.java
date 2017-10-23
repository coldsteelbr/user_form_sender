package ru.romanbrazhnikov.userformsender.editor.model;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import ru.romanbrazhnikov.userformsender.R;
import ru.romanbrazhnikov.userformsender.utils.ValidationUtils;

/**
 * Created by roman on 23.10.17.
 */

public class EmailTextFieldModel extends TextFieldModel {

    public static final String STATE_EMAIL = "STATE_EMAIL";

    public EmailTextFieldModel(TextInputLayout tilField, EditText etField, Context context) {
        super(tilField, etField, context);
    }

    @Override
    public void saveState(Bundle outState) {
        outState.putString(STATE_EMAIL, etField.getText().toString());
    }

    @Override
    public void restoreState(Bundle savedInstanceState) {
        etField.setText(savedInstanceState.getString(STATE_EMAIL));
    }

    @Override
    public boolean isValid() {
        if (ValidationUtils.isValidEmail(etField.getText().toString())) {
            tilField.setErrorEnabled(false);
            return true;
        } else {
            tilField.setErrorEnabled(true);
            tilField.setError(getContext().getString(R.string.err_email_is_wrong));
            return true;
        }
    }

}
