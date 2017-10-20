package ru.romanbrazhnikov.userformsender.viewer.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ru.romanbrazhnikov.userformsender.R;
import ru.romanbrazhnikov.userformsender.application.model.UserForm;

/**
 * Created by roman on 20.10.17.
 */

public class UserFormViewerActivity extends AppCompatActivity {

    // CONSTANTS
    private final static String EXTRA_CUSTOM_USER_FORM = "EXTRA_CUSTOM_USER_FORM";

    // WIDGETS
    private TextView tvEmail;
    private TextView tvPhone;
    private TextView tvPassword;
    private Button bSendByEmail;

    // FIELDS
    private UserForm mUserForm;

    public static void showActivityInstance(Context context, UserForm userForm) {
        Intent intent = new Intent(context, UserFormViewerActivity.class);
        intent.putExtra(EXTRA_CUSTOM_USER_FORM, userForm);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form_viewer);

        mUserForm = (UserForm) getIntent().getSerializableExtra(EXTRA_CUSTOM_USER_FORM);

        initWidgets();

    }

    private void initWidgets() {
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvPassword = (TextView) findViewById(R.id.tv_password);
        bSendByEmail = (Button) findViewById(R.id.b_send_by_email);
        bSendByEmail.setOnClickListener(new SendByEmailClick());

        tvEmail.setText(mUserForm.getEmail());
        tvPhone.setText(mUserForm.getPhone());
        tvPassword.setText(mUserForm.getPassword());
    }

    class SendByEmailClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Toast.makeText(UserFormViewerActivity.this, "Sending", Toast.LENGTH_SHORT).show();
        }
    }
}
