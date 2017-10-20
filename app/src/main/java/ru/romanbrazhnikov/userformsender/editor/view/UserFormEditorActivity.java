package ru.romanbrazhnikov.userformsender.editor.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ru.romanbrazhnikov.userformsender.R;
import ru.romanbrazhnikov.userformsender.application.model.UserForm;
import ru.romanbrazhnikov.userformsender.viewer.view.UserFormViewerActivity;

public class UserFormEditorActivity extends AppCompatActivity {

    // WIDGETS
    private EditText etEmail;
    private EditText etPhone;
    private EditText etPassword;
    private Button bView;

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
        etEmail = (EditText) findViewById(R.id.et_email);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etPassword = (EditText) findViewById(R.id.et_password);

        bView = (Button) findViewById(R.id.b_view);
        bView.setOnClickListener(new ViewButtonClick());
    }

    private void restoreState() {
        // TODO: restore state
    }

    class ViewButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            mUserForm.setEmail(etEmail.getText().toString());
            mUserForm.setPhone(etPhone.getText().toString());
            mUserForm.setPassword(etPassword.getText().toString());
            UserFormViewerActivity.showActivityInstance(UserFormEditorActivity.this, mUserForm);
        }
    }
}
