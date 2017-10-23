package ru.romanbrazhnikov.userformsender.editor.model;

import android.os.Bundle;

/**
 * Created by roman on 23.10.17.
 * Common interface for Screen model that can be validated, saved and restored
 */

public interface ScreenModel {
    boolean isValid();

    /**
     * Saves the state during the activity lifecycle
     */
    void saveState(Bundle outState);

    /**
     * Restores the state during the activity lifecycle
     */
    void restoreState(Bundle savedInstanceState);

}
