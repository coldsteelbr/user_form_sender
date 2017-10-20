package ru.romanbrazhnikov.userformsender.editor.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ru.romanbrazhnikov.userformsender.R;
import ru.romanbrazhnikov.userformsender.application.model.UserForm;
import ru.romanbrazhnikov.userformsender.viewer.view.UserFormViewerActivity;

public class UserFormEditorActivity extends AppCompatActivity {

    private Button bView;
    UserForm mUserForm = new UserForm();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form_editor);

        initWidgets();
        if(savedInstanceState != null){
            restoreState();
        }
    }

    private void initWidgets() {
        bView = (Button) findViewById(R.id.b_view);
        bView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserFormViewerActivity.showActivityInstance(UserFormEditorActivity.this);
            }
        });
    }

    private void restoreState(){
        // TODO: restore state
    }

}
