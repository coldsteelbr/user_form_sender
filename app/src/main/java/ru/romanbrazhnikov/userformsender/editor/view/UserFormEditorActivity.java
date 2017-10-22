package ru.romanbrazhnikov.userformsender.editor.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.romanbrazhnikov.userformsender.R;
import ru.romanbrazhnikov.userformsender.application.model.UserForm;
import ru.romanbrazhnikov.userformsender.editor.model.EditorScreenModel;
import ru.romanbrazhnikov.userformsender.viewer.view.UserFormViewerActivity;

public class UserFormEditorActivity extends AppCompatActivity {

    // CONSTANTS
    private static final int CAMERA_REQUEST = 0;
    private static final String STATE_FILE = "STATE_FILE";

    // MODEL
    private EditorScreenModel mScreenModel;

    // WIDGETS
    @BindView(R.id.ll_root)
    LinearLayout ll_root;
    @BindView(R.id.b_view)
    Button bView;

    // FIELDS
    File mPhotoFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form_editor);

        initWidgets();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                mScreenModel.setFile(mPhotoFile);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // SCREEN MODEL
        mScreenModel.saveState(outState);

        // FILE
        outState.putSerializable(STATE_FILE, mPhotoFile);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // FILE
        mPhotoFile = (File) savedInstanceState.getSerializable(STATE_FILE);

        // SCREEN MODEL
        mScreenModel = new EditorScreenModel(ll_root, this);
        mScreenModel.restoreState(savedInstanceState);
        mScreenModel.setFile(mPhotoFile);
    }


    private void initWidgets() {

        ButterKnife.bind(this);

        mScreenModel = new EditorScreenModel(ll_root, this);

        TakePictureListener listener = new TakePictureListener();
        mScreenModel.setTakePictureListener(listener);

        bView.setOnClickListener(new ViewButtonClick());
    }


    class ViewButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (mScreenModel.isValid()) {
                UserForm userForm = mScreenModel.getPopulatedUserForm();
                UserFormViewerActivity.showActivityInstance(UserFormEditorActivity.this, userForm);
            } else {
                // TODO: proper validation
                Snackbar.make(ll_root, R.string.err_fill_fields_correctly, BaseTransientBottomBar.LENGTH_LONG)
                        .show();
            }
        }
    }

    public class TakePictureListener implements View.OnClickListener {

        // from BNRG

        private String getPhotoFilename() {
            return "IMG_" + System.currentTimeMillis() + ".jpg";
        }

        private File getPhotoFile() {

            ////////
            String state = Environment.getExternalStorageState();
            File filesDir;

            // Make sure it's available
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                // We can read and write the media
                filesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            } else {
                // Load another directory, probably local memory
                filesDir = getFilesDir();
            }

            if (filesDir == null) {
                return null;
            }
            return new File(filesDir, getPhotoFilename());
        }

        @Override
        public void onClick(View view) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            mPhotoFile = getPhotoFile();
            Uri uri = Uri.fromFile(mPhotoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }
}
