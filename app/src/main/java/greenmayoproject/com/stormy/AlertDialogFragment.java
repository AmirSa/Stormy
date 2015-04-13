package greenmayoproject.com.stormy;

import android.app.DialogFragment;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Amir on 4/12/2015.
 */
public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
         AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.error_title))
                .setMessage(context.getString(R.string.error_massage))
                .setPositiveButton(context.getString(R.string.ok_message), null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
