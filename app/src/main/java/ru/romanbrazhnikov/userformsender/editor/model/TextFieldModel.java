package ru.romanbrazhnikov.userformsender.editor.model;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

/**
 * Created by roman on 23.10.17.
 */

public abstract class TextFieldModel implements ScreenModel {
    protected TextInputLayout tilField;
    protected EditText etField;
    private Context mContext;

    public TextFieldModel(TextInputLayout tilField, EditText etField, Context context) {
        this.tilField = tilField;
        this.etField = etField;
        mContext = context;
        this.tilField.setOnFocusChangeListener(new FieldFocusChangeListener());
    }

    public Context getContext() {
        return mContext;
    }

    public boolean isErrorOrEmpty() {
        if (tilField.isErrorEnabled() || etField.getText().length() == 0) {
            return true;
        }
        return false;
    }

    public String getValue() {
        return etField.getText().toString();
    }

    private class FieldFocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (!hasFocus) {
                if (etField.getText().toString().length() > 0) {
                    isValid();
                } else {
                    tilField.setErrorEnabled(false);
                }
            }
        }
    }

}
