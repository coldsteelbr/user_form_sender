package ru.romanbrazhnikov.userformsender.viewer.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.romanbrazhnikov.userformsender.R;

/**
 * Created by roman on 20.10.17.
 */

public class UserFormViewerActivity extends AppCompatActivity {

    public static void showActivityInstance(Context context) {
        Intent intent = new Intent(context, UserFormViewerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form_viewer);
    }
}
