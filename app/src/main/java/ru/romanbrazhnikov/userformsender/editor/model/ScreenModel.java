package ru.romanbrazhnikov.userformsender.editor.model;

import android.os.Bundle;

/**
 * Created by roman on 23.10.17.
 */

public interface ScreenModel {
    boolean isValid();

    void saveState(Bundle outState);

    void restoreState(Bundle savedInstanceState);

}
