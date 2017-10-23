package ru.romanbrazhnikov.userformsender.viewer.business;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import ru.romanbrazhnikov.userformsender.R;
import ru.romanbrazhnikov.userformsender.application.model.UserForm;

/**
 * Created by roman on 21.10.17.
 * <p>
 * Tool class to open "Email chooser" and send UserForm data
 */

public class ImplicitActions {
    public static void openEmailChooser(UserForm form, Context context) {

        Intent emailIntent
                = new Intent(
                Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", "mail@example.com", null));

        emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                context.getString(R.string.app_name) + ": " +
                        context.getString(R.string.form_data));

        emailIntent.putExtra(
                Intent.EXTRA_TEXT,
                String.format(context.getString(R.string.email_format),
                        form.getEmail(),
                        form.getPhone()));

        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(form.getFile()));

        context.startActivity(
                Intent.createChooser(emailIntent, context.getString(R.string.send_by_mail)));

    }

}
