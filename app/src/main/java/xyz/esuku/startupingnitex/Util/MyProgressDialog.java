package xyz.esuku.startupingnitex.Util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import xyz.esuku.startupingnitex.R;

public class MyProgressDialog extends AppCompatActivity {
    Dialog dialog;
    TextView message_view;
    Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public MyProgressDialog(Context context) {
        this.context = context;
    }

    public Dialog create_dialog(String message) {

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        if (message != null) {
            message_view = (TextView) dialog.findViewById(R.id.dialog_message);
            message_view.setText(message);
        }

        dialog.show();
        return dialog;


    }

    public void setMessage(String message) {
        create_dialog(message);
    }

    public void dismiss() {
        //create_dialog(null);
        dialog.hide();
    }

    public void hide() {
        //create_dialog(null);
        dialog.hide();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dialog.hide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog.hide();
    }
}