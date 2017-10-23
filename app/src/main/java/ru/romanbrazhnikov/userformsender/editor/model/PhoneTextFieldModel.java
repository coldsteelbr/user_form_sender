package ru.romanbrazhnikov.userformsender.editor.model;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.widget.EditText;

import ru.romanbrazhnikov.userformsender.R;
import ru.romanbrazhnikov.userformsender.utils.ValidationUtils;

/**
 * Created by roman on 23.10.17.
 */

public class PhoneTextFieldModel extends TextFieldModel {

    private static final String STATE_PHONE = "STATE_PHONE";

    public PhoneTextFieldModel(TextInputLayout tilField, EditText etField, Context context) {
        super(tilField, etField, context);

        etField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    @Override
    public boolean isValid() {
        if (ValidationUtils.isValidPhone(etField.getText().toString())) {
            tilField.setErrorEnabled(false);
            return true;
        } else {
            tilField.setErrorEnabled(true);
            tilField.setError(getContext().getString(R.string.err_phone_is_wrong));
            return false;
        }
    }

    @Override
    public void saveState(Bundle outState) {
        outState.putString(STATE_PHONE, etField.getText().toString());
    }

    @Override
    public void restoreState(Bundle savedInstanceState) {
        etField.setText(savedInstanceState.getString(STATE_PHONE));
    }

}
