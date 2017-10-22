package ru.romanbrazhnikov.userformsender.viewer.view;

import android.content.Context;
import android.content.Intent;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.romanbrazhnikov.userformsender.R;
import ru.romanbrazhnikov.userformsender.application.model.UserForm;
import ru.romanbrazhnikov.userformsender.viewer.business.ImplicitActions;

/**
 * Created by roman on 20.10.17.
 */

public class UserFormViewerActivity extends AppCompatActivity {

    // CONSTANTS
    private final static String EXTRA_CUSTOM_USER_FORM = "EXTRA_CUSTOM_USER_FORM";

    // WIDGETS
    @BindView(R.id.img_picture)
    SimpleDraweeView imgPicture;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_password)
    TextView tvPassword;
    @BindView(R.id.b_send_by_email)
    Button bSendByEmail;

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
        ButterKnife.bind(this);

        mUserForm = (UserForm) getIntent().getSerializableExtra(EXTRA_CUSTOM_USER_FORM);

        initWidgets();
    }

    private void initWidgets() {
        bSendByEmail.setOnClickListener(new SendByEmailClick());

        imgPicture.setImageURI(Uri.fromFile(mUserForm.getFile()));
        ////
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(mUserForm.getFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        boolean isVertical = true ;

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                isVertical = false ;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                isVertical =false ;
                break;
        }
        ////////////

        if(isVertical){
            imgPicture.setRotation(90);
        }


        tvEmail.setText(mUserForm.getEmail());
        tvPhone.setText(mUserForm.getPhone());
        tvPassword.setText(mUserForm.getPassword());
    }

    class SendByEmailClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            //Toast.makeText(UserFormViewerActivity.this, "Sending", Toast.LENGTH_SHORT).show();
            ImplicitActions.openEmailChooser(mUserForm, UserFormViewerActivity.this);
        }
    }
}
