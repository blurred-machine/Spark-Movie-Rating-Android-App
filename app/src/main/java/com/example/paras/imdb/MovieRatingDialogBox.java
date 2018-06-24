package com.example.paras.imdb;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

// class that creates the dialog box for the ratign the movie.
public class MovieRatingDialogBox extends AppCompatDialogFragment {
    Context mContext;

   public void setContext(Context context){
       this.mContext = context;
   }

   // overridden method of the AppCompatDialogFragment class is called when the dialog box is created.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Toast.makeText(mContext, "dialog is created", Toast.LENGTH_SHORT).show();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // object of the AlertBox.
        LayoutInflater inflater = getLayoutInflater();    // setting the layout inflater to inflate the layout (xml file)
        final View view = inflater.inflate(R.layout.rate_movie_dialog_box, null);   // inflating the layout for the activity.
        builder.setView(view);  // setting the view for the Alert box.
        builder.setTitle("Create New ToDo");    // title displayed on top of alert box.
        builder.setPositiveButton("Save",null);     // positive button as save the data.
        builder.setNegativeButton("cancel",null);   // negative button as cancel the dialog box.
        final AlertDialog mAlertDialog = builder.create();

        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                // giving the positive and negative buttons as variable names.
                Button pos = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button neg = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                // set onClickListener for the negative button.
                neg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAlertDialog.dismiss();
                    }
                });
                // set onClickListener for the positive button.
//                pos.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                    }
//                });
            }
        });
        return mAlertDialog;
    }
}
